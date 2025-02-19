package com.azael.taskapp.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.exceptions.ForbiddenException;
import com.azael.taskapp.exceptions.InvalidAuthException;
import com.azael.taskapp.exceptions.NotFoundException;
import com.azael.taskapp.exceptions.RateLimitException;
import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.exceptions.UnauthorizedException;
import com.azael.taskapp.helper.ApiResponseHelper;
import com.azael.taskapp.persistence.dto.response.ApiResponseDataInvalidDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseStatus;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataInvalidException.class)
    public ResponseEntity<ApiResponseDto<ApiResponseDataInvalidDto>> handleDataInvalidException(
            DataInvalidException ex) {
        log.info("DataInvalidException captured: {}", ex.getMessage());
        Map<String, Map<String, String>> errorMap = ex.getErrors();
        ApiResponseDataInvalidDto errorResponse = new ApiResponseDataInvalidDto(errorMap);
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, "Validation failed", ApiResponseStatus.FAIL,
                errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNotFoundException(NotFoundException e) {
        log.info("NotFoundException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidAuthException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleInvalidAuthException(InvalidAuthException e) {
        log.info("InvalidAuthException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleHandlerMethodValidationException(
            HandlerMethodValidationException e) {
        log.info("HandlerMethodValidationException captured: {}", e.getMessage(), e);
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> handleBadRequest(Exception e) {
        log.info("BadRequest captured: {}", e.getMessage());
        Map<String, Map<String, String>> errors = new HashMap<>();

        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
                log.info(fieldError.getField());
                Map<String, String> errorDetails = new HashMap<>();
                errorDetails.put("message", fieldError.getDefaultMessage());
                // errorDetails.put("rejectedValue",
                // String.valueOf(fieldError.getRejectedValue()));
                errors.put(fieldError.getField(), errorDetails);
            });
        } else if (e instanceof HttpMessageNotReadableException) {
            errors.put("request", Map.of("message", "Malformed JSON request. " + e.getMessage()));
        }

        Map<String, Object> results = new HashMap<>();
        results.put("errors", errors);
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, "Validation failed", ApiResponseStatus.FAIL,
                results);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleSignatureException(SignatureException e) {
        log.error("SignatureException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.UNAUTHORIZED, "Token not valid.");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleExpiredJwtException(ExpiredJwtException e) {
        log.error("SignatureException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.UNAUTHORIZED, "Token has expired.");
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseDto<String>> handleBadRequestException(BadRequestException e) {
        log.error("BadRequestException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponseDto<String>> handleIOException(IOException e) {
        log.error("IOException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ApiResponseDto<String>> handleServletException(ServletException e) {
        log.error("ServletException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto<String>> handleBadCredentialsException(BadCredentialsException e) {
        log.error("BadCredentialsException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponseDto<String>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.error("BadRequestException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDto<String>> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponseDto<String>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error("BadRequestException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiResponseDto<String>> handleRateLimitException(RateLimitException e) {
        log.error("RateLimitException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        log.info("ConstraintViolationException captured: {}", ex.getMessage());

        // Crear un mapa para almacenar los errores de validación
        Map<String, Map<String, String>> errors = new HashMap<>();

        // Iterar sobre las violaciones de restricción
        ex.getConstraintViolations().forEach(violation -> {
            // Obtener el nombre del campo que falló la validación
            String fieldName = violation.getPropertyPath().toString();
            // Obtener el mensaje de error
            String errorMessage = violation.getMessage();

            // Agregar el error al mapa
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", errorMessage);
            errors.put(fieldName, errorDetails);
        });

        // Crear un mapa "results" que contenga el campo "errors"
        Map<String, Object> results = new HashMap<>();
        results.put("errors", errors);

        // Devolver la respuesta con el formato deseado
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, "Validation failed", ApiResponseStatus.FAIL,
                results);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseDto<String>> handleUnauthorizedException(UnauthorizedException e) {
        log.info("UnauthorizedException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.UNAUTHORIZED, "Unauthorized access");
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponseDto<String>> handleForbiddenException(ForbiddenException e) {
        log.info("ForbiddenException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.FORBIDDEN, "Access forbidden");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseDto<String>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        log.info("HttpRequestMethodNotSupportedException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDto<String>> handleIllegalStateException(IllegalStateException e) {
        log.info("IllegalStateException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
    }

    @ExceptionHandler(ServiceLogicException.class)
    public ResponseEntity<ApiResponseDto<String>> handleServiceLogicException(ServiceLogicException e) {
        log.info("ServiceLogicException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto<String>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.info("NoResourceFoundException captured: {}", e.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDto<String>> handleInvalidTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.info("MethodArgumentTypeMismatchException captured: {}", ex.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ApiResponseDto<String>> internalServerErrorException(InternalServerError ex) {
        log.info("InternalServerError captured: {}", ex.getMessage());
        return ApiResponseHelper.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponseDto<String>> handleValidationException(ValidationException ex) {
        log.error("ValidationException captured: {}", ex.getMessage(), ex);
        return ApiResponseHelper.createResponse(HttpStatus.BAD_REQUEST, "Validation failed", ApiResponseStatus.FAIL,
                ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGenericException(Exception e) {
        log.error("Unexpected error occurred: ", e);
        return ApiResponseHelper.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
    }
}