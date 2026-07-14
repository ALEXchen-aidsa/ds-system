package com.ds.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ds.common.Result;
import com.ds.entity.ProductCategory;
import com.ds.mapper.ProductCategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "分类管理", description = "商品分类接口")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final ProductCategoryMapper categoryMapper;

    public CategoryController(ProductCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Operation(summary = "获取分类列表")
    @GetMapping("/list")
    public Result<List<ProductCategory>> list() {
        List<ProductCategory> list = categoryMapper.selectList(
            new LambdaQueryWrapper<ProductCategory>()
                .eq(ProductCategory::getStatus, 1)
                .orderByAsc(ProductCategory::getSortOrder)
        );
        return Result.success(list);
    }

    @Operation(summary = "创建分类")
    @PostMapping
    public Result<Void> create(@RequestBody ProductCategory category) {
        categoryMapper.insert(category);
        return Result.success();
    }
}