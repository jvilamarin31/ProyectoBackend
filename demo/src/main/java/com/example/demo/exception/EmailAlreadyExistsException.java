package com.example.demo.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super("El email ya está registrado: " + email);
    }
}
