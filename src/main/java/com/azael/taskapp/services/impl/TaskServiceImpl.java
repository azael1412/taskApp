package com.azael.taskapp.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.exceptions.NotFoundException;
import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.dto.request.task.CreateTaskRequestDto;
import com.azael.taskapp.persistence.dto.request.task.UpdateTaskRequestDto;
import com.azael.taskapp.persistence.dto.response.task.TaskResponseDto;
import com.azael.taskapp.persistence.entities.Status;
import com.azael.taskapp.persistence.entities.Task;
import com.azael.taskapp.persistence.entities.User;
import com.azael.taskapp.persistence.mappers.TaskMapper;
import com.azael.taskapp.persistence.repositories.StatusRepository;
import com.azael.taskapp.persistence.repositories.TaskRepository;
import com.azael.taskapp.services.TaskService;
import com.azael.taskapp.validation.status.StatusValidator;
import com.azael.taskapp.validation.task.edit.UpdateTaskValidator;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final AuthServiceImpl authServiceImpl;
    private final UpdateTaskValidator taskValidator;
    private final StatusValidator statusValidator;


    public TaskServiceImpl(TaskRepository taskRepository, StatusRepository statusRepository,
            AuthServiceImpl authServiceImpl, UpdateTaskValidator taskValidator, StatusValidator statusValidator) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.authServiceImpl = authServiceImpl;
        this.taskValidator = taskValidator;
        this.statusValidator = statusValidator;
    }


    @Transactional
    @Override
    public TaskResponseDto create(CreateTaskRequestDto newTaskDetails) throws ServiceLogicException, DataInvalidException {
            // Validar si el status existe
            Status status = statusRepository.findById(newTaskDetails.statusId()).orElseThrow(() -> new NotFoundException("Status not found with id " + newTaskDetails.statusId()));
                    // .orElseThrow(() -> {
                    //     Map<String, String> statusError = new HashMap<>();
                    //     statusError.put("message", "Status does not exist with id " + newTaskDetails.statusId());
                    //     Map<String, Map<String, String>> errors = new HashMap<>();
                    //     errors.put("status", statusError);
                    //     return new DataInvalidException("Validation failed", errors);
                    // });
    
            // Validar si el usuario existe
            // User user = userRepository.findById(userId) // Usamos newTaskDetails.getUser().getId()
            //         .orElseThrow(() -> 
            //             new NotFoundException("User not found")
                        // Map<String, String> userError = new HashMap<>();
                        // userError.put("message", "User does not exist with id " + userId);
                        // Map<String, Map<String, String>> errors = new HashMap<>();
                        // errors.put("user", userError);
                        // return new DataInvalidException("Validation failed", errors);
                    // );

            User currentUser = authServiceImpl.me();
            if(currentUser == null) {
                throw new NotFoundException("User not found");
            }
    
            // Verificar que el nombre de la tarea sea único para ese usuario
            // if (taskRepository.existsByUserIdAndName(currentUser.getId(), newTaskDetails.name())) {
            //     Map<String, String> taskNameError = new HashMap<>();
            //     taskNameError.put("message", "Task name must be unique for the user.");
            //     Map<String, Map<String, String>> errors = new HashMap<>();
            //     errors.put("name", taskNameError);
            //     throw new DataInvalidException("Validation failed", errors);
            // }
            // Crear la nueva tarea
            Task newTask = new Task();
            newTask.setName(newTaskDetails.name());
            newTask.setDescription(newTaskDetails.description());
            newTask.setStatus(status);
            newTask.setUser(currentUser);
            // newTask.setCreatedAt(LocalDateTime.now());
            // newTask.setUpdatedAt(LocalDateTime.now());
    
            // Guardar la tarea en la base de datos
            Task task = taskRepository.save(newTask);
            return TaskMapper.toDTO(task);

    }
    
    
    @Transactional
    @Override
    public TaskResponseDto update(UpdateTaskRequestDto newTaskDetails, Long id) throws NotFoundException, ServiceLogicException, DataInvalidException {
     
            // Buscar la tarea actual
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Task not found with id " + id));
    
            // Validar si el status existe
            Status status = statusRepository.findById(newTaskDetails.statusId())
                    .orElseThrow(() -> new NotFoundException("Status not found with id " + newTaskDetails.statusId())
                        // Map<String, String> statusError = new HashMap<>();
                        // statusError.put("message", "Status does not exist with id " + newTaskDetails.statusId());
                        // Map<String, Map<String, String>> errors = new HashMap<>();
                        // errors.put("status", statusError);
                        // return new DataInvalidException("Validation failed", errors);
                    );
    
            // Validar si el usuario existe
            // User user = userRepository.findById(newTaskDetails.userId()) // Usamos newTaskDetails.getUser().getId()
            //         .orElseThrow(() -> {
            //             Map<String, String> userError = new HashMap<>();
            //             userError.put("message", "User does not exist with id " + newTaskDetails.userId());
            //             Map<String, Map<String, String>> errors = new HashMap<>();
            //             errors.put("user", userError);
            //             return new DataInvalidException("Validation failed", errors);
            //         });
            User currentUser = authServiceImpl.me();
            if(currentUser == null) {
                throw new NotFoundException("User not found");
            }
    
            //Verificar que el nombre de la tarea sea único para ese usuario (excepto la tarea que estamos actualizando)
            // if (!task.getName().equals(newTaskDetails.name()) && 
            //     taskRepository.existsByUserIdAndNameAndIdNot(currentUser.getId(), newTaskDetails.name(), id)) {
            //     Map<String, String> taskNameError = new HashMap<>();
            //     taskNameError.put("message", "Task name must be unique for the user.");
            //     Map<String, Map<String, String>> errors = new HashMap<>();
            //     errors.put("name", taskNameError);
            //     throw new DataInvalidException("Validation failed", errors);
            // }
            // Configurar el ID de la tarea a excluir en el validador
             // Configurar el ID de la tarea a excluir en el validador
            // UpdateUniqueTaskNameForUserValidator validator = new UpdateUniqueTaskNameForUserValidator();
            // validator.setTaskIdToExclude(id); // Aquí pasamos el ID de la tarea actual

            // // Validar el nombre de la tarea usando el validador
            // boolean isTaskNameValid = validator.isValid(newTaskDetails.name(), null);
            // if (!isTaskNameValid) {
            //     // Lanzamos DataInvalidException si no es válido
            //     Map<String, String> taskNameError = new HashMap<>();
            //     taskNameError.put("message", "Task name must be unique for the user.");
            //     Map<String, Map<String, String>> errors = new HashMap<>();
            //     errors.put("name", taskNameError);
            //     throw new DataInvalidException("Validation failed", errors);
            // }
            // Validar que el nombre sea único para el usuario actual
            taskValidator.validateUniqueTaskName(currentUser.getId(), newTaskDetails.name(), id);
            // Actualizar la tarea con los nuevos detalles
            task.setName(newTaskDetails.name());
            task.setDescription(newTaskDetails.description());
            task.setStatus(status);
            task.setUser(currentUser);
            // task.setUpdatedAt(LocalDateTime.now());
    
            // Guardar la tarea actualizada
            return TaskMapper.toDTO(task);
    }
    

    @Override
    public Page<Task> getAll(int page, int size, Sort sort, String name) throws ServiceLogicException {
        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, size, sort);
    
        // Obtener el usuario actual
        User currentUser = authServiceImpl.me();
        // UserMapper.toEntity(currentUser);
        Long userId = currentUser.getId();
    
        // Determinar si el usuario es administrador
        boolean isAdmin = currentUser.getRole().getId() == 1;
    
        // Validar si el nombre está vacío o nulo
        boolean isNameEmpty = name == null || name.trim().isEmpty();
    
        // Construir la consulta según las condiciones
        if (isAdmin) {
            return isNameEmpty 
                    ? taskRepository.findAll(pageable) 
                    : taskRepository.findByNameLike(name, pageable);
        } else {
            return isNameEmpty 
                    ? taskRepository.findByUserId(pageable, userId) 
                    : taskRepository.findByNameLikeAndUserId(name, pageable, userId);
        }
    }

    @Transactional
    @Override
    public void delete(Long id) throws NotFoundException, ServiceLogicException {
            // Verificar si la tarea existe
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Task not found with id " + id));

            // Eliminar la tarea
            taskRepository.delete(task);
    }

    @Override
    public TaskResponseDto show(Long id) throws NotFoundException, ServiceLogicException {
        // Obtener el usuario actual
        User currentUser = authServiceImpl.me();
        Long userId = currentUser.getId();
    
        // Determinar si el usuario es administrador
        boolean isAdmin = currentUser.getRole().getId() == 1;
    
        // Buscar la tarea dependiendo del rol del usuario
        Task task = isAdmin 
                ? taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id " + id))
                : taskRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Task not found for this user"));
    
        // Mapear la tarea encontrada a un DTO y devolverla
        return TaskMapper.toDTO(task);
    }
    
    @Transactional
    @Override
    public TaskResponseDto changeStatus(Long taskId, Long statusId) throws NotFoundException, DataInvalidException, ServiceLogicException {
            // Buscar la tarea actual
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new NotFoundException("Task not found with id " + taskId));
    
            // Validar si el status existe
            // Status status = statusRepository.findById(statusId)
            //         .orElseThrow(() -> {
            //             Map<String, String> statusError = new HashMap<>();
            //             statusError.put("message", "Status does not exist with id " + statusId);
            //             Map<String, Map<String, String>> errors = new HashMap<>();
            //             errors.put("status", statusError);
            //             return new DataInvalidException("Validation failed", errors);
            // });
            // Validar que el Status exista
            // Validar y obtener el Status utilizando el StatusValidator
            Status status = statusValidator.validateStatusExistence(statusId);
    
            // Cambiar solo el status de la tarea
            task.setStatus(status);
    
            // Guardar la tarea con el nuevo status
            return TaskMapper.toDTO(task);
       
    }
}
