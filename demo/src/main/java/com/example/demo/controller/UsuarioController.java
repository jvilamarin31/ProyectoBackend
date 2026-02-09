package com.example.demo.controller;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.GetUserResponse;
import com.example.demo.dto.response.UserBasicResponse;
import com.example.demo.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsuarioController {

    private final IUserService userService;

    public UsuarioController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("api/v1/users")
    public ResponseEntity<UserBasicResponse> createUser(@Valid @RequestBody CreateUserRequest userRequest) {
        return new ResponseEntity<UserBasicResponse>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PutMapping("api/v1/users/{id_user}")
    public ResponseEntity<UserBasicResponse> updateUser(@Valid @RequestBody UpdateUserRequest userRequest, @PathVariable("id_user") String idUser) {
        return new ResponseEntity<UserBasicResponse>(userService.updateUser(userRequest, idUser), HttpStatus.OK);
    }

    @DeleteMapping("api/v1/users/{id_user}")
    public ResponseEntity<UserBasicResponse> deleteUser(@PathVariable("id_user") String idUser) {
        return new ResponseEntity<UserBasicResponse>(userService.deleteUser(idUser), HttpStatus.OK);
    }

    @GetMapping("api/v1/users/{id_user}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("id_user") String idUser) {
        return new ResponseEntity<GetUserResponse>(userService.getUser(idUser), HttpStatus.OK);
    }

    @GetMapping("api/v1/users")
    public ResponseEntity<List<GetUserResponse>> getUsers() {
        return new ResponseEntity<List<GetUserResponse>>(userService.getUsers(), HttpStatus.OK);
    }

}
