package com.azael.taskapp.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azael.taskapp.exceptions.NotFoundException;
import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.helper.ApiResponseHelper;
import com.azael.taskapp.helper.HateoasHelper;
import com.azael.taskapp.persistence.dto.request.task.UpdateTaskRequestDto;
import com.azael.taskapp.persistence.dto.request.task.CreateTaskRequestDto;
import com.azael.taskapp.persistence.dto.request.task.TaskStatusChangeRequest;
import com.azael.taskapp.persistence.dto.response.ApiResponseDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseStatus;
import com.azael.taskapp.persistence.dto.response.task.TaskResponseDto;
import com.azael.taskapp.persistence.entities.Task;
import com.azael.taskapp.persistence.mappers.TaskMapper;
import com.azael.taskapp.services.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Task Management", description = "Task-related operations")
public class TaskController {
        @Autowired
        private TaskService taskService;

        @Operation(summary = "Create a new task", description = "Creates a new task in the system by providing valid task details.", tags = {
                        "Task Management" })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Task successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"Task successfully created!\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"id\": 1,\n" +
                                        "    \"name\": \"Complete project documentation\",\n" +
                                        "    \"description\": \"Write detailed API documentation for the project.\",\n"
                                        +
                                        "    \"status\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"PENDING\"\n" +
                                        "    },\n" +
                                        "    \"user\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"John Doe\",\n" +
                                        "      \"username\": \"johndoe\",\n" +
                                        "      \"email\": \"john.doe@example.com\",\n" +
                                        "      \"phone\": \"1234567890\",\n" +
                                        "      \"active\": true,\n" +
                                        "      \"role\": {\n" +
                                        "        \"id\": 1,\n" +
                                        "        \"name\": \"USER\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "400", description = "Validation failed or invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Validation failed\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"errors\": {\n" +
                                        "      \"name\": {\n" +
                                        "        \"message\": \"Task name is required\"\n" +
                                        "      },\n" +
                                        "      \"description\": {\n" +
                                        "        \"message\": \"Description must not be empty\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @PostMapping({ "/", "" })
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ApiResponseDto<TaskResponseDto>> create(
                        @Parameter(description = "Details of the task to be created", required = true, schema = @Schema(implementation = CreateTaskRequestDto.class)) @Valid @RequestBody CreateTaskRequestDto taskDetailsRequestDto) {
                TaskResponseDto newTask = taskService.create(taskDetailsRequestDto);
                return ApiResponseHelper.createResponse(HttpStatus.CREATED,
                                "New user account has been successfully created!",
                                ApiResponseStatus.SUCCESS, newTask);
        }

        @Operation(summary = "Get all tasks with pagination and filtering", description = "Retrieves a paginated list of tasks. Supports filtering by name and sorting by any field.", tags = {
                        "Task Management" }, security = @SecurityRequirement(name = "bearer-key"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"Task data successfully obtained\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"_embedded\": {\n" +
                                        "      \"tasks\": [\n" +
                                        "        {\n" +
                                        "          \"id\": 1,\n" +
                                        "          \"name\": \"Complete project documentation\",\n" +
                                        "          \"description\": \"Write detailed API documentation for the project.\",\n"
                                        +
                                        "          \"status\": {\n" +
                                        "            \"id\": 1,\n" +
                                        "            \"name\": \"PENDING\"\n" +
                                        "          },\n" +
                                        "          \"user\": {\n" +
                                        "            \"id\": 1,\n" +
                                        "            \"name\": \"John Doe\",\n" +
                                        "            \"username\": \"johndoe\",\n" +
                                        "            \"email\": \"john.doe@example.com\",\n" +
                                        "            \"phone\": \"1234567890\",\n" +
                                        "            \"active\": true,\n" +
                                        "            \"role\": {\n" +
                                        "              \"id\": 1,\n" +
                                        "              \"name\": \"USER\"\n" +
                                        "            }\n" +
                                        "          }\n" +
                                        "        },\n" +
                                        "        {\n" +
                                        "          \"id\": 2,\n" +
                                        "          \"name\": \"Fix bugs in the login module\",\n" +
                                        "          \"description\": \"Resolve all reported issues in the login functionality.\",\n"
                                        +
                                        "          \"status\": {\n" +
                                        "            \"id\": 2,\n" +
                                        "            \"name\": \"IN_PROGRESS\"\n" +
                                        "          },\n" +
                                        "          \"user\": {\n" +
                                        "            \"id\": 2,\n" +
                                        "            \"name\": \"Jane Smith\",\n" +
                                        "            \"username\": \"janesmith\",\n" +
                                        "            \"email\": \"jane.smith@example.com\",\n" +
                                        "            \"phone\": \"9876543210\",\n" +
                                        "            \"active\": true,\n" +
                                        "            \"role\": {\n" +
                                        "              \"id\": 2,\n" +
                                        "              \"name\": \"ADMIN\"\n" +
                                        "            }\n" +
                                        "          }\n" +
                                        "        }\n" +
                                        "      ]\n" +
                                        "    },\n" +
                                        "    \"page\": {\n" +
                                        "      \"size\": 10,\n" +
                                        "      \"totalElements\": 2,\n" +
                                        "      \"totalPages\": 1,\n" +
                                        "      \"number\": 0\n" +
                                        "    },\n" +
                                        "    \"links\": {\n" +
                                        "      \"self\": {\n" +
                                        "        \"href\": \"http://localhost:8080/api/tasks?page=0&size=10&sortBy=id&direction=asc\"\n"
                                        +
                                        "      },\n" +
                                        "      \"first\": {\n" +
                                        "        \"href\": \"http://localhost:8080/api/tasks?page=0&size=10&sortBy=id&direction=asc\"\n"
                                        +
                                        "      },\n" +
                                        "      \"last\": {\n" +
                                        "        \"href\": \"http://localhost:8080/api/tasks?page=0&size=10&sortBy=id&direction=asc\"\n"
                                        +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Validation failed\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"errors\": {\n" +
                                        "      \"sortBy\": {\n" +
                                        "        \"message\": \"Invalid sort field\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unauthorized access\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @GetMapping({ "/", "" })
        @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
        public ResponseEntity<ApiResponseDto<PagedModel<TaskResponseDto>>> getAll(
                        @Parameter(description = "Page number (default: 0)", example = "0") @RequestParam(defaultValue = "0") Integer page,

                        @Parameter(description = "Number of items per page (default: 10)", example = "10") @RequestParam(defaultValue = "10") Integer size,

                        @Parameter(description = "Filter tasks by name (optional)", example = "Complete project documentation") @RequestParam(defaultValue = "") String name,

                        @Parameter(description = "Field to sort by (default: id)", example = "id") @RequestParam(defaultValue = "id") String sortBy,

                        @Parameter(description = "Sort direction (asc or desc, default: asc)", example = "asc") @RequestParam(defaultValue = "asc") String direction) {
                // Validar la dirección de ordenamiento
                Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                                ? Sort.Direction.DESC
                                : Sort.Direction.ASC;

                // Crear el objeto Sort para el ordenamiento
                Sort sort = Sort.by(sortDirection, sortBy);

                // Obtener la página de tareas
                Page<Task> tasksPage = taskService.getAll(page, size, sort, name);

                // Mapear las tareas a DTO y agregar enlaces HATEOAS
                List<TaskResponseDto> taskResources = tasksPage.getContent().stream()
                                .map(task -> {
                                        TaskResponseDto dto = TaskMapper.toDTO(task);
                                        // addHateoasLinks(dto, task, page, size, name, sortBy, direction);
                                        return dto;
                                })
                                .collect(Collectors.toList());

                // Crear el modelo paginado con metadatos
                PagedModel<TaskResponseDto> pagedModel = PagedModel.of(
                                taskResources,
                                new PagedModel.PageMetadata(
                                                tasksPage.getSize(),
                                                tasksPage.getNumber(),
                                                tasksPage.getTotalElements(),
                                                tasksPage.getTotalPages()));

                // Agregar enlaces HATEOAS al modelo paginado
                addPaginationLinks(pagedModel, tasksPage, page, size, sortBy, direction, name);

                // Devolver la respuesta
                return ApiResponseHelper.createResponse(
                                HttpStatus.OK,
                                "Task data successfully obtained",
                                ApiResponseStatus.SUCCESS,
                                pagedModel);
        }

        // Método auxiliar para agregar enlaces de paginación al modelo paginado
        private void addPaginationLinks(PagedModel<TaskResponseDto> pagedModel, Page<Task> tasksPage, int page,
                        int size,
                        String sortBy, String direction, String name) {
                pagedModel.add(HateoasHelper.createLink(
                                this,
                                "getAll",
                                "self",
                                page, size, name, sortBy, direction));

                if (tasksPage.hasNext()) {
                        pagedModel.add(HateoasHelper.createLink(
                                        this,
                                        "getAll",
                                        "next",
                                        page + 1, size, name, sortBy, direction));
                }

                if (tasksPage.hasPrevious()) {
                        pagedModel.add(HateoasHelper.createLink(
                                        this,
                                        "getAll",
                                        "previous",
                                        page - 1, size, name, sortBy, direction));
                }

                pagedModel.add(HateoasHelper.createLink(
                                this,
                                "getAll",
                                "first",
                                0, size, name, sortBy, direction));

                pagedModel.add(HateoasHelper.createLink(
                                this,
                                "getAll",
                                "last",
                                tasksPage.getTotalPages() - 1, size, name, sortBy, direction));
        }

        @Operation(summary = "Get task details by ID", description = "Retrieves the details of a specific task by providing its ID. Requires a valid JWT token with ADMIN or USER role.", tags = {
                        "Task Management" }, security = @SecurityRequirement(name = "bearer-key"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Task retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"Task data successfully obtained\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"id\": 1,\n" +
                                        "    \"name\": \"Complete project documentation\",\n" +
                                        "    \"description\": \"Write detailed API documentation for the project.\",\n"
                                        +
                                        "    \"status\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"PENDING\"\n" +
                                        "    },\n" +
                                        "    \"user\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"John Doe\",\n" +
                                        "      \"username\": \"johndoe\",\n" +
                                        "      \"email\": \"john.doe@example.com\",\n" +
                                        "      \"phone\": \"1234567890\",\n" +
                                        "      \"active\": true,\n" +
                                        "      \"role\": {\n" +
                                        "        \"id\": 1,\n" +
                                        "        \"name\": \"USER\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Validation failed\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"errors\": {\n" +
                                        "      \"id\": {\n" +
                                        "        \"message\": \"Invalid task ID\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unauthorized access\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Access forbidden\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Task with ID 1 not found\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @GetMapping("/show/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
        public ResponseEntity<ApiResponseDto<TaskResponseDto>> show(
                        @Parameter(description = "ID of the task to retrieve", required = true, example = "1") @PathVariable Long id)
                        throws NotFoundException, ServiceLogicException {
                TaskResponseDto task = taskService.show(id);
                return ApiResponseHelper.createResponse(HttpStatus.OK, "Task data successfully obtained",
                                ApiResponseStatus.SUCCESS, task);

        }

        @Operation(summary = "Update an existing task", description = "Updates the details of an existing task by providing valid task data and the task's ID. Requires a valid JWT token with USER role.", tags = {
                        "Task Management" }, security = @SecurityRequirement(name = "bearer-key"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Task updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"Task account updated successfully!\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"id\": 1,\n" +
                                        "    \"name\": \"Complete project documentation (Updated)\",\n" +
                                        "    \"description\": \"Write detailed API documentation for the project (Updated).\",\n"
                                        +
                                        "    \"status\": {\n" +
                                        "      \"id\": 2,\n" +
                                        "      \"name\": \"IN_PROGRESS\"\n" +
                                        "    },\n" +
                                        "    \"user\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"John Doe\",\n" +
                                        "      \"username\": \"johndoe\",\n" +
                                        "      \"email\": \"john.doe@example.com\",\n" +
                                        "      \"phone\": \"1234567890\",\n" +
                                        "      \"active\": true,\n" +
                                        "      \"role\": {\n" +
                                        "        \"id\": 1,\n" +
                                        "        \"name\": \"USER\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "400", description = "Validation failed or invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Validation failed\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"errors\": {\n" +
                                        "      \"name\": {\n" +
                                        "        \"message\": \"Task name is required\"\n" +
                                        "      },\n" +
                                        "      \"description\": {\n" +
                                        "        \"message\": \"Description must not be empty\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unauthorized access\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Access forbidden\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Task with ID 1 not found\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @PutMapping("/update/{id}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ApiResponseDto<TaskResponseDto>> update(
                        @Parameter(description = "ID of the task to be updated", required = true, example = "1") @PathVariable Long id,

                        @Parameter(description = "Updated details of the task", required = true, schema = @Schema(implementation = UpdateTaskRequestDto.class)) @Valid @RequestBody UpdateTaskRequestDto taskDetailsRequestDto) {
                TaskResponseDto updatedTask = taskService.update(taskDetailsRequestDto, id);
                return ApiResponseHelper.createResponse(HttpStatus.OK, "Task account updated successfully!",
                                ApiResponseStatus.SUCCESS, updatedTask);
        }

        @Operation(summary = "Delete a task by ID", description = "Deletes an existing task by providing its ID. Requires a valid JWT token with USER role.", tags = {
                        "Task Management" }, security = @SecurityRequirement(name = "bearer-key"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Task deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"Task account deleted successfully!\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unauthorized access\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Access forbidden\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Task with ID 1 not found\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @DeleteMapping("/delete/{id}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ApiResponseDto<Void>> delete(
                        @Parameter(description = "ID of the task to be deleted", required = true, example = "1") @PathVariable Long id) {
                taskService.delete(id);
                return ApiResponseHelper.createResponse(HttpStatus.OK, "Task account deleted successfully!",
                                ApiResponseStatus.SUCCESS, null);
        }

        @Operation(summary = "Update the status of a task", description = "Updates the status of an existing task by providing its ID and the new status ID. Requires a valid JWT token with USER role.", tags = {
                        "Task Management" }, security = @SecurityRequirement(name = "bearer-key") 
        )
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Task status updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"Task account updated successfully!\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"id\": 1,\n" +
                                        "    \"name\": \"Complete project documentation\",\n" +
                                        "    \"description\": \"Write detailed API documentation for the project.\",\n"
                                        +
                                        "    \"status\": {\n" +
                                        "      \"id\": 3,\n" +
                                        "      \"name\": \"COMPLETED\"\n" +
                                        "    },\n" +
                                        "    \"user\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"John Doe\",\n" +
                                        "      \"username\": \"johndoe\",\n" +
                                        "      \"email\": \"john.doe@example.com\",\n" +
                                        "      \"phone\": \"1234567890\",\n" +
                                        "      \"active\": true,\n" +
                                        "      \"role\": {\n" +
                                        "        \"id\": 1,\n" +
                                        "        \"name\": \"USER\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "400", description = "Validation failed or invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Validation failed\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"errors\": {\n" +
                                        "      \"statusId\": {\n" +
                                        "        \"message\": \"Invalid status ID\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unauthorized access\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Access forbidden\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Task with ID 1 not found\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @PutMapping("/{taskId}/status")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ApiResponseDto<TaskResponseDto>> update(
                        @Parameter(description = "ID of the task to be updated", required = true, example = "1") @PathVariable Long taskId,

                        @Parameter(description = "New status ID for the task", required = true, schema = @Schema(implementation = TaskStatusChangeRequest.class)) @Valid @RequestBody TaskStatusChangeRequest statusChangeRequest) {
                TaskResponseDto updatedTask = taskService.changeStatus(taskId, statusChangeRequest.statusId());
                return ApiResponseHelper.createResponse(HttpStatus.OK, "Task account updated successfully!",
                                ApiResponseStatus.SUCCESS, updatedTask);
        }
}
