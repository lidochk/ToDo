package com.example.todo.controller;

import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Task;
import com.example.todo.service.JwtService;
import com.example.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtService jwtService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequest createTaskRequest,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        String userId = jwtService.extractUserId(token);
        Task createdTask = taskService.createTask(createTaskRequest.getTitle(),
                createTaskRequest.getDescription(),
                Long.parseLong(userId));

        TaskDto taskDto = new TaskDto();
        taskDto.setId(createdTask.getId());
        taskDto.setTitle(createdTask.getTitle());
        taskDto.setDescription(createdTask.getDescription());
        taskDto.setStatus(createdTask.isStatus());
        taskDto.setCreatedAt(createdTask.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(taskDto);
    }

}
