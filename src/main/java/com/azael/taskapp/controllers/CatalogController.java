package com.azael.taskapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azael.taskapp.helper.ApiResponseHelper;
import com.azael.taskapp.persistence.dto.response.ApiResponseDto;
import com.azael.taskapp.persistence.dto.response.ApiResponseStatus;
import com.azael.taskapp.persistence.entities.Role;
import com.azael.taskapp.persistence.entities.Status;
import com.azael.taskapp.services.CatalogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/catalogs")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Catalog", description = "Catalog-related operations")
public class CatalogController {

        @Autowired
        private CatalogService catalogService;

        @Operation(summary = "Get all status", description = "Retrieves a list of all available statuses in the system.", tags = {
                        "Catalog Management" })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Status retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"Status data successfully obtained\",\n" +
                                        "  \"results\": [\n" +
                                        "    {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"name\": \"PENDING\"\n" +
                                        "    },\n" +
                                        "    {\n" +
                                        "      \"id\": 2,\n" +
                                        "      \"name\": \"IN_PROGRESS\"\n" +
                                        "    },\n" +
                                        "    {\n" +
                                        "      \"id\": 3,\n" +
                                        "      \"name\": \"COMPLETED\"\n" +
                                        "    }\n" +
                                        "  ]\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @GetMapping({ "/status", "/status/" })
        public ResponseEntity<ApiResponseDto<List<Status>>> getAllStatus() {
                List<Status> status = catalogService.getAllStatus();
                return ApiResponseHelper.createResponse(
                                HttpStatus.OK,
                                "Status data successfully obtained",
                                ApiResponseStatus.SUCCESS,
                                status);
        }

        @Operation(summary = "Get all roles", description = "Retrieves a list of all available roles in the system.", tags = {
                        "Catalog Management" })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"SUCCESS\",\n" +
                                        "  \"message\": \"Role data successfully obtained\",\n" +
                                        "  \"results\": [\n" +
                                        "    {\n" +
                                        "      \"id\": 1,\n" +
                                        "      \"roleName\": \"USER\"\n" +
                                        "    },\n" +
                                        "    {\n" +
                                        "      \"id\": 2,\n" +
                                        "      \"roleName\": \"ADMIN\"\n" +
                                        "    }\n" +
                                        "  ]\n" +
                                        "}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class), examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"status\": \"FAIL\",\n" +
                                        "  \"message\": \"Unexpected error occurred\",\n" +
                                        "  \"results\": null\n" +
                                        "}")))
        })
        @GetMapping({ "/roles", "/roles/" })
        public ResponseEntity<ApiResponseDto<List<Role>>> getAllRoles() {
                List<Role> roles = catalogService.getAllRoles();
                return ApiResponseHelper.createResponse(
                                HttpStatus.OK,
                                "Role data successfully obtained",
                                ApiResponseStatus.SUCCESS,
                                roles);
        }
}
