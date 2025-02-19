package com.azael.taskapp.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.exceptions.NotFoundException;
import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.dto.request.user.CreateUserRequestDto;
import com.azael.taskapp.persistence.dto.request.user.UpdateUserRequestDto;
import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;
import com.azael.taskapp.persistence.entities.Role;
import com.azael.taskapp.persistence.entities.User;
import com.azael.taskapp.persistence.mappers.UserMapper;
import com.azael.taskapp.persistence.repositories.RoleRepository;
import com.azael.taskapp.persistence.repositories.UserRepository;
import com.azael.taskapp.services.UserService;
import com.azael.taskapp.validation.user.edit.UpdateUserValidator;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UpdateUserValidator userValidator;

    @Transactional
    @Override
    public UserResponseDto register(CreateUserRequestDto newUserDetails) throws ServiceLogicException {
    
            //Mapa para almacenar errores de validación
            // Map<String, Map<String, String>> errors = new HashMap<>();
    
            // Verificar si ya existe un usuario con el mismo correo
            // if (userRepository.findByEmail(newUserDetails.email()).isPresent()) {
            //     Map<String, String> emailError = new HashMap<>();
            //     emailError.put("message", "Registration failed: User already exists with email " + newUserDetails.email());
            //     errors.put("email", emailError);
            // }
    
            // Verificar si ya existe un usuario con el mismo nombre de usuario
            // if (userRepository.findByUsername(newUserDetails.username()).isPresent()) {
            //     Map<String, String> usernameError = new HashMap<>();
            //     usernameError.put("message", "Registration failed: User already exists with username " + newUserDetails.username());
            //     errors.put("username", usernameError);
            // }

            // if (!errors.isEmpty()) {
            //     throw new DataInvalidException("Validation failed", errors);
            // }
    
            // Verificar si el rol está presente y es válido
            // if (newUserDetails.getRole() == null || newUserDetails.roleId() == null) {
            //     Map<String, String> roleError = new HashMap<>();
            //     roleError.put("message", "Registration failed: Role is required and should not be null.");
            //     errors.put("role", roleError);
            // } else {
                // Recuperar el rol del repositorio
                Role role = roleRepository.findById(newUserDetails.roleId())
                .orElseThrow(() -> new NotFoundException("Role not found with id " + newUserDetails.roleId()));  // Mejorar el mensaje de la excepción
            
                // {
                //     Map<String, String> roleError = new HashMap<>();
                //     roleError.put("message", "Role does not exist with id " + newUserDetails.roleId());
                //     errors.put("role", roleError); // Agregar el error al mapa de errores
                //     return new DataInvalidException("Role validation failed", errors); // Lanzar DataInvalidException con los errores
                // }
                // );
               
                // Si no hay errores, proceder a crear el nuevo usuario
                User newUser = new User(newUserDetails.name(),
                        newUserDetails.username(), newUserDetails.email(),
                        newUserDetails.phone(), passwordEncoder.encode(newUserDetails.password()), true, role);
                // Guardar el usuario en la base de datos
                 User user = userRepository.save(newUser);
                 return UserMapper.toDTO(user);
                
            // }
    }

    @Override
    public Page<User> getAll(int page, int size, Sort sort, String name, String email) throws ServiceLogicException {
            Pageable pageable = PageRequest.of(page, size, sort);
            if (name == "" && email == "") {
                return userRepository.findAll(pageable);
            }
            // Si se proporcionan filtros, usar un método personalizado en el repositorio
            return userRepository.findByNameLikeOrEmailLike(name, email, pageable);
    }
    @Transactional
    @Override
    public UserResponseDto update(UpdateUserRequestDto newUserDetails, Long id) throws NotFoundException, DataInvalidException, ServiceLogicException {
                // Buscar el usuario actual por ID
                User user = userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("User not found with id " + id));

                userValidator.validateUniqueEmail(id, newUserDetails.email());
                userValidator.validateUniqueUsername(id, newUserDetails.username());

                
                // Map para almacenar errores de validación
                // Map<String, Map<String, String>> errors = new HashMap<>();
        
                // Verificar si el correo ha cambiado y si ya existe otro usuario con ese correo
                // if (!user.getEmail().equals(newUserDetails.email()) &&
                //     userRepository.findByEmailAndIdNot(newUserDetails.email(), id) != null) {
                //     Map<String, String> emailError = new HashMap<>();
                //     emailError.put("message", "Registration failed: User already exists with email " + newUserDetails.email());
                //     errors.put("email", emailError);
                // }
        
                // Verificar si el nombre de usuario ha cambiado y si ya existe otro usuario con ese nombre
                // if (!user.getUsername().equals(newUserDetails.username()) &&
                //     userRepository.findByUsernameAndIdNot(newUserDetails.username(), id) != null) {
                //     Map<String, String> usernameError = new HashMap<>();
                //     usernameError.put("message", "Registration failed: User already exists with username " + newUserDetails.username());
                //     errors.put("username", usernameError);
                // }
        
                // Verificar si el rol está presente y es válido
                // if (newUserDetails.getRole() == null || newUserDetails.getRole().getId() == null) {
                //     Map<String, String> roleError = new HashMap<>();
                //     roleError.put("message", "Update failed: Role is required and should not be null.");
                //     errors.put("role", roleError);
                // } else {
                    // Recuperar el rol del repositorio
                Role role = roleRepository.findById(newUserDetails.roleId())
                .orElseThrow(() -> new NotFoundException("Role not found with id " + newUserDetails.roleId())
                // {
                //     Map<String, String> roleError = new HashMap<>();
                //     roleError.put("message", "Role does not exist with id " + newUserDetails.roleId());
                //     errors.put("role", roleError); // Agregar el error al mapa de errores
                //     return new DataInvalidException("Role validation failed", errors); // Lanzar DataInvalidException con los errores
                // }
                );
                    
                    // Si no hay errores de validación, actualizar el rol del usuario
                    // user.setRole(role);
                // }
        
                // Si hay errores de validación, lanzar la excepción personalizada
                // if (!errors.isEmpty()) {
                //     log.debug("Validation errors found: " + errors);
                //     throw new DataInvalidException("Validation failed", errors);
                // }
        
                // Actualizar los detalles del usuario si no hay errores
                user.setName(newUserDetails.name());
                user.setUsername(newUserDetails.username());
                user.setEmail(newUserDetails.email());
                user.setPassword(passwordEncoder.encode(newUserDetails.password()));
                user.setPhone(newUserDetails.phone());
                user.setRole(role);

                return UserMapper.toDTO(userRepository.save(user));
    }
    
    
    @Transactional
    @Override
    public void delete(Long id) throws NotFoundException, ServiceLogicException {
            User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id " + id));
            userRepository.delete(user);   throw new ServiceLogicException("Unexpected error occurred while deleting user");
    }

    @Override
    public UserResponseDto show(Long id) throws NotFoundException {
       User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id " + id));
        return UserMapper.toDTO(user);
    }@Transactional
    @Override
    public UserResponseDto changeStatus(Long id) throws NotFoundException, ServiceLogicException {
        // Obtener el usuario o lanzar una excepción si no se encuentra
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    
        // Cambiar el estado de 'activo' a lo contrario
        user.setActive(!user.isActive());
    
        // No es necesario guardar de nuevo explícitamente, ya que el cambio se persistirá automáticamente al final de la transacción.
        return UserMapper.toDTO(user);  // Devolver el DTO directamente
    }
    
}
