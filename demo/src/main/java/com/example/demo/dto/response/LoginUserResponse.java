package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"user", "acces_token", "token_type"})
public class LoginUserResponse {

    private UserSessionResponse user;

    @JsonProperty("acces_token")
    private String accesToken;

    @JsonProperty("token_type")
    private String tokenType;
}
