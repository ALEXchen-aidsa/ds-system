package com.ds.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.dto.OrderDTO;
import com.ds.dto.OrderQueryDTO;
import com.ds.entity.Order;
import com.ds.entity.OrderItem;

import java.util.List;

public interface OrderService {

    Order createOrder(Long userId, OrderDTO dto);

    Page<Order> listOrders(OrderQueryDTO query);

    Order getOrderById(Long id);

    void updateOrderStatus(Long id, Integer status);

    List<OrderItem> getOrderItems(Long orderId);

    void cancelExpiredOrders();
}