package com.example.demo.exception;

public class MobilePhoneAlreadyExistsException extends RuntimeException {
    public MobilePhoneAlreadyExistsException(String mobilePhone) {
        super("El número de teléfono móvil ya está registrado: " + mobilePhone);
    }
}
