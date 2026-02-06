package com.example.demo.mapper;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.response.GetUserResponse;
import com.example.demo.dto.response.LoginUserResponse;
import com.example.demo.dto.response.UserBasicResponse;
import com.example.demo.dto.response.UserSessionResponse;
import com.example.demo.model.UserModel;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static UserModel toModel(CreateUserRequest req, PasswordEncoder encoder) {
        return UserModel.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .dateBirth(req.getDateBirth())
                .address(req.getAddress())
                .email(req.getEmail())
                .mobilePhone(req.getMobilePhone())
                .password(encoder.encode(req.getPassword()))
                .token(null)
                .build();
    }

    public static UserBasicResponse toBasicResponse(UserModel model) {
        return UserBasicResponse.builder()
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .dateBirth(model.getDateBirth())
                .email(model.getEmail())
                .mobilePhone(model.getMobilePhone())
                .password(model.getPassword())
                .address(model.getAddress())
                .build();
    }

    public static GetUserResponse toGetUserResponse(UserModel model) {
        return GetUserResponse.builder()
                .id(model.getId())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .dateBirth(model.getDateBirth())
                .mobilePhone(model.getMobilePhone())
                .email(model.getEmail())
                .address(model.getAddress())
                .sessionActive(model.getToken() != null)
                .build();
    }

    public static UserSessionResponse toSessionResponse(UserModel model, Boolean active) {
        return UserSessionResponse.builder()
                .id(model.getId())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .sessionActive(active)
                .dateBirth(model.getDateBirth())
                .email(model.getEmail())
                .mobilePhone(model.getMobilePhone())
                .password(model.getPassword())
                .address(model.getAddress())
                .build();
    }

    public static LoginUserResponse toLoginUserResponse(UserModel model, String token){
        return LoginUserResponse.builder()
                .accesToken(token)
                .tokenType("Bearer")
                .user(UserMapper.toSessionResponse(model, true))
                .build();
    }


}
