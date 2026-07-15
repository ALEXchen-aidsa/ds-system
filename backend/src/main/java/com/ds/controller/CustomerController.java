package com.ds.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.common.Result;
import com.ds.entity.Customer;
import com.ds.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "客户管理", description = "客户CRUD接口")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "获取客户列表")
    @GetMapping("/list")
    public Result<Page<Customer>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(customerService.listCustomers(page, size));
    }

    @Operation(summary = "创建客户")
    @PostMapping
    public Result<Void> create(@RequestBody Customer customer) {
        customerService.createCustomer(customer);
        return Result.success();
    }

    @Operation(summary = "更新客户")
    @PutMapping
    public Result<Void> update(@RequestBody Customer customer) {
        customerService.updateCustomer(customer);
        return Result.success();
    }

    @Operation(summary = "删除客户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return Result.success();
    }
}