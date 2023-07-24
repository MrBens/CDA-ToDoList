package com.todolist.todolist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.todolist.todolist.enums.Status;

import java.sql.Date;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskDTO(
        Integer id,

        String title,

        String description,

        Date creationDate,

        Date dueDate,

        UserDTO owner,

        Status status

) {
}
