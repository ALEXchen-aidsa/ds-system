package com.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.entity.Supplier;
import com.ds.mapper.SupplierMapper;
import com.ds.service.SupplierService;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierMapper supplierMapper) {
        this.supplierMapper = supplierMapper;
    }

    @Override
    public Page<Supplier> listSuppliers(Integer page, Integer size) {
        Page<Supplier> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Supplier::getCreateTime);
        return supplierMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public void createSupplier(Supplier supplier) {
        supplier.setStatus(1);
        supplierMapper.insert(supplier);
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        supplierMapper.updateById(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        supplierMapper.deleteById(id);
    }
}