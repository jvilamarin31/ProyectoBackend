package com.example.demo.service;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.GetUserResponse;
import com.example.demo.dto.response.UserBasicResponse;

import java.util.List;

public interface IUserService {
    public UserBasicResponse createUser (CreateUserRequest user);
    public UserBasicResponse updateUser(UpdateUserRequest user, String idUser);
    public UserBasicResponse deleteUser (String idUser);
    public GetUserResponse getUser (String idUser);
    public List<GetUserResponse> getUsers ();
}
