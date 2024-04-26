package com.example.editorforimages;

import com.example.editorforimages.config.BaseTest;
import com.example.editorforimages.dto.JwtRequest;
import com.example.editorforimages.dto.JwtResponse;
import com.example.editorforimages.dto.RegistrationUserDto;
import com.example.editorforimages.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAuthController extends BaseTest {

    private final RestClient restClient = RestClient.create();

    @Autowired
    private UserRepository userRepository;

    private static final String REGISTER_URI = "http://localhost:8080/api/v1/auth/register";
    private static final String LOGIN_URI = "http://localhost:8080/api/v1/auth/login";
    private static final String USER_URI = "http://localhost:8080/api/v1/auth/user";

    private static final String USERNAME = "user123";
    private static final String PASSWORD = "qwerty";
    private static final String CONFIRM = "qwerty";

    @BeforeEach
    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void registerNewUser() {
        // Register
        var responseEntity = restClient.post()
                .uri(REGISTER_URI)
                .body(new RegistrationUserDto(USERNAME, PASSWORD, CONFIRM))
                .retrieve()
                .toBodilessEntity();
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void loginUser() {
        // Register
        restClient.post()
                .uri(REGISTER_URI)
                .body(new RegistrationUserDto(USERNAME, PASSWORD, CONFIRM))
                .retrieve()
                .toBodilessEntity();

        // Login
        var responseEntity = restClient.post()
                .uri(LOGIN_URI)
                .body(new JwtRequest(USERNAME, PASSWORD))
                .retrieve()
                .toEntity(JwtResponse.class);
        assertEquals(200, responseEntity.getStatusCode().value());
    }

}
