package com.example.mock2.common.exception;

public class CommonLogicException extends RuntimeException{
    public CommonLogicException(String usernameAlreadyExists) {
        super(usernameAlreadyExists);
    }
}
