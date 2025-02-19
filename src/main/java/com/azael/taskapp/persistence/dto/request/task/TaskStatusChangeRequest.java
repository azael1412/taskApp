package com.azael.taskapp.persistence.dto.request.task;

import com.azael.taskapp.validation.status.StatusExistByid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to change the status of a task")
public record TaskStatusChangeRequest(
        @Schema(description = "The ID of the new status to assign to the task", example = "1", allowableValues = {
                "1",
                "2" }) @NotNull(message = "Status is mandatory") @Min(value = 1, message = "Status ID must be at least 1") @Max(value = 2, message = "Status ID must be at most 2") @StatusExistByid Long statusId // assign
    ){
}
