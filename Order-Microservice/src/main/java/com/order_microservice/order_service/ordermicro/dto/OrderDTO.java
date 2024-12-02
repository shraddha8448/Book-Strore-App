package com.order_microservice.order_service.ordermicro.dto;

import lombok.Data;

@Data
public class OrderDTO {

    private Long userId;
    private Long bookId;
    private Long qty;
    private String address;
    private Boolean cancel;
}
