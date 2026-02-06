package com.example.demo.service;

import com.example.demo.dto.request.LoginUserRequest;
import com.example.demo.dto.response.LoginUserResponse;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.exception.MobilePhoneNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.UserModel;
import com.example.demo.repository.IUserRepository;
import com.example.demo.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthServiceImp implements IAuthService{

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImp(IUserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public LoginUserResponse login(LoginUserRequest userRequest){
        Optional<UserModel> usuarioExiste = userRepository.findByMobilePhone(userRequest.getMobilePhone());
        if (!usuarioExiste.isPresent()){
            throw new MobilePhoneNotFoundException(userRequest.getMobilePhone());
        }

        UserModel userFinal = usuarioExiste.get();

        if (!passwordEncoder.matches(userRequest.getPassword(), userFinal.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(userFinal.getId());

        userFinal.setToken(token);
        userRepository.save(userFinal);
        return UserMapper.toLoginUserResponse(userFinal,token);
    }

}
