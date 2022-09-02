package com.example.shoppingmall.exception;

public class MemberPhoneAlreadyExistsException extends RuntimeException{
    public MemberPhoneAlreadyExistsException(String message) {
        super(message);
    }
}
