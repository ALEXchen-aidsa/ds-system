package com.ds.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.entity.Supplier;

public interface SupplierService {
    Page<Supplier> listSuppliers(Integer page, Integer size);
    void createSupplier(Supplier supplier);
    void updateSupplier(Supplier supplier);
    void deleteSupplier(Long id);
}