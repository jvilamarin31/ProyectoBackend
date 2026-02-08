package com.example.demo;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.GetUserResponse;
import com.example.demo.dto.response.UserBasicResponse;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.MobilePhoneAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.UserModel;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.UserServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImpTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImp userService;

    // createUser

    @Test
    void createUser_shouldThrowEmailAlreadyExists_whenEmailExists() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setEmail("a@test.com");
        userRequest.setMobilePhone("3000000000");
        userRequest.setPassword("1234");

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(new UserModel()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userRequest));

        verify(userRepository).findByEmail("a@test.com");
        verify(userRepository, never()).findByMobilePhone(any());
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void createUser_shouldThrowMobilePhoneAlreadyExists_whenMobileExists() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setEmail("a@test.com");
        userRequest.setMobilePhone("3000000000");
        userRequest.setPassword("1234");

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByMobilePhone(userRequest.getMobilePhone())).thenReturn(Optional.of(new UserModel()));

        assertThrows(MobilePhoneAlreadyExistsException.class, () -> userService.createUser(userRequest));

        verify(userRepository).findByEmail("a@test.com");
        verify(userRepository).findByMobilePhone("3000000000");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void createUser_shouldSaveAndReturnResponse_whenDataIsValid() {

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setFirstName("Admin");
        userRequest.setLastName("System");
        userRequest.setAddress("N/A");
        userRequest.setDateBirth(LocalDate.of(2000, 1, 1));
        userRequest.setEmail("admin@test.com");
        userRequest.setMobilePhone("3000000000");
        userRequest.setPassword("Admin123");

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByMobilePhone(userRequest.getMobilePhone())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("HASHED_PASS");

        when(userRepository.save(any(UserModel.class))).thenAnswer(inv -> {
            UserModel user = inv.getArgument(0);
            user.setId("123");
            return user;
        });

        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);

        UserBasicResponse userResponse = userService.createUser(userRequest);

        assertNotNull(userResponse);
        verify(userRepository).save(captor.capture());

        UserModel saved = captor.getValue();
        assertEquals("admin@test.com", saved.getEmail());
        assertEquals("3000000000", saved.getMobilePhone());
        assertEquals("HASHED_PASS", saved.getPassword());
        verify(passwordEncoder).encode("Admin123");
    }

    // updateUser

    @Test
    void updateUser_shouldThrowUserNotFound_whenIdDoesNotExist() {
        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setFirstName("Juanito");

        when(userRepository.findById("no existe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userRequest, "no existe"));

        verify(userRepository).findById("no existe");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateUser_shouldThrowMobilePhoneAlreadyExists_whenChangingMobileToExistingOne() {
        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setMobilePhone("3111111111");

        UserModel userDB = new UserModel();
        userDB.setId("123");
        userDB.setMobilePhone("3000000000"); // actual
        userDB.setEmail("admin@test.com");

        when(userRepository.findById("123")).thenReturn(Optional.of(userDB));
        when(userRepository.findByMobilePhone("3111111111")).thenReturn(Optional.of(new UserModel()));

        assertThrows(MobilePhoneAlreadyExistsException.class, () -> userService.updateUser(userRequest, "123"));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldThrowEmailAlreadyExists_whenChangingEmailToExistingOne() {
        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setEmail("nuevo@test.com");

        UserModel userDB = new UserModel();
        userDB.setId("123");
        userDB.setEmail("admin@test.com");
        userDB.setMobilePhone("3000000000");

        when(userRepository.findById("123")).thenReturn(Optional.of(userDB));
        when(userRepository.findByEmail("nuevo@test.com")).thenReturn(Optional.of(new UserModel()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUser(userRequest, "123"));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldUpdateFieldsAndSave_whenValid() {
        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setFirstName("Juan");
        userRequest.setPassword("NuevaContra123");
        userRequest.setAddress("Cali");

        UserModel userDB = new UserModel();
        userDB.setId("123");
        userDB.setFirstName("Admin");
        userDB.setPassword("viejoHash");
        userDB.setAddress("N/A");
        userDB.setEmail("admin@test.com");
        userDB.setMobilePhone("3000000000");

        when(userRepository.findById("123")).thenReturn(Optional.of(userDB));
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("nuevoHash");

        when(userRepository.save(any(UserModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);

        UserBasicResponse resp = userService.updateUser(userRequest, "123");

        assertNotNull(resp);
        verify(userRepository).save(captor.capture());

        UserModel saved = captor.getValue();
        assertEquals("Juan", saved.getFirstName());
        assertEquals("Cali", saved.getAddress());
        assertEquals("nuevoHash", saved.getPassword());

        verify(passwordEncoder).encode("NuevaContra123");
    }

    // deleteUser

    @Test
    void deleteUser_shouldThrowUserNotFound_whenIdDoesNotExist() {
        when(userRepository.findById("no existe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser("no existe"));

        verify(userRepository).findById("no existe");
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUser_shouldDeleteAndReturnResponse_whenUserExists() {
        UserModel userDB = new UserModel();
        userDB.setId("123");

        when(userRepository.findById("123")).thenReturn(Optional.of(userDB));

        UserBasicResponse userResponse = userService.deleteUser("123");

        assertNotNull(userResponse);
        verify(userRepository).delete(userDB);
    }

    // getUser

    @Test
    void getUser_shouldThrowUserNotFound_whenIdDoesNotExist() {
        when(userRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser("no-existe"));

        verify(userRepository).findById("no-existe");
    }

    @Test
    void getUser_shouldReturnResponse_whenUserExists() {
        UserModel userDB = new UserModel();
        userDB.setId("123");

        when(userRepository.findById("123")).thenReturn(Optional.of(userDB));

        GetUserResponse userResponse = userService.getUser("123");

        assertNotNull(userResponse);
        verify(userRepository).findById("123");
    }

    // getUsers

    @Test
    void getUsers_shouldReturnListMappedFromRepository() {
        UserModel user1 = new UserModel(); user1.setId("1");
        UserModel user2 = new UserModel(); user2.setId("2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<GetUserResponse> resp = userService.getUsers();

        assertNotNull(resp);
        assertEquals(2, resp.size());
        verify(userRepository).findAll();
    }
}
