package com.ds.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("stock_check_item")
public class StockCheckItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long checkId;

    private Long productId;

    private Integer systemQuantity;

    private Integer actualQuantity;

    private Integer difference;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}