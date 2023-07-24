package com.todolist.todolist.service;

import com.todolist.todolist.config.JwtService;
import com.todolist.todolist.dto.TaskDTO;
import com.todolist.todolist.dto.UserDTO;
import com.todolist.todolist.dtoMapper.EventMapper;
import com.todolist.todolist.exception.EntityAlreadyExistsException;
import com.todolist.todolist.exception.EntityNotFoundException;
import com.todolist.todolist.model.Task;
import com.todolist.todolist.model.User;
import com.todolist.todolist.record.taskRequest.UpdateTaskRequest;
import com.todolist.todolist.record.userRequest.UpdateUserResponse;
import com.todolist.todolist.repository.TaskRepository;
import com.todolist.todolist.utils.PropertyChecker;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public record TaskService(
        TaskRepository taskRepository,
        JwtService jwtService,
        EventMapper eventMapper,
        PropertyChecker propertyChecker
) {
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(eventMapper::toTaskDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTask(Integer taskId, String token) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException(Task.class, "ID:", taskId.toString()));

        if (!jwtService.extractUsername(token).equalsIgnoreCase(task.getOwner().getEmail()) && !jwtService.extractRole(token).contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("Insufficient permission");
        }
        return eventMapper.toTaskDTO(task);
    }

    public TaskDTO updateTask(Integer taskId, UpdateTaskRequest taskRequest, String token) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, "ID", taskId.toString()));

        if (jwtService.extractUsername(token).equals(task.getOwner().getEmail()) || jwtService.extractRole(token).contains("ROLE_ADMIN")) {

            if (!propertyChecker.isPropertiesSame(taskRequest.title(), task.getTitle())) {
                task.setTitle(taskRequest.title());
            }

            if (!propertyChecker.isPropertiesSame(taskRequest.description(), task.getDescription())) {
                task.setDescription(taskRequest.description());
            }

            if (!propertyChecker.isPropertiesSame(taskRequest.status(), task.getStatus())) {
                task.setStatus(taskRequest.status());
            }

            if (!propertyChecker.isPropertiesSame(taskRequest.status(), task.getStatus())) {
                task.setStatus(taskRequest.status());
            }

        } else {
            throw new AccessDeniedException("Insufficient permission");
        }

        return eventMapper.toTaskDTO(taskRepository.save(task));
    }

    public boolean deleteTask(Integer taskId, String token) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "ID", taskId.toString()));

        if (!jwtService.extractUsername(token).equalsIgnoreCase(task.getOwner().getEmail()) && !jwtService.extractRole(token).contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("Insufficient permission");
        }

        taskRepository.save(task);
        return true;
    }
}
