package com.azael.taskapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.entities.Role;
import com.azael.taskapp.persistence.entities.Status;

@Service
public interface CatalogService {
    List<Status> getAllStatus() throws ServiceLogicException;
    List<Role> getAllRoles() throws ServiceLogicException;
}
