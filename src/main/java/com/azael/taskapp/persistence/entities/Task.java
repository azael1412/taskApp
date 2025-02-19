package com.azael.taskapp.persistence.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.azael.taskapp.validation.task.create.CreateUniqueTaskNameForUser;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 50, message = "Name should be between 3 to 50 characters")
    @Column(nullable = false)
    @CreateUniqueTaskNameForUser
    private String name;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 10, max = 1000, message = "Description should be between 3 to 50 characters")
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    @NotNull(message = "status is mandatory")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "user is mandatory")
    private User user;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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

    public Task(
            String name,
            String description,
            Status status,
            User user) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.user = user;
    }

}