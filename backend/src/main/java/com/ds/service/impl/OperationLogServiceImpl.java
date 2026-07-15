package com.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.entity.OperationLog;
import com.ds.mapper.OperationLogMapper;
import com.ds.service.OperationLogService;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogMapper logMapper;

    public OperationLogServiceImpl(OperationLogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public void saveLog(OperationLog log) {
        logMapper.insert(log);
    }

    @Override
    public Page<OperationLog> listLogs(Integer page, Integer size) {
        Page<OperationLog> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(OperationLog::getCreateTime);
        return logMapper.selectPage(pageParam, wrapper);
    }
}