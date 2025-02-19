package com.azael.taskapp.persistence.mappers;

import com.azael.taskapp.persistence.dto.response.role.RoleResponseDto;
import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;
import com.azael.taskapp.persistence.entities.Role;
import com.azael.taskapp.persistence.entities.User;


public class UserMapper {

 // Convertir de la entidad User al DTO UserDTO
 public static UserResponseDto toDTO(User user) {
    RoleResponseDto role = RoleMapper.toDTO(user.getRole());
    return new UserResponseDto(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getEmail(),
        user.getPhone(),
        user.isActive(),
        role
    );
}

    // // Convertir del DTO UserDTO a la entidad User
    public static User toEntity(UserResponseDto userDTO) {
         // Aquí necesitas convertir el RoleResponseDto en un Role
         Role role = RoleMapper.toEntity(userDTO.getRole());

        return new User(
            userDTO.getName(),
            userDTO.getUsername(),
            userDTO.getEmail(),
            userDTO.getPhone(),
            "",
            userDTO.isActive(), 
            role
        );
    }

    
}
