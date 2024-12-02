package com.book_microservice.book_service.bookmicro.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String msg){
        super(msg);
    }
}
