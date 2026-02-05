package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @JsonProperty("date_birth")
    private LocalDate dateBirth;

    @Size(max = 15, message = "El numero de telefono no puede tener mas de 15 caracteres")
    @JsonProperty("mobile_phone")
    private String mobilePhone;

    @Email(message = "El email no cumple con el formato")
    private String email;

    @Size(max = 120, message = "La contraseña debe ser menor a 120 digitos")
    private String password;

    private String address;
}

