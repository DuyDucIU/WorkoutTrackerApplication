package com.duyduc.workout_tracker.service.impl;

import com.duyduc.workout_tracker.dto.request.LoginRequest;
import com.duyduc.workout_tracker.dto.request.RegisterRequest;
import com.duyduc.workout_tracker.dto.response.AuthResponse;
import com.duyduc.workout_tracker.entity.User;
import com.duyduc.workout_tracker.repository.UserRepo;
import com.duyduc.workout_tracker.security.JwtUtils;
import com.duyduc.workout_tracker.service.AuthService;
import com.duyduc.workout_tracker.exception.ResourceNotFoundException;
import com.duyduc.workout_tracker.exception.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private UserRepo userRepo;

    @Transactional
    @Override
    public String register(RegisterRequest registerRequest) {

        if (userRepo.existsByUsername(registerRequest.getUsername()))
            throw new UserAlreadyExistsException("User already exists with username: " + registerRequest.getUsername());

        if (userRepo.existsByEmail(registerRequest.getEmail()))
            throw new UserAlreadyExistsException("User already exists with email: " + registerRequest.getEmail());

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .build();

        userRepo.save(user);

        return "User registered sucessfully!!";
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authenticatedUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        // Get user to include userId in JWT
        User user = userRepo.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        String token = jwtUtils.generateToken(authenticatedUser, user.getId());

        AuthResponse response = new AuthResponse(token, true);

        return response;
    }
}
