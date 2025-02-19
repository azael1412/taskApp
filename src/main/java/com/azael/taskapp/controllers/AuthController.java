package com.azael.taskapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azael.taskapp.helper.ApiResponseHelper;
import com.azael.taskapp.persistence.dto.request.auth.LoginRequestDto;
import com.azael.taskapp.persistence.dto.request.auth.RefreshTokenRequestDto;
import com.azael.taskapp.persistence.dto.request.auth.RegisterRequestDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseStatus;
import com.azael.taskapp.persistence.dto.response.auth.LoginResponseDto;
import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;
import com.azael.taskapp.persistence.entities.User;
import com.azael.taskapp.persistence.mappers.UserMapper;
import com.azael.taskapp.services.AuthService;

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
@RequestMapping("/auth")
@Tag(name = "Auth Management", description = "Auth-related operations")
public class AuthController {
    @Autowired
    private AuthService authService;

   
    @Operation(summary = "User login", description = "Authenticates a user by providing valid credentials (username and password). Returns a JWT token and user details upon successful login.", tags = {
            "Authentication" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"SUCCESS\",\n" +
                    "  \"message\": \"Session successfully logged in!\",\n" +
                    "  \"results\": {\n" +
                    "    \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\",\n" +
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
            @ApiResponse(responseCode = "400", description = "Invalid credentials or validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"FAIL\",\n" +
                    "  \"message\": \"Invalid username or password\",\n" +
                    "  \"results\": null\n" +
                    "}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"FAIL\",\n" +
                    "  \"message\": \"Unexpected error occurred\",\n" +
                    "  \"results\": null\n" +
                    "}")))
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(
            @Parameter(description = "User credentials for login", required = true, schema = @Schema(implementation = LoginRequestDto.class)) @Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ApiResponseHelper.createResponse(HttpStatus.OK, "Session successfully logged in!",
                ApiResponseStatus.SUCCESS,
                response);
    }

    
    @Operation(summary = "Register a new user", description = "Registers a new user in the system by providing valid user details.", tags = {
            "Authentication" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
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
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> register(
            @Parameter(description = "Details of the user to be registered", required = true, schema = @Schema(implementation = RegisterRequestDto.class)) @Valid @RequestBody RegisterRequestDto request) {
        UserResponseDto newUser = authService.register(request);
        // userDto.add(HateoasHelper.createLink(this, "show","self", newUser.getId()));
        return ApiResponseHelper.createResponse(HttpStatus.CREATED, "New user account has been successfully created!",
                ApiResponseStatus.SUCCESS, newUser);
    }

  
    @Operation(summary = "Get authenticated user details", description = "Retrieves the details of the currently authenticated user. Requires a valid JWT token with ADMIN or USER role.", tags = {
            "Authentication" }, security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User data retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
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
            @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"FAIL\",\n" +
                    "  \"message\": \"Unexpected error occurred\",\n" +
                    "  \"results\": null\n" +
                    "}")))
    })
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> authenticatedUser() {
        User user = authService.me();

        // userDto.add(HateoasHelper.createLink(this, "show","self", user.getId()));
        return ApiResponseHelper.createResponse(HttpStatus.OK, "User data successfully obtained",
                ApiResponseStatus.SUCCESS,
                UserMapper.toDTO(user));
    }

    
    @Operation(summary = "User logout", description = "Closes the session of the currently authenticated user by invalidating their JWT token. Requires a valid Bearer token.", tags = {
            "Authentication" }, security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"SUCCESS\",\n" +
                    "  \"message\": \"Session successfully closed!\",\n" +
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
            @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"FAIL\",\n" +
                    "  \"message\": \"Unexpected error occurred\",\n" +
                    "  \"results\": null\n" +
                    "}")))
    })
    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ApiResponseDto<Void>> logout(
            @Parameter(description = "Authorization header containing the JWT token (format: Bearer <token>)", required = true, example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") @RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ApiResponseHelper.createResponse(HttpStatus.OK, "Session successfully closed!",
                ApiResponseStatus.SUCCESS,
                null);
    }

    @Operation(summary = "Refresh JWT token", description = "Updates the JWT token by providing a valid refresh token and the current Authorization header. Requires a valid Bearer token.", tags = {
            "Authentication" }, security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"SUCCESS\",\n" +
                    "  \"message\": \"Token successfully updated!\",\n" +
                    "  \"results\": {\n" +
                    "    \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\",\n" +
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
            @ApiResponse(responseCode = "400", description = "Invalid refresh token or validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"FAIL\",\n" +
                    "  \"message\": \"Invalid refresh token\",\n" +
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
            @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"FAIL\",\n" +
                    "  \"message\": \"Unexpected error occurred\",\n" +
                    "  \"results\": null\n" +
                    "}")))
    })
    @PostMapping("/refresh-token")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> refreshToken(
            @Parameter(description = "Authorization header containing the current JWT token (format: Bearer <token>)", required = true, example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Refresh token request body containing the refresh token", required = true, schema = @Schema(implementation = RefreshTokenRequestDto.class)) @Valid @RequestBody RefreshTokenRequestDto request) {
        LoginResponseDto resp = authService.refreshToken(authHeader, request);
        return ApiResponseHelper.createResponse(HttpStatus.OK, "Token successfully updated!", ApiResponseStatus.SUCCESS,
                resp);
    }
}
