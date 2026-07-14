package com.ds.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.common.Result;
import com.ds.dto.OrderDTO;
import com.ds.dto.OrderQueryDTO;
import com.ds.entity.Order;
import com.ds.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public Result<Order> create(@Valid @RequestBody OrderDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getCredentials();
        return Result.success(orderService.createOrder(userId, dto));
    }

    @Operation(summary = "获取订单列表")
    @GetMapping("/list")
    public Result<Page<Order>> list(OrderQueryDTO query) {
        return Result.success(orderService.listOrders(query));
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        return Result.success(orderService.getOrderById(id));
    }
}