package com.ds.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.common.Result;
import com.ds.dto.ProductDTO;
import com.ds.dto.ProductQueryDTO;
import com.ds.entity.Product;
import com.ds.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品管理", description = "商品CRUD接口")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "获取商品列表")
    @GetMapping("/list")
    public Result<Page<Product>> list(ProductQueryDTO query) {
        return Result.success(productService.listProducts(query));
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    @Operation(summary = "创建商品")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody ProductDTO dto) {
        productService.createProduct(dto);
        return Result.success();
    }

    @Operation(summary = "更新商品")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody ProductDTO dto) {
        productService.updateProduct(dto);
        return Result.success();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }
}