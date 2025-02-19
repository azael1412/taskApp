package com.azael.taskapp.persistence.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDataInvalidDto {
    // private String message;
    // private String field;
    //private String code;

    private Map<String, Map<String, String>> errors;
    // Getter y Setter
    public Map<String, Map<String, String>> getErrors() {
        return errors;
    }

    // public void setErrors(Map<String, Map<String, String>> errors) {
    //     this.errors = errors;
    // }

}
