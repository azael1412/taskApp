package com.azael.taskapp.persistence.dto.response.status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusResponseDto {
    private Long id;
    private String name;
}
