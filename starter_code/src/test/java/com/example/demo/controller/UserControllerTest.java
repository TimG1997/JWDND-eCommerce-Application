package com.example.demo.controller;

import com.example.demo.controller.requests.CreateUserRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final long ID = 1L;
    private static final String PASSWORD_WITH_LENGTH_SIX = "abcdef";
    private static final String WRONG_PASSWORD_CONFIRMATION = "wrong-password-confirmation";

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    public void beforeEach(){
        user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
    }

    @Test
    public void verify_that_a_user_can_be_retrieved_by_id(){
        when(this.userRepository.findById(ID)).thenReturn(Optional.ofNullable(user));

        ResponseEntity<User> userResponseEntity = this.userController.findById(ID);

        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
        assertEquals(user, userResponseEntity.getBody());
    }

    @Test
    public void verify_that_http_status_is_not_found_when_username_doesnt_exist(){
        when(this.userRepository.findByUsername(USERNAME)).thenReturn(null);

        ResponseEntity<User> userResponseEntity = this.userController.findByUserName(USERNAME);

        assertEquals(HttpStatus.NOT_FOUND, userResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_a_user_can_be_retrieved_by_username(){
        when(this.userRepository.findByUsername(USERNAME)).thenReturn(user);

        ResponseEntity<User> userResponseEntity = this.userController.findByUserName(USERNAME);

        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
        assertEquals(user, userResponseEntity.getBody());
    }

    @Test
    public void verify_that_a_user_can_not_be_created_with_a_password_length_below_seven(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setPassword(PASSWORD_WITH_LENGTH_SIX);

        ResponseEntity<User> userResponseEntity = this.userController.createUser(createUserRequest);

        assertEquals(HttpStatus.BAD_REQUEST, userResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_a_user_can_not_be_created_when_passwords_are_not_matching(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(WRONG_PASSWORD_CONFIRMATION);

        ResponseEntity<User> userResponseEntity = this.userController.createUser(createUserRequest);

        assertEquals(HttpStatus.BAD_REQUEST, userResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_a_user_can_be_created_when_a_create_user_request_meets_requirements(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USERNAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);

        ResponseEntity<User> userResponseEntity = this.userController.createUser(createUserRequest);

        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
        assertEquals(USERNAME, userResponseEntity.getBody().getUsername());
    }
}
