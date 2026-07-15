package com.ds.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.entity.OperationLog;

public interface OperationLogService {
    void saveLog(OperationLog log);
    Page<OperationLog> listLogs(Integer page, Integer size);
}