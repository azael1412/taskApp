package com.azael.taskapp.validation.task.edit;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.persistence.repositories.TaskRepository;

@Component
public class UpdateTaskValidator {
     private final TaskRepository taskRepository;

    public UpdateTaskValidator(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void validateUniqueTaskName(Long userId, String newName, Long taskId) {
        if (taskRepository.existsByUserIdAndNameAndIdNot(userId, newName, taskId)) {
            Map<String, String> taskNameError = Map.of("message", "Task name must be unique for the user.");
            Map<String, Map<String, String>> errors = Map.of("name", taskNameError);
            throw new DataInvalidException("Validation failed", errors);
        }
    }
}
