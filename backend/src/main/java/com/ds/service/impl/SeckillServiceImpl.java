package com.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.common.RedisRateLimiter;
import com.ds.entity.SeckillProduct;
import com.ds.exception.BusinessException;
import com.ds.mapper.SeckillProductMapper;
import com.ds.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillServiceImpl implements SeckillService {

    private static final Logger log = LoggerFactory.getLogger(SeckillServiceImpl.class);

    private final SeckillProductMapper seckillProductMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisRateLimiter rateLimiter;

    private static final String SECKILL_STOCK_KEY = "seckill:stock:";
    private static final String SECKILL_BUY_KEY = "seckill:buy:";
    private static final String SECKILL_LOCK_KEY = "seckill:lock:";

    public SeckillServiceImpl(SeckillProductMapper seckillProductMapper,
                             RedisTemplate<String, Object> redisTemplate,
                             RedisRateLimiter rateLimiter) {
        this.seckillProductMapper = seckillProductMapper;
        this.redisTemplate = redisTemplate;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public Page<SeckillProduct> listSeckillProducts(Integer page, Integer size) {
        Page<SeckillProduct> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SeckillProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SeckillProduct::getCreateTime);
        return seckillProductMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public SeckillProduct getSeckillProductById(Long id) {
        return seckillProductMapper.selectById(id);
    }

    @Override
    @Transactional
    public Map<String, Object> doSeckill(Long userId, Long seckillProductId) {
        String rateLimitKey = "seckill:" + seckillProductId;
        if (!rateLimiter.isAllowed(rateLimitKey)) {
            throw new BusinessException("请求太频繁，请稍后再试");
        }

        String buyKey = SECKILL_BUY_KEY + seckillProductId + ":" + userId;
        Boolean hasBought = redisTemplate.hasKey(buyKey);
        if (Boolean.TRUE.equals(hasBought)) {
            throw new BusinessException("您已经抢购过了");
        }

        SeckillProduct seckillProduct = seckillProductMapper.selectById(seckillProductId);
        if (seckillProduct == null) {
            throw new BusinessException("秒杀商品不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(seckillProduct.getStartTime())) {
            throw new BusinessException("秒杀尚未开始");
        }
        if (now.isAfter(seckillProduct.getEndTime())) {
            throw new BusinessException("秒杀已结束");
        }

        String stockKey = SECKILL_STOCK_KEY + seckillProductId;
        Object stockObj = redisTemplate.opsForValue().get(stockKey);

        if (stockObj == null) {
            redisTemplate.opsForValue().set(stockKey, seckillProduct.getSeckillStock());
            stockObj = redisTemplate.opsForValue().get(stockKey);
        }

        Long stock = (Long) stockObj;
        if (stock <= 0) {
            throw new BusinessException("已售罄");
        }

        String lockKey = SECKILL_LOCK_KEY + seckillProductId;
        String lockValue = UUID.randomUUID().toString();
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);

        try {
            stock = redisTemplate.opsForValue().decrement(stockKey);
            if (stock < 0) {
                redisTemplate.opsForValue().increment(stockKey);
                throw new BusinessException("已售罄");
            }

            redisTemplate.opsForValue().set(buyKey, "1", 24, TimeUnit.HOURS);

            seckillProduct.setSeckillStock(seckillProduct.getSeckillStock() - 1);
            seckillProductMapper.updateById(seckillProduct);

            log.info("用户{}秒杀商品{}成功", userId, seckillProductId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "秒杀成功");
            return result;
        } finally {
            if (Boolean.TRUE.equals(locked)) {
                String currentValue = (String) redisTemplate.opsForValue().get(lockKey);
                if (lockValue.equals(currentValue)) {
                    redisTemplate.delete(lockKey);
                }
            }
        }
    }

    @Override
    public void createSeckillProduct(SeckillProduct seckillProduct) {
        seckillProduct.setStatus(0);
        seckillProductMapper.insert(seckillProduct);
    }
}