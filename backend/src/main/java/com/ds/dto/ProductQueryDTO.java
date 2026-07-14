package com.ds.dto;

import lombok.Data;

@Data
public class ProductQueryDTO {

    private String name;

    private String code;

    private Long categoryId;

    private Integer status;

    private Integer page = 1;

    private Integer size = 10;
}