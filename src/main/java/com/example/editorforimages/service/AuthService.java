package com.example.editorforimages.service;

import com.example.editorforimages.dto.JwtRequest;
import com.example.editorforimages.dto.JwtResponse;
import com.example.editorforimages.dto.RegistrationUserDto;
import com.example.editorforimages.dto.UserDto;
import com.example.editorforimages.entity.UserEntity;
import com.example.editorforimages.exceptions.AppError;
import com.example.editorforimages.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody final JwtRequest authRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword());
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(),
                    "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        Long id = userService.findByUserName(authRequest.getUsername()).getId();
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(id, token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody final RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.findByUserName(registrationUserDto.getUsername()) != null) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Пользователь с указанным именем уже существует"),
                    HttpStatus.BAD_REQUEST);
        }
        UserEntity user = userService.createUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getName()));
    }
}
