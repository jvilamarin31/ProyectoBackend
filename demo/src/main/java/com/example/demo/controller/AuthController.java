package com.example.demo.controller;

import com.example.demo.dto.request.LoginUserRequest;
import com.example.demo.dto.response.LoginUserResponse;
import com.example.demo.service.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }


    @PostMapping("api/v1/users/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest userRequest) {
        return ResponseEntity.ok(authService.login(userRequest));
    }
}
