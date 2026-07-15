package com.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ds.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}