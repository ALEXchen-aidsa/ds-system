package com.ds.dto;

import lombok.Data;

@Data
public class OrderQueryDTO {

    private String orderNo;

    private Integer status;

    private Long userId;

    private Integer page = 1;

    private Integer size = 10;
}