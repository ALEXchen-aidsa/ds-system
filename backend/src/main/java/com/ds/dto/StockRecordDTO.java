package com.ds.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockRecordDTO {

    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @NotNull(message = "数量不能为空")
    private Integer quantity;

    private String remark;
}