package com.azael.taskapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.azael.taskapp.persistence.dto.response.ApiResponseDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseStatus;

public abstract class BaseController {
// Método para generar una respuesta de error consistente
    protected ResponseEntity<ApiResponseDto<?>> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), message, null));
    }
}
