package com.azael.taskapp.persistence.mappers;

import com.azael.taskapp.persistence.dto.response.status.StatusResponseDto;
import com.azael.taskapp.persistence.entities.Status;

public class StatusMapper {
     public static StatusResponseDto toDTO(Status status) {
        return new StatusResponseDto(
            status.getId(),
            status.getName()
        );
    }
}
