package com.example.demo;

import com.example.demo.dto.request.LoginUserRequest;
import com.example.demo.dto.response.LoginUserResponse;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.exception.MobilePhoneNotFoundException;
import com.example.demo.model.UserModel;
import com.example.demo.repository.IUserRepository;
import com.example.demo.security.JwtService;
import com.example.demo.service.AuthServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImpTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImp authService;

    @Test
    void login_shouldThrowMobilePhoneNotFound_whenUserDoesNotExist(){

        LoginUserRequest userRequest = new LoginUserRequest();
        userRequest.setMobilePhone("3000000000");
        userRequest.setPassword("Admin123");

        when(userRepository.findByMobilePhone(userRequest.getMobilePhone())).thenReturn(Optional.empty());

        assertThrows(MobilePhoneNotFoundException.class, () -> authService.login(userRequest));

        verify(userRepository).findByMobilePhone("3000000000");
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);

    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenPasswordIsIncorrect(){

        LoginUserRequest userRequest = new LoginUserRequest();
        userRequest.setMobilePhone("3000000000");
        userRequest.setPassword("Admin123");

        UserModel user = new UserModel();
        user.setId("123");
        user.setMobilePhone(userRequest.getMobilePhone());
        user.setPassword("HolaMundo123");

        when(userRepository.findByMobilePhone(userRequest.getMobilePhone())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(userRequest));

        verifyNoInteractions(jwtService);
        verify(userRepository, never()).save(any());

    }

    @Test
    void login_shouldReturnLoginUserResponse_whenCredentialsAreValid(){

        LoginUserRequest userRequest = new LoginUserRequest();
        userRequest.setMobilePhone("3000000000");
        userRequest.setPassword("Admin123");

        UserModel user = new UserModel();
        user.setId("123");
        user.setMobilePhone(userRequest.getMobilePhone());
        user.setPassword("HashedAdmin123");

        when(userRepository.findByMobilePhone(userRequest.getMobilePhone())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getId())).thenReturn("token123");

        LoginUserResponse usuarioResponse = authService.login(userRequest);

        assertNotNull(usuarioResponse);
        assertEquals("token123", usuarioResponse.getAccesToken());

        verify(userRepository).save(user);
        verify(jwtService).generateToken(user.getId());
        verify(passwordEncoder).matches(userRequest.getPassword(), user.getPassword());
    }


}
