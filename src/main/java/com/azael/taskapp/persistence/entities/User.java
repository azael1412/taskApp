package com.azael.taskapp.persistence.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
}, name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 50, message = "Name should be between 3 to 50 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 20, message = "Name should be between 3 to 20 characters")
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required!")
    @Size(min = 10, max = 10, message = "Phone number must have 10 characters!")
    @Pattern(regexp="^[0-9]*$", message = "Phone number must contain only digits")
    private String phone;

    @NotBlank(message = "Password is mandatory")
    // @Size(min = 6, max = 16, message = "Password should be between 6 to 16 characters")
    @Column(nullable = false)
    private String password;


    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT true", nullable = false)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull(message = "role is mandatory")
    private Role role;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User(
            String name,
            String username,
            String email,
            String phone,
            String password, Boolean isActive, Role role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    public String getPassword() {
        return password;
    }

    // @Override
    public String getUsername() {
        // return email;
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
