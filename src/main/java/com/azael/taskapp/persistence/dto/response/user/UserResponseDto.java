package com.azael.taskapp.persistence.dto.response.user;

import com.azael.taskapp.persistence.dto.response.role.RoleResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// import org.springframework.hateoas.RepresentationModel;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto //extends RepresentationModel<UserResponseDto>   {
{   private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    // private String password;
    private boolean isActive;
    private RoleResponseDto role;
}
