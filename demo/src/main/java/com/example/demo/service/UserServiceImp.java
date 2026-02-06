package com.example.demo.service;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.GetUserResponse;
import com.example.demo.dto.response.UserBasicResponse;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.MobilePhoneAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.UserModel;
import com.example.demo.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements IUserService{

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserBasicResponse createUser (CreateUserRequest userRequest){
        Optional <UserModel> usuarioEmailExiste = userRepository.findByEmail(userRequest.getEmail());
        if (usuarioEmailExiste.isPresent()) {
            throw new EmailAlreadyExistsException(userRequest.getEmail());
        }
        Optional <UserModel> usuarioTelefonoExiste = userRepository.findByMobilePhone(userRequest.getMobilePhone());
        if (usuarioTelefonoExiste.isPresent()) {
            throw new MobilePhoneAlreadyExistsException(userRequest.getMobilePhone());
        }

        UserModel userFinal = UserMapper.toModel(userRequest, passwordEncoder);
        UserModel userResponse = userRepository.save(userFinal);

        return UserMapper.toBasicResponse(userResponse);
    }

    @Override
    public UserBasicResponse updateUser(UpdateUserRequest userRequest, String idUser){

        Optional<UserModel> usuarioExiste = userRepository.findById(idUser);
        if (!usuarioExiste.isPresent()) {
            throw new UserNotFoundException(idUser);
        }
        UserModel usuarioFinal = usuarioExiste.get();
        if (userRequest.getFirstName() != null && !userRequest.getFirstName().isBlank() ) {
            usuarioFinal.setFirstName(userRequest.getFirstName());
        }
        if (userRequest.getLastName() != null && !userRequest.getLastName().isBlank() ) {
            usuarioFinal.setLastName(userRequest.getLastName());
        }
        if (userRequest.getDateBirth() != null) {
            usuarioFinal.setDateBirth(userRequest.getDateBirth());
        }
        if (userRequest.getMobilePhone() != null && !userRequest.getMobilePhone().isBlank() && !userRequest.getMobilePhone().equals(usuarioFinal.getMobilePhone())) {
            Optional<UserModel> usuarioTelefonoExiste = userRepository.findByMobilePhone(userRequest.getMobilePhone());
            if (usuarioTelefonoExiste.isPresent()) {
                throw new MobilePhoneAlreadyExistsException(userRequest.getMobilePhone());
            }
            usuarioFinal.setMobilePhone(userRequest.getMobilePhone());
        }
        if (userRequest.getEmail() != null && !userRequest.getEmail().isBlank() && !userRequest.getEmail().equals(usuarioFinal.getEmail())) {
            Optional<UserModel> usuarioEmailExiste = userRepository.findByEmail(userRequest.getEmail());
            if (usuarioEmailExiste.isPresent()) {
                throw new EmailAlreadyExistsException(userRequest.getEmail());
            }
            usuarioFinal.setEmail(userRequest.getEmail());
        }
        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank() ) {
            usuarioFinal.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        if (userRequest.getAddress() != null && !userRequest.getAddress().isBlank() ) {
            usuarioFinal.setAddress(userRequest.getAddress());
        }

        UserModel userSave = userRepository.save(usuarioFinal);

        return UserMapper.toBasicResponse(userSave);
    }

    @Override
    public UserBasicResponse deleteUser (String idUser){
        Optional <UserModel> usuarioExiste = userRepository.findById(idUser);
        if (!usuarioExiste.isPresent()) {
            throw new UserNotFoundException(idUser);
        }
        UserModel usuarioFinal = usuarioExiste.get();
        userRepository.delete(usuarioFinal);
        return UserMapper.toBasicResponse(usuarioFinal);
    }
    @Override
    public GetUserResponse getUser (String idUser){
        Optional<UserModel> usuarioExiste = userRepository.findById(idUser);
        if (!usuarioExiste.isPresent()) {
            throw new UserNotFoundException(idUser);
        }
        UserModel usuarioFinal = usuarioExiste.get();
        return UserMapper.toGetUserResponse(usuarioFinal);
    }

    @Override
    public List<GetUserResponse> getUsers() {
        List<UserModel> users = userRepository.findAll();
        List<GetUserResponse> responses = new ArrayList<>();
        for (UserModel user : users) {
            responses.add(UserMapper.toGetUserResponse(user));
        }
        return responses;
    }
}
