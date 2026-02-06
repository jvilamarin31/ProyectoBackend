package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserResponse {

    private UserSessionResponse user;

    @JsonProperty("acces_token")
    private String accesToken;

    @JsonProperty("token_type")
    private String tokenType;
}
