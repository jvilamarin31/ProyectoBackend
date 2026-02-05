package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @JsonProperty("last_name")
    private String lastName;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @JsonProperty("date_birth")
    private LocalDate dateBirth;

    @NotBlank(message = "El numero de telefono es obligatorio")
    @Size(max = 15, message = "El numero de telefono no puede tener mas de 15 caracteres")
    @JsonProperty("mobile_phone")
    private String mobilePhone;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no cumple con el formato")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 120, message = "La contraseña debe ser menor a 120 digitos")
    private String password;

    @NotBlank(message = "La direccion es obligatoria")
    private String address;

}
