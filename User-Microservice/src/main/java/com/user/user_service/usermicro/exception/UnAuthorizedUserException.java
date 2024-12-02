package com.user.user_service.usermicro.exception;

public class UnAuthorizedUserException extends RuntimeException {

    public UnAuthorizedUserException(String message) {

      super(message);
    }
}
