package com.ds.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.common.Result;
import com.ds.entity.SeckillProduct;
import com.ds.service.SeckillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "秒杀管理", description = "秒杀相关接口")
@RestController
@RequestMapping("/api/seckill")
public class SeckillController {

    private final SeckillService seckillService;

    public SeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    @Operation(summary = "获取秒杀商品列表")
    @GetMapping("/list")
    public Result<Page<SeckillProduct>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(seckillService.listSeckillProducts(page, size));
    }

    @Operation(summary = "获取秒杀商品详情")
    @GetMapping("/{id}")
    public Result<SeckillProduct> getById(@PathVariable Long id) {
        return Result.success(seckillService.getSeckillProductById(id));
    }

    @Operation(summary = "参与秒杀")
    @PostMapping("/do/{seckillProductId}")
    public Result<Map<String, Object>> doSeckill(@PathVariable Long seckillProductId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getCredentials();
        return Result.success(seckillService.doSeckill(userId, seckillProductId));
    }

    @Operation(summary = "创建秒杀商品")
    @PostMapping
    public Result<Void> create(@RequestBody SeckillProduct seckillProduct) {
        seckillService.createSeckillProduct(seckillProduct);
        return Result.success();
    }
}