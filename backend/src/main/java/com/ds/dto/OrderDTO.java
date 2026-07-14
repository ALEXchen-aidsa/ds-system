package com.ds.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {

    @NotNull(message = "商品列表不能为空")
    private List<OrderItemDTO> items;
}