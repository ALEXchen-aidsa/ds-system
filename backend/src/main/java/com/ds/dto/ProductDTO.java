package com.ds.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotBlank(message = "商品编码不能为空")
    private String code;

    private Long categoryId;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    private String description;

    private String image;

    private Integer status;
}