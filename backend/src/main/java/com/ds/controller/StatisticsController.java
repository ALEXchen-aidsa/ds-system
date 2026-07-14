package com.ds.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ds.common.Result;
import com.ds.entity.Product;
import com.ds.entity.SysUser;
import com.ds.mapper.ProductMapper;
import com.ds.mapper.SysUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "统计管理", description = "数据统计接口")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final ProductMapper productMapper;
    private final SysUserMapper userMapper;

    public StatisticsController(ProductMapper productMapper, SysUserMapper userMapper) {
        this.productMapper = productMapper;
        this.userMapper = userMapper;
    }

    @Operation(summary = "获取统计数据")
    @GetMapping
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalProduct", productMapper.selectCount(null));
        statistics.put("totalUser", userMapper.selectCount(null));
        statistics.put("totalOrder", 0);
        statistics.put("todaySales", 0);
        return Result.success(statistics);
    }
}