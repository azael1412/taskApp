package com.azael.taskapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.exceptions.NotFoundException;
import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.dto.request.task.CreateTaskRequestDto;
import com.azael.taskapp.persistence.dto.request.task.UpdateTaskRequestDto;
import com.azael.taskapp.persistence.dto.response.task.TaskResponseDto;
import com.azael.taskapp.persistence.entities.Task;

@Service
public interface TaskService {
        Page<Task> getAll(int page, int size, Sort sort, String name) throws ServiceLogicException;
        TaskResponseDto show(Long id) throws NotFoundException, ServiceLogicException;
        TaskResponseDto changeStatus(Long id, Long StatusId) throws NotFoundException, ServiceLogicException;
        TaskResponseDto create(CreateTaskRequestDto newTaskDetails) throws ServiceLogicException, DataInvalidException;
        TaskResponseDto update(UpdateTaskRequestDto newTaskDetails, Long id) throws NotFoundException, ServiceLogicException, DataInvalidException;
        void delete(Long id) throws NotFoundException, ServiceLogicException;
}
