package com.azael.taskapp.exceptions;

public class ServiceLogicException extends RuntimeException {
    public ServiceLogicException(String message) {
        //super("Something went wrong. Please try again later!");
        super(message);
    }
}