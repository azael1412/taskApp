package com.azael.taskapp.validation.role;

import org.springframework.beans.factory.annotation.Autowired;

import com.azael.taskapp.persistence.repositories.RoleRepository;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleExistByIdValidator  implements ConstraintValidator<RoleExistById, Long> {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean isValid(Long roleId, ConstraintValidatorContext context) {
        return roleId != null && roleRepository.existsById(roleId);
    }

}
