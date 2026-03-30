package com.example.lol.controller;

import com.example.lol.dto.auth.AuthResponse;
import com.example.lol.dto.auth.LoginRequest;
import com.example.lol.dto.auth.RegisterRequest;
import com.example.lol.entity.Role;
import com.example.lol.security.JwtService;
import com.example.lol.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        String token = authService.register(
                request.name,
                request.email,
                request.password,
                request.role
        );
        Long userId = jwtService.extractUserId(token);
        Role role = jwtService.extractRole(token);
        return new AuthResponse(token, userId, role);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(request.email, request.password);
        Long userId = jwtService.extractUserId(token);
        Role role = jwtService.extractRole(token);
        return new AuthResponse(token, userId, role);
    }
}

