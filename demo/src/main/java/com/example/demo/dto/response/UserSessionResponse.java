package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserSessionResponse {
    private String id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("session_active")
    private Boolean sessionActive;

    @JsonProperty("date_birth")
    private LocalDate dateBirth;
    private String email;

    @JsonProperty("mobile_phone")
    private String mobilePhone;
    private String password;
    private String address;


}
