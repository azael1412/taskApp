package com.azael.taskapp.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.azael.taskapp.persistence.dto.response.ApiResponseDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseStatus;

public class ApiResponseHelper {

    // Versión del método con "data" opcional (por defecto será null) y estado
    // predeterminado como "FAIL"
    public static <T> ResponseEntity<ApiResponseDto<T>> createResponse(HttpStatus status, String message) {
        return createResponse(status, message, ApiResponseStatus.FAIL, null); // Por defecto "FAIL"
    }

    // Versión del método con el parámetro "data" y el estado explícitamente
    // recibido
    public static <T> ResponseEntity<ApiResponseDto<T>> createResponse(HttpStatus status, String message,
            ApiResponseStatus apiResponseStatus, T data) {
        return ResponseEntity.status(status)
                .body(new ApiResponseDto<>(apiResponseStatus.name(), message, data));
    }
}
