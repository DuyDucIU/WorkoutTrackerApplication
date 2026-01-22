package com.duyduc.workout_tracker.auth.service.impl;

import com.duyduc.workout_tracker.auth.dto.LoginRequest;
import com.duyduc.workout_tracker.auth.dto.RegisterRequest;
import com.duyduc.workout_tracker.auth.entity.User;
import com.duyduc.workout_tracker.auth.repository.UserRepo;
import com.duyduc.workout_tracker.auth.service.AuthService;
import com.duyduc.workout_tracker.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private UserRepo userRepo;

    @Transactional
    @Override
    public String register(RegisterRequest registerRequest) {

        if (userRepo.existsByUsername(registerRequest.getUsername()))
            throw new ApiException("User existed with email: " + registerRequest.getEmail());

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepo.save(user);

        return "User registered sucessfully!!";
    }

    @Override
    public String login(LoginRequest loginRequest) {
        Authentication authenticatedUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        return "Login success fully!";
    }
}
