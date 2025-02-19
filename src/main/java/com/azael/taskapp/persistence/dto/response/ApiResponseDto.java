package com.azael.taskapp.persistence.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {
    // private boolean success;
    // private String message;
    // private T data;
    private String status;
    private String message;
    private T results;
}
