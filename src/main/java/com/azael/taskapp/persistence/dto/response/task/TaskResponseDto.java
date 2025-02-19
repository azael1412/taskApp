package com.azael.taskapp.persistence.dto.response.task;

import com.azael.taskapp.persistence.dto.response.status.StatusResponseDto;
import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// import org.springframework.hateoas.RepresentationModel;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto //extends RepresentationModel<TaskResponseDto> 
{
    private Long id;

    private String name;

    private String description;

    private StatusResponseDto status;

    private UserResponseDto user;

}
