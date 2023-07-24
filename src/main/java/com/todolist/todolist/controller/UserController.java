package com.todolist.todolist.controller;

import com.todolist.todolist.config.JwtService;
import com.todolist.todolist.dto.UserDTO;
import com.todolist.todolist.exception.ValidationException;
import com.todolist.todolist.record.userRequest.UpdateUserRequest;
import com.todolist.todolist.record.userRequest.UpdateUserResponse;
import com.todolist.todolist.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/users")
public record UserController(UserService userService, JwtService jwtService) {

    @GetMapping
    public @ResponseBody List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable Integer userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return userService.getUser(userId, token.substring(7));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @PathVariable Integer userId,
            @Valid @RequestBody UpdateUserRequest userRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            BindingResult bindingResult
    ){

        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult.getFieldErrors());
        return ResponseEntity.ok().body(userService.updateUser(userId, userRequest, token.substring(7)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Integer userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return ResponseEntity.ok().body(userService.deleteUser(userId, token.substring(7)));
    }
}
