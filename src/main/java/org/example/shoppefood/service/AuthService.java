package org.example.shoppefood.service;

import org.example.shoppefood.dto.auth.LoginRequest;
import org.example.shoppefood.dto.auth.RegisterRequest;
import org.example.shoppefood.entity.UserEntity;

public interface AuthService {
    UserEntity login(LoginRequest request);
    UserEntity register(RegisterRequest request);
    void logout();
} 