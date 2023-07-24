package com.todolist.todolist.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.todolist.config.JwtService;
import com.todolist.todolist.exception.EntityAlreadyExistsException;
import com.todolist.todolist.exception.EntityNotFoundException;
import com.todolist.todolist.model.Role;
import com.todolist.todolist.model.User;
import com.todolist.todolist.record.authentication.AuthenticationRequest;
import com.todolist.todolist.record.authentication.AuthenticationResponse;
import com.todolist.todolist.record.userRequest.UserRegistrationRequest;
import com.todolist.todolist.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public record AuthenticationService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        AuthenticationManager authenticationManager
) {

    public AuthenticationResponse register(UserRegistrationRequest request) {

        // TODO : Random generate 4 numbers code then hash it
        if (userRepository.existsByEmailIgnoreCase(request.email())){
            throw new EntityAlreadyExistsException(User.class, "email", request.email());
        }

        User user = User.builder()
                .email(request.email().toLowerCase())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .password(passwordEncoder.encode(request.password()))
                .state(1)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return new AuthenticationResponse(jwtService.generateToken(user), jwtService.generateRefreshToken(user));
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password()));

        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new EntityNotFoundException(User.class, "email", request.email()));

        return new AuthenticationResponse(jwtService.generateToken(user), jwtService.generateRefreshToken(user));
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            User user = userRepository.findByEmailIgnoreCase(userEmail)
                    .orElseThrow(() -> new EntityNotFoundException(User.class, "email", userEmail));

            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                AuthenticationResponse authResponse = new AuthenticationResponse(accessToken, refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
