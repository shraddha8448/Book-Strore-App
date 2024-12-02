package com.order_microservice.order_service.ordermicro.exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(String msg){
        super(msg);
    }
}
