package com.azael.taskapp.validation.status;



import com.azael.taskapp.persistence.repositories.StatusRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusExistByIdValidator implements ConstraintValidator<StatusExistByid, Long> {
    private StatusRepository statusRepository;

    public StatusExistByIdValidator(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public boolean isValid(Long statusId, ConstraintValidatorContext context) {
        if (statusId == null) {
            return true;  // Si el nombre de la tarea está vacío, no validamos aquí (puede validarse por otros medios)
        }
        return statusRepository.existsById(statusId);
    }
}
