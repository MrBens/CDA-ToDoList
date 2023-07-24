package com.todolist.todolist.controller;

import com.todolist.todolist.dto.TaskDTO;
import com.todolist.todolist.exception.ValidationException;
import com.todolist.todolist.record.taskRequest.UpdateTaskRequest;
import com.todolist.todolist.service.TaskService;
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
@RequestMapping("api/v1/tasks")
public record TaskController ( TaskService taskService

) {
    @GetMapping
    public @ResponseBody List<TaskDTO> getAllTasks(){
        return taskService.getAllTasks();
    }

    @GetMapping("/{taskId}")
    public TaskDTO getTask(@PathVariable Integer taskId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return taskService.getTask(taskId, token.substring(7));
    }

    @PutMapping("/{taskId}")
    public TaskDTO updateTask(
            @PathVariable Integer taskId,
            @Valid @RequestBody UpdateTaskRequest taskRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult.getFieldErrors());
        return taskService.updateTask(taskId, taskRequest, token.substring(7));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable Integer taskId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return ResponseEntity.ok().body(taskService.deleteTask(taskId, token.substring(7)));
    }
}
