package com.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.entity.Warehouse;
import com.ds.exception.BusinessException;
import com.ds.mapper.WarehouseMapper;
import com.ds.service.WarehouseService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseMapper warehouseMapper;

    public WarehouseServiceImpl(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public Page<Warehouse> listWarehouses(Integer page, Integer size) {
        Page<Warehouse> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Warehouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Warehouse::getCreateTime);
        return warehouseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return warehouseMapper.selectById(id);
    }

    @Override
    public void createWarehouse(Warehouse warehouse) {
        Warehouse existing = warehouseMapper.selectOne(
            new LambdaQueryWrapper<Warehouse>().eq(Warehouse::getCode, warehouse.getCode())
        );
        if (existing != null) {
            throw new BusinessException("仓库编码已存在");
        }
        warehouse.setStatus(1);
        warehouseMapper.insert(warehouse);
    }

    @Override
    public void updateWarehouse(Warehouse warehouse) {
        warehouseMapper.updateById(warehouse);
    }

    @Override
    public void deleteWarehouse(Long id) {
        warehouseMapper.deleteById(id);
    }
}