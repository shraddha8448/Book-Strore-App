package com.cart_microservice.cart_service.cartmicro.exception;

public class NoSuchCartException extends RuntimeException{
    public NoSuchCartException(String msg){
        super(msg);
    }
}
