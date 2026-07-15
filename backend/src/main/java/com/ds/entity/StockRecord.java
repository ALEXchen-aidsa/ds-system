package com.ds.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("stock_record")
public class StockRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String recordNo;

    private Integer type;

    private Long warehouseId;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    private String remark;

    private Integer status;

    private Long operatorId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}