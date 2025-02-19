package com.azael.taskapp.validation.status;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.persistence.entities.Status;
import com.azael.taskapp.persistence.repositories.StatusRepository;

@Component
public class StatusValidator {

    private final StatusRepository statusRepository;

    public StatusValidator(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    // Método para validar la existencia del Status y devolverlo
    public Status validateStatusExistence(Long statusId) throws DataInvalidException {
        return statusRepository.findById(statusId)
                .orElseThrow(() -> {
                    Map<String, String> statusError = new HashMap<>();
                    statusError.put("message", "Status does not exist with id " + statusId);
                    Map<String, Map<String, String>> errors = new HashMap<>();
                    errors.put("status", statusError);
                    return new DataInvalidException("Validation failed", errors);
                });
    }
}

