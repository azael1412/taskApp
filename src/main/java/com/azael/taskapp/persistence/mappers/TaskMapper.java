package com.azael.taskapp.persistence.mappers;

import com.azael.taskapp.persistence.dto.response.status.StatusResponseDto;
import com.azael.taskapp.persistence.dto.response.task.TaskResponseDto;
import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;
import com.azael.taskapp.persistence.entities.Task;

public class TaskMapper {
       public static com.azael.taskapp.persistence.dto.response.task.TaskResponseDto toDTO(Task task) {
        StatusResponseDto status = StatusMapper.toDTO(task.getStatus());
        UserResponseDto user = UserMapper.toDTO(task.getUser());
        return new TaskResponseDto(
            task.getId(),
            task.getName(),
            task.getDescription(),
            status,
            user
        );
    }
}
