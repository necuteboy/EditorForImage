package com.example.editorforimages;

import com.example.editorforimages.config.BaseTest;
import com.example.editorforimages.dto.JwtRequest;
import com.example.editorforimages.dto.RegistrationUserDto;
import com.example.editorforimages.repository.UserRepository;
import com.example.editorforimages.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class AuthServiceTest extends BaseTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String CONFIRM = "password";

    @BeforeEach
    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void wrongConfirmToRegister() {
        assertEquals(HttpStatus.BAD_REQUEST,
                authService.createNewUser(new RegistrationUserDto(USERNAME, "anotherpass",CONFIRM)).getStatusCode());
    }

    @Test
    public void wrongLogin() {
        authService.createNewUser(new RegistrationUserDto(USERNAME, PASSWORD,CONFIRM));
        assertEquals(HttpStatus.UNAUTHORIZED,
                authService.createAuthToken(new JwtRequest(USERNAME, "anotherpass")).getStatusCode());
    }

}
