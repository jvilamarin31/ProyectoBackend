package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginUserRequest {
    @NotNull(message = "El numero de telefono es obligatorio")
    @JsonProperty("mobile_phone")
    private String mobilePhone;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
