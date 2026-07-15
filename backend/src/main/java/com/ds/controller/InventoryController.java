package com.ds.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.common.Result;
import com.ds.dto.StockRecordDTO;
import com.ds.entity.Inventory;
import com.ds.entity.StockRecord;
import com.ds.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "库存管理", description = "库存相关接口")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "获取库存列表")
    @GetMapping("/list")
    public Result<Page<Inventory>> list(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(inventoryService.listInventory(warehouseId, page, size));
    }

    @Operation(summary = "入库")
    @PostMapping("/in")
    public Result<Void> stockIn(@Valid @RequestBody StockRecordDTO dto) {
        inventoryService.stockIn(dto);
        return Result.success();
    }

    @Operation(summary = "出库")
    @PostMapping("/out")
    public Result<Void> stockOut(@Valid @RequestBody StockRecordDTO dto) {
        inventoryService.stockOut(dto);
        return Result.success();
    }

    @Operation(summary = "获取出入库记录")
    @GetMapping("/records")
    public Result<Page<StockRecord>> records(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(inventoryService.listRecords(warehouseId, type, page, size));
    }
}