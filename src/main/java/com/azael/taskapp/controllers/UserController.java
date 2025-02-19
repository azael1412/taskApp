package com.azael.taskapp.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
// import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.exceptions.NotFoundException;
import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.helper.ApiResponseHelper;
import com.azael.taskapp.helper.HateoasHelper;
import com.azael.taskapp.persistence.dto.request.user.CreateUserRequestDto;
import com.azael.taskapp.persistence.dto.request.user.UpdateUserRequestDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseStatus;
import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;
import com.azael.taskapp.persistence.entities.User;
import com.azael.taskapp.persistence.mappers.UserMapper;
import com.azael.taskapp.services.UserService;

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
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "User Management", description = "User-related operations")
public class UserController {

        @Autowired
        private UserService userService;
        
        @Operation(summary = "Get all users with pagination and filtering", description = "Retrieves a paginated list of users. Supports filtering by name and email, and sorting by any field.", tags = {
                        "User Management" })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"User data successfully obtained\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"content\": {\n" +
                                        "      \"users\": [\n" +
                                        "        {\n" +
                                        "          \"id\": 1,\n" +
                                        "          \"name\": \"John Doe\",\n" +
                                        "          \"username\": \"johndoe\",\n" +
                                        "          \"email\": \"john.doe@example.com\",\n" +
                                        "          \"phone\": \"1234567890\",\n" +
                                        "          \"active\": true,\n" +
                                        "          \"role\": {\n" +
                                        "            \"id\": 1,\n" +
                                        "            \"name\": \"USER\"\n" +
                                        "          }\n" +
                                        "        },\n" +
                                        "        {\n" +
                                        "          \"id\": 2,\n" +
                                        "          \"name\": \"Jane Smith\",\n" +
                                        "          \"username\": \"janesmith\",\n" +
                                        "          \"email\": \"jane.smith@example.com\",\n" +
                                        "          \"phone\": \"9876543210\",\n" +
                                        "          \"active\": true,\n" +
                                        "          \"role\": {\n" +
                                        "            \"id\": 2,\n" +
                                        "            \"name\": \"ADMIN\"\n" +
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
                                        "        \"href\": \"http://localhost:8080/api/users?page=0&size=10&sortBy=id&direction=asc\"\n"
                                        +
                                        "      },\n" +
                                        "      \"first\": {\n" +
                                        "        \"href\": \"http://localhost:8080/api/users?page=0&size=10&sortBy=id&direction=asc\"\n"
                                        +
                                        "      },\n" +
                                        "      \"last\": {\n" +
                                        "        \"href\": \"http://localhost:8080/api/users?page=0&size=10&sortBy=id&direction=asc\"\n"
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
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @GetMapping({ "/", "" })
        public ResponseEntity<ApiResponseDto<PagedModel<UserResponseDto>>> getAll(
                        @Parameter(description = "Page number (default: 0)", example = "0") @RequestParam(defaultValue = "0") Integer page,

                        @Parameter(description = "Number of items per page (default: 10)", example = "10") @RequestParam(defaultValue = "10") Integer size,

                        @Parameter(description = "Filter users by name (optional)", example = "John") @RequestParam(defaultValue = "") String name,

                        @Parameter(description = "Filter users by email (optional)", example = "john.doe@example.com") @RequestParam(defaultValue = "") String email,

                        @Parameter(description = "Field to sort by (default: id)", example = "id") @RequestParam(defaultValue = "id") String sortBy,

                        @Parameter(description = "Sort direction (asc or desc, default: asc)", example = "asc") @RequestParam(defaultValue = "asc") String direction) {
                // Validar la dirección de ordenamiento
                Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                                ? Sort.Direction.DESC
                                : Sort.Direction.ASC;

                // Crear el objeto Sort para el ordenamiento
                Sort sort = Sort.by(sortDirection, sortBy);

                // Obtener la página de usuarios
                Page<User> usersPage = userService.getAll(page, size, sort, name, email);

                // Mapear los usuarios a DTO y agregar enlaces HATEOAS
                List<UserResponseDto> userResources = usersPage.getContent().stream()
                                .map(user -> UserMapper.toDTO(user))
                                // addHateoasLinks(UserMapper.toDTO(user), page, size, name, email, sortBy,
                                // direction))
                                .collect(Collectors.toList());

                // Crear el modelo paginado con metadatos
                PagedModel<UserResponseDto> pagedModel = PagedModel.of(
                                userResources,
                                new PagedModel.PageMetadata(
                                                usersPage.getSize(),
                                                usersPage.getNumber(),
                                                usersPage.getTotalElements(),
                                                usersPage.getTotalPages()));

                // Agregar enlaces de paginación al modelo paginado
                addPaginationLinks(pagedModel, usersPage, page, size, sortBy, direction, name, email);

                // Devolver la respuesta
                return ApiResponseHelper.createResponse(
                                HttpStatus.OK,
                                "User data successfully obtained",
                                ApiResponseStatus.SUCCESS,
                                pagedModel);
        }
        // Método auxiliar para agregar enlaces de paginación al modelo paginado
        private void addPaginationLinks(PagedModel<UserResponseDto> pagedModel, Page<User> usersPage, int page,
                        int size,
                        String sortBy, String direction, String name, String email) {
                pagedModel.add(HateoasHelper.createLink(
                                this,
                                "getAll",
                                "self",
                                page, size, name, email, sortBy, direction));

                if (usersPage.hasNext()) {
                        pagedModel.add(HateoasHelper.createLink(
                                        this,
                                        "getAll",
                                        "next",
                                        page + 1, size, name, email, sortBy, direction));
                }

                if (usersPage.hasPrevious()) {
                        pagedModel.add(HateoasHelper.createLink(
                                        this,
                                        "getAll",
                                        "previous",
                                        page - 1, size, name, email, sortBy, direction));
                }

                pagedModel.add(HateoasHelper.createLink(
                                this,
                                "getAll",
                                "first",
                                0, size, name, email, sortBy, direction));

                pagedModel.add(HateoasHelper.createLink(
                                this,
                                "getAll",
                                "last",
                                usersPage.getTotalPages() - 1, size, name, email, sortBy, direction));
        }

        @Operation(summary = "Get user details by ID", description = "Retrieves the details of a specific user by providing their ID.", tags = {
                        "User Management" })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"User data successfully obtained\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"id\": 1,\n" +
                                        "    \"name\": \"John Doe\",\n" +
                                        "    \"username\": \"johndoe\",\n" +
                                        "    \"email\": \"john.doe@example.com\",\n" +
                                        "    \"phone\": \"1234567890\",\n" +
                                        "    \"active\": true,\n" +
                                        "    \"role\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"USER\"\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"User with ID 1 not found\",\n" +
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
        public ResponseEntity<ApiResponseDto<UserResponseDto>> show(
                        @Parameter(description = "ID of the user to retrieve", required = true, example = "1") @PathVariable Long id)
                        throws NotFoundException, ServiceLogicException {
                UserResponseDto user = userService.show(id);

                // Devolver la respuesta con el DTO y los enlaces HATEOAS
                return ApiResponseHelper.createResponse(
                                HttpStatus.OK,
                                "User data successfully obtained",
                                ApiResponseStatus.SUCCESS,
                                user);
        }

       
        @Operation(summary = "Create a new user", description = "Registers a new user in the system by providing valid user details.", tags = {
                        "User Management" })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"New user account has been successfully created!\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"id\": 1,\n" +
                                        "    \"name\": \"John Doe\",\n" +
                                        "    \"username\": \"johndoe\",\n" +
                                        "    \"email\": \"john.doe@example.com\",\n" +
                                        "    \"phone\": \"1234567890\",\n" +
                                        "    \"active\": true,\n" +
                                        "    \"role\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"USER\"\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "400", description = "Validation failed or invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Validation failed\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"errors\": {\n" +
                                        "      \"email\": {\n" +
                                        "        \"message\": \"E-Mail is already in use\"\n" +
                                        "      },\n" +
                                        "      \"username\": {\n" +
                                        "        \"message\": \"Username is already in use\"\n" +
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
        public ResponseEntity<ApiResponseDto<UserResponseDto>> register(
                        @Parameter(description = "Details of the user to be created", required = true, schema = @Schema(implementation = CreateUserRequestDto.class)) @Valid @RequestBody CreateUserRequestDto userDetailsRequestDto)
                        throws DataInvalidException {
                UserResponseDto newUser = userService.register(userDetailsRequestDto);
                // UserResponseDto userDto = UserMapper.toDTO(newUser);
                // userDto.add(HateoasHelper.createLink(this, "show","self", newUser.getId()));
                return ApiResponseHelper.createResponse(HttpStatus.CREATED,
                                "New user account has been successfully created!",
                                ApiResponseStatus.SUCCESS, newUser);
        }

        
        @Operation(summary = "Update an existing user", description = "Updates the details of an existing user by providing valid user data and the user's ID.", tags = {
                        "User Management" })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"User account updated successfully!\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"id\": 1,\n" +
                                        "    \"name\": \"John Doe Updated\",\n" +
                                        "    \"username\": \"johndoe_updated\",\n" +
                                        "    \"email\": \"john.doe.updated@example.com\",\n" +
                                        "    \"phone\": \"1234567890\",\n" +
                                        "    \"active\": true,\n" +
                                        "    \"role\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"USER\"\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data or validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Validation failed\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"errors\": {\n" +
                                        "      \"email\": {\n" +
                                        "        \"message\": \"E-Mail is already in use\"\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"User with ID 1 not found\",\n" +
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
        public ResponseEntity<ApiResponseDto<UserResponseDto>> update(
                        @Parameter(description = "ID of the user to be updated", required = true, example = "1") @PathVariable Long id,

                        @Parameter(description = "Updated details of the user", required = true, schema = @Schema(implementation = UpdateUserRequestDto.class)) @Valid @RequestBody UpdateUserRequestDto userDetailsRequestDto) {
                UserResponseDto updatedUser = userService.update(userDetailsRequestDto, id);
                // userDto.add(HateoasHelper.createLink(this, "show","self", id));
                return ApiResponseHelper.createResponse(HttpStatus.OK, "User account updated successfully!",
                                ApiResponseStatus.SUCCESS, updatedUser);
        }

        
        @Operation(summary = "Delete an existing user", description = "Deletes an existing user by providing the user's ID.", tags = {
                        "User Management" })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"User account deleted successfully!\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"User with ID 1 not found\",\n" +
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
        public ResponseEntity<ApiResponseDto<Void>> delete(
                        @Parameter(description = "ID of the user to be deleted", required = true, example = "1") @PathVariable Long id) {
                userService.delete(id);
                return ApiResponseHelper.createResponse(HttpStatus.OK, "User account deleted successfully!",
                                ApiResponseStatus.SUCCESS, null);
        }

        
        @Operation(summary = "Update the status of a user", description = "Updates the status (e.g., active/inactive) of an existing user by providing the user's ID.", tags = {
                        "User Management" })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User status updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"Task account updated successfully!\",\n" +
                                        "  \"results\": {\n" +
                                        "    \"id\": 1,\n" +
                                        "    \"name\": \"John Doe\",\n" +
                                        "    \"username\": \"johndoe\",\n" +
                                        "    \"email\": \"john.doe@example.com\",\n" +
                                        "    \"phone\": \"1234567890\",\n" +
                                        "    \"active\": false,\n" +
                                        "    \"role\": {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"USER\"\n" +
                                        "    }\n" +
                                        "  }\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"User with ID 1 not found\",\n" +
                                        "  \"results\": null\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @PatchMapping("/{userId}/status")
        public ResponseEntity<ApiResponseDto<UserResponseDto>> update(
                        @Parameter(description = "ID of the user whose status will be updated", required = true, example = "1") @PathVariable Long userId) {
                UserResponseDto updatedUser = userService.changeStatus(userId);
                // userDto.add(HateoasHelper.createLink(this, "show","self", userId));
                return ApiResponseHelper.createResponse(HttpStatus.OK, "Task account updated successfully!",
                                ApiResponseStatus.SUCCESS, updatedUser);
        }
}
