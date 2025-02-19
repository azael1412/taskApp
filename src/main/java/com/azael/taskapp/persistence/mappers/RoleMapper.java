package com.azael.taskapp.persistence.mappers;

import com.azael.taskapp.persistence.dto.response.role.RoleResponseDto;
import com.azael.taskapp.persistence.entities.Role;

public class RoleMapper {
     public static RoleResponseDto toDTO(Role role) {
        return new RoleResponseDto(
            role.getId(),
            role.getName()
        );
    }
    public static Role toEntity(RoleResponseDto roleResponseDto) {
        if (roleResponseDto == null) {
            return null; // O lanzar una excepción si prefieres
        }
        // Crea un nuevo objeto Role con los valores del DTO
        Role role = new Role();
        role.setId(roleResponseDto.getId()); // Asegúrate de que roleResponseDto tiene el campo 'id'
        role.setName(roleResponseDto.getName()); // Y que tiene el campo 'name'

        return role;
    }
}
