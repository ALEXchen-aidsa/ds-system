package com.ds.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("stock_check")
public class StockCheck {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String checkNo;

    private Long warehouseId;

    private Integer status;

    private Long operatorId;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private LocalDateTime finishTime;
}