package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonPropertyOrder({"first_name", "last_name", "date_birth", "mobile_phone", "email", "password", "address"})
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
