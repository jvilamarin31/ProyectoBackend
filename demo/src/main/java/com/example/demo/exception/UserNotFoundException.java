package com.example.demo.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("El usuario con id: " + id + "no ha sido encontrado.");
    }
}
