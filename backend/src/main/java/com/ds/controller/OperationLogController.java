package com.ds.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.common.Result;
import com.ds.entity.OperationLog;
import com.ds.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "操作日志", description = "操作日志接口")
@RestController
@RequestMapping("/api/log")
public class OperationLogController {

    private final OperationLogService logService;

    public OperationLogController(OperationLogService logService) {
        this.logService = logService;
    }

    @Operation(summary = "获取操作日志列表")
    @GetMapping("/list")
    public Result<Page<OperationLog>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(logService.listLogs(page, size));
    }
}