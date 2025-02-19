package com.azael.taskapp.persistence.dto.request.task;

import com.azael.taskapp.validation.status.StatusExistByid;
import com.azael.taskapp.validation.task.create.CreateUniqueTaskNameForUser;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new task")
public record CreateTaskRequestDto(
                @Schema(description = "The name of the task", example = "Task Example", minLength = 5, maxLength = 100) @NotBlank(message = "Name is mandatory") @Size(min = 5, max = 100, message = "Name should be between 5 to 100 characters") @CreateUniqueTaskNameForUser String name,

                @Schema(description = "The detailed description of the task", example = "This task is about creating a new feature for the application.", minLength = 10, maxLength = 1000) @NotBlank(message = "Description is mandatory") @Size(min = 10, max = 1000, message = "Description should be between 10 to 1000 characters") String description,

                @Schema(description = "The ID of the status associated with the task", example = "1") @NotNull(message = "Status is mandatory") @StatusExistByid Long statusId

// Uncomment and update if user association is needed
// @NotNull(message = "User is mandatory")
// Long userId
) {
}
