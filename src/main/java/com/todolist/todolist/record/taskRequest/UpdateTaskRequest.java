package com.todolist.todolist.record.taskRequest;

import com.todolist.todolist.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskRequest(

        @NotNull(message = "title cannot be null")
        @NotEmpty(message = "title cannot be empty")
        String title,

        @NotNull(message = "description cannot be null")
        @NotEmpty(message = "description cannot be empty")
        String description,

        @NotNull(message = "due date cannot be null")
        @NotEmpty(message = "due date cannot be empty")
        String dueDate,

        @NotEmpty(message = "status cannot be empty")
        @NotNull(message = "status cannot be null")
        Status status
) {
}
