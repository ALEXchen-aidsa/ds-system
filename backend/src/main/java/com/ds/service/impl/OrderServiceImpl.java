package com.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.dto.OrderDTO;
import com.ds.dto.OrderItemDTO;
import com.ds.dto.OrderQueryDTO;
import com.ds.entity.Order;
import com.ds.entity.OrderItem;
import com.ds.entity.Product;
import com.ds.exception.BusinessException;
import com.ds.mapper.OrderItemMapper;
import com.ds.mapper.OrderMapper;
import com.ds.mapper.ProductMapper;
import com.ds.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ORDER_STOCK_LOCK = "order:stock:";

    public OrderServiceImpl(OrderMapper orderMapper,
                           OrderItemMapper orderItemMapper,
                           ProductMapper productMapper,
                           RedisTemplate<String, Object> redisTemplate) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.productMapper = productMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, OrderDTO dto) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        Order order = new Order();
        order.setOrderNo("ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        order.setUserId(userId);
        order.setStatus(0);
        order.setTotalAmount(BigDecimal.ZERO);
        orderMapper.insert(order);

        for (OrderItemDTO itemDTO : dto.getItems()) {
            String lockKey = ORDER_STOCK_LOCK + itemDTO.getProductId();
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 30, TimeUnit.SECONDS);

            if (!locked) {
                throw new BusinessException("商品正在被抢购，请稍后再试");
            }

            try {
                Product product = productMapper.selectById(itemDTO.getProductId());
                if (product == null) {
                    throw new BusinessException("商品不存在");
                }
                if (product.getStock() < itemDTO.getQuantity()) {
                    throw new BusinessException("商品库存不足");
                }

                product.setStock(product.getStock() - itemDTO.getQuantity());
                productMapper.updateById(product);

                OrderItem item = new OrderItem();
                item.setOrderId(order.getId());
                item.setProductId(product.getId());
                item.setProductName(product.getName());
                item.setPrice(product.getPrice());
                item.setQuantity(itemDTO.getQuantity());
                orderItemMapper.insert(item);

                totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
            } finally {
                redisTemplate.delete(lockKey);
            }
        }

        order.setTotalAmount(totalAmount);
        orderMapper.updateById(order);

        return order;
    }

    @Override
    public Page<Order> listOrders(OrderQueryDTO query) {
        Page<Order> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (query.getOrderNo() != null && !query.getOrderNo().isEmpty()) {
            wrapper.eq(Order::getOrderNo, query.getOrderNo());
        }
        if (query.getStatus() != null) {
            wrapper.eq(Order::getStatus, query.getStatus());
        }
        if (query.getUserId() != null) {
            wrapper.eq(Order::getUserId, query.getUserId());
        }
        wrapper.orderByDesc(Order::getCreateTime);

        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrders() {
        log.info("执行定时任务：取消超时订单");
        LocalDateTime expireTime = LocalDateTime.now().minusMinutes(30);

        List<Order> expiredOrders = orderMapper.selectList(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, 0)
                .lt(Order::getCreateTime, expireTime)
        );

        for (Order order : expiredOrders) {
            order.setStatus(4);
            orderMapper.updateById(order);
            log.info("订单 {} 已自动取消", order.getOrderNo());
        }

        if (!expiredOrders.isEmpty()) {
            log.info("共取消 {} 个超时订单", expiredOrders.size());
        }
    }
}