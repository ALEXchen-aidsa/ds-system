package com.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.dto.StockRecordDTO;
import com.ds.entity.Inventory;
import com.ds.entity.StockRecord;
import com.ds.exception.BusinessException;
import com.ds.mapper.InventoryMapper;
import com.ds.mapper.StockRecordMapper;
import com.ds.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final StockRecordMapper stockRecordMapper;

    public InventoryServiceImpl(InventoryMapper inventoryMapper, StockRecordMapper stockRecordMapper) {
        this.inventoryMapper = inventoryMapper;
        this.stockRecordMapper = stockRecordMapper;
    }

    @Override
    public Page<Inventory> listInventory(Long warehouseId, Integer page, Integer size) {
        Page<Inventory> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        if (warehouseId != null) {
            wrapper.eq(Inventory::getWarehouseId, warehouseId);
        }
        wrapper.orderByDesc(Inventory::getUpdateTime);
        return inventoryMapper.selectPage(pageParam, wrapper);
    }

    @Override
    @Transactional
    public void stockIn(StockRecordDTO dto) {
        StockRecord record = new StockRecord();
        record.setRecordNo("IN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        record.setType(1);
        record.setWarehouseId(dto.getWarehouseId());
        record.setProductId(dto.getProductId());
        record.setQuantity(dto.getQuantity());
        record.setRemark(dto.getRemark());
        record.setStatus(1);
        stockRecordMapper.insert(record);

        Inventory inventory = inventoryMapper.findByWarehouseAndProduct(dto.getWarehouseId(), dto.getProductId());
        if (inventory == null) {
            inventory = new Inventory();
            inventory.setWarehouseId(dto.getWarehouseId());
            inventory.setProductId(dto.getProductId());
            inventory.setQuantity(dto.getQuantity());
            inventory.setFrozenQuantity(0);
            inventoryMapper.insert(inventory);
        } else {
            inventory.setQuantity(inventory.getQuantity() + dto.getQuantity());
            inventoryMapper.updateById(inventory);
        }
    }

    @Override
    @Transactional
    public void stockOut(StockRecordDTO dto) {
        Inventory inventory = inventoryMapper.findByWarehouseAndProduct(dto.getWarehouseId(), dto.getProductId());
        if (inventory == null || inventory.getQuantity() < dto.getQuantity()) {
            throw new BusinessException("库存不足");
        }

        StockRecord record = new StockRecord();
        record.setRecordNo("OUT" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        record.setType(2);
        record.setWarehouseId(dto.getWarehouseId());
        record.setProductId(dto.getProductId());
        record.setQuantity(dto.getQuantity());
        record.setRemark(dto.getRemark());
        record.setStatus(1);
        stockRecordMapper.insert(record);

        inventory.setQuantity(inventory.getQuantity() - dto.getQuantity());
        inventoryMapper.updateById(inventory);
    }

    @Override
    public Page<StockRecord> listRecords(Long warehouseId, Integer type, Integer page, Integer size) {
        Page<StockRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        if (warehouseId != null) {
            wrapper.eq(StockRecord::getWarehouseId, warehouseId);
        }
        if (type != null) {
            wrapper.eq(StockRecord::getType, type);
        }
        wrapper.orderByDesc(StockRecord::getCreateTime);
        return stockRecordMapper.selectPage(pageParam, wrapper);
    }
}