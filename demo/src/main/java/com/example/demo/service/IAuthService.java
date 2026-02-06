package com.example.demo.service;


import com.example.demo.dto.request.LoginUserRequest;
import com.example.demo.dto.response.LoginUserResponse;

public interface IAuthService {
    public LoginUserResponse login(LoginUserRequest userRequest);
}
