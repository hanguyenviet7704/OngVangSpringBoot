package org.example.shoppefood.service.impl;

import org.example.shoppefood.dto.auth.LoginRequest;
import org.example.shoppefood.dto.auth.RegisterRequest;
import org.example.shoppefood.entity.RoleEntity;
import org.example.shoppefood.entity.UserEntity;
import org.example.shoppefood.exception.AuthenticationException;
import org.example.shoppefood.repository.RoleRepository;
import org.example.shoppefood.repository.UserRepository;
import org.example.shoppefood.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserEntity login(LoginRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new AuthenticationException("Email is required");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new AuthenticationException("Password is required");
            }

            // Find user first to check if exists
            UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("User not found"));

            if (!user.getStatus()) {
                throw new AuthenticationException("Account is disabled");
            }

            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return user;
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public UserEntity register(RegisterRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new AuthenticationException("Email is required");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new AuthenticationException("Password is required");
            }
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                throw new AuthenticationException("Name is required");
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AuthenticationException("Email already exists");
            }

            UserEntity user = new UserEntity();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setName(request.getName());
            user.setRegisterDate(new Date());
            user.setStatus(true);
            user.setAvatar("user.png");

            Set<RoleEntity> roles = new HashSet<>();
            RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AuthenticationException("Default role not found"));
            roles.add(userRole);
            user.setRoles(roles);

            return userRepository.save(user);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
} 