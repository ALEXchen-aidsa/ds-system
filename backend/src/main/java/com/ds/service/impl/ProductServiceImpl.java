package com.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.dto.ProductDTO;
import com.ds.dto.ProductQueryDTO;
import com.ds.entity.Product;
import com.ds.exception.BusinessException;
import com.ds.mapper.ProductMapper;
import com.ds.service.ProductService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PRODUCT_CACHE_KEY = "product:";
    private static final long CACHE_EXPIRE_HOURS = 2;

    public ProductServiceImpl(ProductMapper productMapper, RedisTemplate<String, Object> redisTemplate) {
        this.productMapper = productMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Page<Product> listProducts(ProductQueryDTO query) {
        Page<Product> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(Product::getName, query.getName());
        }
        if (query.getCode() != null && !query.getCode().isEmpty()) {
            wrapper.like(Product::getCode, query.getCode());
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, query.getCategoryId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(Product::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(Product::getCreateTime);

        return productMapper.selectPage(page, wrapper);
    }

    @Override
    public Product getProductById(Long id) {
        String cacheKey = PRODUCT_CACHE_KEY + id;
        Product product = (Product) redisTemplate.opsForValue().get(cacheKey);

        if (product == null) {
            product = productMapper.selectById(id);
            if (product != null) {
                redisTemplate.opsForValue().set(cacheKey, product, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            }
        }

        return product;
    }

    @Override
    public void createProduct(ProductDTO dto) {
        Product existing = productMapper.selectOne(
            new LambdaQueryWrapper<Product>().eq(Product::getCode, dto.getCode())
        );
        if (existing != null) {
            throw new BusinessException("商品编码已存在");
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setCode(dto.getCode());
        product.setCategoryId(dto.getCategoryId());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        productMapper.insert(product);
    }

    @Override
    public void updateProduct(ProductDTO dto) {
        Product product = productMapper.selectById(dto.getId());
        if (product == null) {
            throw new BusinessException("商品不存在");
        }

        product.setName(dto.getName());
        product.setCategoryId(dto.getCategoryId());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());
        product.setStatus(dto.getStatus());
        productMapper.updateById(product);

        redisTemplate.delete(PRODUCT_CACHE_KEY + dto.getId());
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
        redisTemplate.delete(PRODUCT_CACHE_KEY + id);
    }
}