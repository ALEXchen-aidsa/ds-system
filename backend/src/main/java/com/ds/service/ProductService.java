package com.ds.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.dto.ProductDTO;
import com.ds.dto.ProductQueryDTO;
import com.ds.entity.Product;

public interface ProductService {

    Page<Product> listProducts(ProductQueryDTO query);

    Product getProductById(Long id);

    void createProduct(ProductDTO dto);

    void updateProduct(ProductDTO dto);

    void deleteProduct(Long id);
}