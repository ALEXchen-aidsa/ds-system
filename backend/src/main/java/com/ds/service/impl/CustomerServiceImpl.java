package com.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.entity.Customer;
import com.ds.mapper.CustomerMapper;
import com.ds.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    @Override
    public Page<Customer> listCustomers(Integer page, Integer size) {
        Page<Customer> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Customer::getCreateTime);
        return customerMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public void createCustomer(Customer customer) {
        customer.setStatus(1);
        customerMapper.insert(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerMapper.updateById(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerMapper.deleteById(id);
    }
}