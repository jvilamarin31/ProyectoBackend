package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex
    ) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(MobilePhoneAlreadyExistsException.class)
    public ResponseEntity<String> handleMobilePhoneAlreadyExistsException(
            MobilePhoneAlreadyExistsException ex
    ) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(
            UserNotFoundException ex
    ) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // 404
    }

    // Errores de validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Error de validación");

        return new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST); // 400
    }

    // JSON mal formado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidFormat(
            HttpMessageNotReadableException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Formato inválido en la solicitud");
    }

    // Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(
            Exception ex
    ) {
        return new ResponseEntity<>(
                "Ocurrió un error inesperado",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

