package com.example.demo.exception;

public class MobilePhoneNotFoundException extends RuntimeException{
    public MobilePhoneNotFoundException(String mobilePhone){
        super("El número de telefono ingresado: " + mobilePhone + " no ha sido encontrado.");
    }
}
