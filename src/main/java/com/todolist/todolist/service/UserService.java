package com.todolist.todolist.service;

import com.todolist.todolist.config.JwtService;
import com.todolist.todolist.dto.UserDTO;
import com.todolist.todolist.dtoMapper.EventMapper;
import com.todolist.todolist.exception.EntityAlreadyExistsException;
import com.todolist.todolist.exception.EntityNotFoundException;
import com.todolist.todolist.model.User;
import com.todolist.todolist.record.userRequest.UpdateUserRequest;
import com.todolist.todolist.record.userRequest.UpdateUserResponse;
import com.todolist.todolist.utils.PropertyChecker;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;
import com.todolist.todolist.repository.UserRepository;

@Service
public record UserService(
        UserRepository userRepository,
        EventMapper eventMapper,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        PropertyChecker propertyChecker
) {

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(eventMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(Integer id, String token) {

        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "ID:", id.toString()));

        if (!jwtService.extractUsername(token).equalsIgnoreCase(user.getEmail()) && !jwtService.extractRole(token).contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("Insufficient permission");
        }
        return eventMapper.toUserDTO(user);
    }

    public UpdateUserResponse updateUser(Integer userId, UpdateUserRequest request, String token) {
        String jwtToken = null;
        String refreshToken = null;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "ID", userId.toString()));

        if (jwtService.extractUsername(token).equals(user.getEmail()) || jwtService.extractRole(token).contains("ROLE_ADMIN")) {

            if (!propertyChecker.isPropertiesSame(request.firstName(), user.getFirstName())) {
                user.setFirstName(request.firstName());
            }

            if (!propertyChecker.isPropertiesSame(request.lastName(), user.getLastName())) {
                user.setLastName(request.lastName());
            }

            if (!propertyChecker.isPropertiesSame(request.phoneNumber(), user.getPhoneNumber())) {
                user.setPhoneNumber(request.phoneNumber());
            }

            if (!propertyChecker.isPropertiesSame(request.state(), user.getState())) {
                user.setState(request.state());
            }

        } else {
            throw new AccessDeniedException("Insufficient permission");
        }

        if (jwtService.extractUsername(token).equalsIgnoreCase(user.getEmail())) {

            if (!propertyChecker.isPropertiesSame(request.email().toLowerCase(), user.getEmail().toLowerCase()) && userRepository.existsByEmailIgnoreCase(request.email())) {
                throw new EntityAlreadyExistsException(User.class, "email", request.email());
            }

            if (!propertyChecker.isPropertiesSame(request.email().toLowerCase(), user.getEmail().toLowerCase())) {
                user.setEmail(request.email().toLowerCase());

                jwtToken = jwtService.generateToken(user);
                refreshToken = jwtService.generateRefreshToken(user);
            }

            if ((request.newPassword() != null) && (!changeUserPassword(user, request.password(), request.newPassword(), request.validNewPassword()))) {
                throw new InputMismatchException("Failed new password matching verification");
            }

        } else if ((request.email() != null || request.password() != null) && jwtService.extractRole(token).contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("Insufficient permission");
        }


        Date updateDate = Date.valueOf(LocalDate.now());
        user.setLastUpdate(updateDate);
        user = userRepository.save(user);

        UserDTO userDTO = eventMapper.toUserDTO(user);

        return new UpdateUserResponse(userDTO, jwtToken, refreshToken);
    }

    public boolean changeUserPassword(User user, String password, String newPassword, String validNewPassword) {

        if (passwordEncoder.matches(password, user.getPassword()) && newPassword.equals(validNewPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }

    public boolean deleteUser(Integer userId, String token) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "ID", userId.toString()));

        if (!jwtService.extractUsername(token).equalsIgnoreCase(user.getEmail()) && !jwtService.extractRole(token).contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("Insufficient permission");
        }

        user.setState(3);
        userRepository.save(user);
        return true;
    }
}
