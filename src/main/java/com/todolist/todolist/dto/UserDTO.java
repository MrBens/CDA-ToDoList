package com.todolist.todolist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer state,
        List<TaskDTO> tasks
){
}
