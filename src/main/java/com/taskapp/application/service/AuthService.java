package com.taskapp.application.service;

import com.taskapp.application.dto.request.LoginRequest;
import com.taskapp.application.dto.request.RegisterRequest;
import com.taskapp.application.dto.response.AuthResponse;
import com.taskapp.application.port.out.UserRepositoryPort;
import com.taskapp.domain.entity.User;
import com.taskapp.domain.enums.Role;
import com.taskapp.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already registered: " + request.email());
        }

        User user = User.builder()
            .name(request.name())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.USER)
            .build();

        user = userRepository.save(user);
        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }
}
