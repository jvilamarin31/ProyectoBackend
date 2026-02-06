package com.example.demo.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(){
        super("Credenciales inválidas");
    }
}
