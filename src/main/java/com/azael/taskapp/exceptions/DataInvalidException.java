package com.azael.taskapp.exceptions;

import java.util.Map;

public class DataInvalidException extends RuntimeException {
    // private final List<ApiResponseDataInvalidDto> errors;

    // public DataInvalidException(String message, List<ApiResponseDataInvalidDto>
    // errors) {
    // super(message);
    // this.errors = errors;
    // }

    // public List<ApiResponseDataInvalidDto> getErrors() {
    // return errors;
    // }

    private final Map<String, Map<String, String>> errors;

    // Constructor que recibe el mensaje de error y el mapa de errores
    public DataInvalidException(String message, Map<String, Map<String, String>> errors) {
        super(message);
        this.errors = errors;
    }

    // Getter para obtener los errores
    public Map<String, Map<String, String>> getErrors() {
        return errors;
    }
}
