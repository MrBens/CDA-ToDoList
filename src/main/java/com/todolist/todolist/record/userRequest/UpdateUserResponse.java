package com.todolist.todolist.record.userRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.todolist.todolist.dto.UserDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateUserResponse(UserDTO user, String jwtToken, String refreshToken) {
}
