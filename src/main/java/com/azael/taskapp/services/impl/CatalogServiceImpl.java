package com.azael.taskapp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.entities.Role;
import com.azael.taskapp.persistence.entities.Status;
import com.azael.taskapp.persistence.repositories.RoleRepository;
import com.azael.taskapp.persistence.repositories.StatusRepository;
import com.azael.taskapp.services.CatalogService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Status> getAllStatus() throws ServiceLogicException {
        try {
            return statusRepository.findAll();
        } catch (Exception e) {
            log.error("Error fetching status: " + e.getMessage(), e);
            throw new ServiceLogicException("Unexpected error occurred while fetching users");
        }
    }

    @Override
    public List<Role> getAllRoles() throws ServiceLogicException {
        try {
            return roleRepository.findAll();
        } catch (Exception e) {
            log.error("Error fetching roles: " + e.getMessage(), e);
            throw new ServiceLogicException("Unexpected error occurred while fetching users");
        }
    }

}
