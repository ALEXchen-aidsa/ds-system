package com.ds.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.common.Result;
import com.ds.entity.Warehouse;
import com.ds.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "仓库管理", description = "仓库CRUD接口")
@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Operation(summary = "获取仓库列表")
    @GetMapping("/list")
    public Result<Page<Warehouse>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(warehouseService.listWarehouses(page, size));
    }

    @Operation(summary = "获取仓库详情")
    @GetMapping("/{id}")
    public Result<Warehouse> getById(@PathVariable Long id) {
        return Result.success(warehouseService.getWarehouseById(id));
    }

    @Operation(summary = "创建仓库")
    @PostMapping
    public Result<Void> create(@RequestBody Warehouse warehouse) {
        warehouseService.createWarehouse(warehouse);
        return Result.success();
    }

    @Operation(summary = "更新仓库")
    @PutMapping
    public Result<Void> update(@RequestBody Warehouse warehouse) {
        warehouseService.updateWarehouse(warehouse);
        return Result.success();
    }

    @Operation(summary = "删除仓库")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return Result.success();
    }
}