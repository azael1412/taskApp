package com.azael.taskapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.exceptions.NotFoundException;
import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.dto.request.user.CreateUserRequestDto;
import com.azael.taskapp.persistence.dto.request.user.UpdateUserRequestDto;
import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;
import com.azael.taskapp.persistence.entities.User;


@Service
public interface UserService {
        UserResponseDto register(CreateUserRequestDto newUserDetails) throws ServiceLogicException, DataInvalidException;
        Page<User> getAll(int page, int size, Sort sort, String name, String email) throws ServiceLogicException;
        UserResponseDto update(UpdateUserRequestDto newUserDetails, Long id) throws NotFoundException, ServiceLogicException, DataInvalidException;
        void delete(Long id) throws NotFoundException, ServiceLogicException;
        UserResponseDto show(Long id) throws NotFoundException, ServiceLogicException;
        UserResponseDto changeStatus(Long id) throws NotFoundException, ServiceLogicException;
}