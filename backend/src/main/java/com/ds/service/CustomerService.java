package com.ds.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.entity.Customer;

public interface CustomerService {
    Page<Customer> listCustomers(Integer page, Integer size);
    void createCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(Long id);
}