package com.ds.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.entity.SeckillProduct;

import java.util.Map;

public interface SeckillService {

    Page<SeckillProduct> listSeckillProducts(Integer page, Integer size);

    SeckillProduct getSeckillProductById(Long id);

    Map<String, Object> doSeckill(Long userId, Long seckillProductId);

    void createSeckillProduct(SeckillProduct seckillProduct);
}