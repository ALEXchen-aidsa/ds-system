package com.ds.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.dto.StockRecordDTO;
import com.ds.entity.Inventory;
import com.ds.entity.StockRecord;

import java.util.List;

public interface InventoryService {

    Page<Inventory> listInventory(Long warehouseId, Integer page, Integer size);

    void stockIn(StockRecordDTO dto);

    void stockOut(StockRecordDTO dto);

    Page<StockRecord> listRecords(Long warehouseId, Integer type, Integer page, Integer size);
}