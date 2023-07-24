package com.todolist.todolist.dtoMapper;

import com.todolist.todolist.dto.*;
import com.todolist.todolist.model.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class EventMapper {

    public UserDTO toUserDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getState(),
                user.getTasks()
                        .stream()
                        .map(task -> toTaskDTO(task, user))
                        .collect(Collectors.toList())
        );
    }

    public UserDTO toUserDTO(User user, Task task){
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getState(),
                null
        );
    }

    public TaskDTO toTaskDTO(Task task){
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreationDate(),
                task.getDueDate(),
                toUserDTO(task.getOwner()),
                task.getStatus()
        );
    }

    public TaskDTO toTaskDTO(Task task, User user){

        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreationDate(),
                task.getDueDate(),
                null,
                task.getStatus()
        );
    }
}
