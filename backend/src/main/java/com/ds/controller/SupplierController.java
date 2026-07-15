package com.ds.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.common.Result;
import com.ds.entity.Supplier;
import com.ds.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "供应商管理", description = "供应商CRUD接口")
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation(summary = "获取供应商列表")
    @GetMapping("/list")
    public Result<Page<Supplier>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(supplierService.listSuppliers(page, size));
    }

    @Operation(summary = "创建供应商")
    @PostMapping
    public Result<Void> create(@RequestBody Supplier supplier) {
        supplierService.createSupplier(supplier);
        return Result.success();
    }

    @Operation(summary = "更新供应商")
    @PutMapping
    public Result<Void> update(@RequestBody Supplier supplier) {
        supplierService.updateSupplier(supplier);
        return Result.success();
    }

    @Operation(summary = "删除供应商")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return Result.success();
    }
}