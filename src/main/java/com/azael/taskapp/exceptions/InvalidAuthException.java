package com.azael.taskapp.exceptions;

public class InvalidAuthException extends RuntimeException {

  public InvalidAuthException(String message) {
    super(message);
  }
}