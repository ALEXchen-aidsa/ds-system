package com.ds.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.dto.OrderDTO;
import com.ds.dto.OrderQueryDTO;
import com.ds.entity.Order;

public interface OrderService {

    Order createOrder(Long userId, OrderDTO dto);

    Page<Order> listOrders(OrderQueryDTO query);

    Order getOrderById(Long id);

    void cancelExpiredOrders();
}