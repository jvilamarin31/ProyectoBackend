package com.example.demo.exception;

public class JwtInvalidTokenException extends RuntimeException{
    public JwtInvalidTokenException(String message) {
        super(message);
    }

    public JwtInvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
