package com.ds.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.entity.Warehouse;

public interface WarehouseService {

    Page<Warehouse> listWarehouses(Integer page, Integer size);

    Warehouse getWarehouseById(Long id);

    void createWarehouse(Warehouse warehouse);

    void updateWarehouse(Warehouse warehouse);

    void deleteWarehouse(Long id);
}