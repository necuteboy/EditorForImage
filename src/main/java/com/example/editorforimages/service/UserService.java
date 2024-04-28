package com.example.editorforimages.service;

import com.example.editorforimages.config.PasswordEncoderConfiguration;
import com.example.editorforimages.dto.RegistrationUserDto;
import com.example.editorforimages.entity.UserEntity;
import com.example.editorforimages.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoderConfiguration passwordEncoder;
    private final RoleService roleService;

    public UserEntity findByUserName(final String name) {
        return userRepository.findByName(name);
    }

    public UserEntity saveUser(final String name, final int age) {
        var user = UserEntity.builder()
                .name(name)
                .build();
        return userRepository.save(user);
    }

    public UserEntity getUserById(final Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public UserEntity createUser(final RegistrationUserDto registrationUserDto) {
        UserEntity user = new UserEntity();
        user.setName(registrationUserDto.getUsername());
        user.setPassword(passwordEncoder.passwordEncoder().encode(registrationUserDto.getPassword()));
        user.setRoles(List.of(roleService.getUserRole()));
        user.setRole("USER");
        return userRepository.save(user);

    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByName(username);
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }
}
