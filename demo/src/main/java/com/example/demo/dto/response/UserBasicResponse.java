package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserBasicResponse {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("date_birth")
    private LocalDate dateBirth;

    @JsonProperty("mobile_phone")
    private String mobilePhone;
    private String email;
    private String password;
    private String address;

}
