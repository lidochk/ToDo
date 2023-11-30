package com.example.todo.service;

import com.example.todo.Exception.ApiRequestException;
import com.example.todo.entity.Task;
import com.example.todo.entity.User;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(String title, String description, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiRequestException("User with id " + userId + " not found"));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(false); // Предполагаю, что status начинается с false
        task.setCreatedAt(LocalDateTime.now());
        task.setUser(user);

        return taskRepository.save(task);
    }

    public List<Task> getUserTasks(Long userId) {
        return taskRepository.findByUserUserId(userId);
    }

}
