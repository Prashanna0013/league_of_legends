package com.example.lol.dto.auth;

import com.example.lol.entity.Role;

public class AuthResponse {
    public String token;
    public Long userId;
    public Role role;

    public AuthResponse(String token, Long userId, Role role) {
        this.token = token;
        this.userId = userId;
        this.role = role;
    }
}

