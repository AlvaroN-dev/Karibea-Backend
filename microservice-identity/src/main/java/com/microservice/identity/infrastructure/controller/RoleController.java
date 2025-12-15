package com.microservice.identity.infrastructure.controller;

import com.microservice.identity.application.dto.request.RoleRequest;
import com.microservice.identity.application.dto.response.RoleResponse;
import com.microservice.identity.application.mapper.RoleMapper;
import com.microservice.identity.application.services.RoleApplicationService;
import com.microservice.identity.domain.models.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Role management operations.
 * Follows hexagonal architecture and implements best practices.
 * Roles use soft delete pattern to maintain referential integrity.
 */
@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Role management endpoints")
public class RoleController {

        private final RoleApplicationService roleApplicationService;

        public RoleController(RoleApplicationService roleApplicationService) {
                this.roleApplicationService = roleApplicationService;
        }

        @Operation(summary = "Create a new role", description = "Creates a new role with the specified name and description. "
                        +
                        "Role names must be unique across the system.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Role successfully created", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., empty name)"),
                        @ApiResponse(responseCode = "409", description = "Role with the same name already exists")
        })
        @PostMapping
        public ResponseEntity<RoleResponse> createRole(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Role creation data", required = true) @Valid @RequestBody RoleRequest request) {

                Role role = roleApplicationService.createRole(request.getName(), request.getDescription());
                return ResponseEntity.status(HttpStatus.CREATED).body(RoleMapper.toResponse(role));
        }

        @Operation(summary = "Get role by ID", description = "Retrieves a role by its unique identifier")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Role found", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Role not found")
        })
        @GetMapping("/{id}")
        public ResponseEntity<RoleResponse> getRoleById(
                        @Parameter(description = "Role unique identifier (UUID)", required = true) @PathVariable UUID id) {

                Role role = roleApplicationService.getRoleById(id)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                return ResponseEntity.ok(RoleMapper.toResponse(role));
        }

        @Operation(summary = "Get role by name", description = "Retrieves a role by its name. Case-sensitive search.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Role found", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Role not found")
        })
        @GetMapping("/name/{name}")
        public ResponseEntity<RoleResponse> getRoleByName(
                        @Parameter(description = "Role name to search for", required = true, example = "ADMIN") @PathVariable String name) {

                Role role = roleApplicationService.getRoleByName(name)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                return ResponseEntity.ok(RoleMapper.toResponse(role));
        }

        @Operation(summary = "Get all roles", description = "Retrieves a list of all available roles in the system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "List of roles retrieved successfully", content = @Content(schema = @Schema(implementation = RoleResponse.class)))
        })
        @GetMapping
        public ResponseEntity<List<RoleResponse>> getAllRoles() {
                List<Role> roles = roleApplicationService.getAllRoles();
                return ResponseEntity.ok(RoleMapper.toResponseList(roles));
        }

        @Operation(summary = "Update role", description = "Updates an existing role's information (name and description)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Role updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "404", description = "Role not found"),
                        @ApiResponse(responseCode = "409", description = "Role name already in use by another role")
        })
        @PutMapping("/{id}")
        public ResponseEntity<Void> updateRole(
                        @Parameter(description = "Role ID to update", required = true) @PathVariable UUID id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated role data") @Valid @RequestBody RoleRequest request) {

                roleApplicationService.updateRole(id, request.getName(), request.getDescription());
                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Soft delete role", description = "Performs a soft delete on the role. " +
                        "The role is marked as deleted but retained in the database to maintain referential integrity. "
                        +
                        "Users with this role will retain the association, but the role cannot be assigned to new users. "
                        +
                        "This operation is reversible by database administrators.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Role soft-deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Role not found"),
                        @ApiResponse(responseCode = "409", description = "Role is already deleted or cannot be deleted (e.g., system role)")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRole(
                        @Parameter(description = "Role ID to soft delete", required = true) @PathVariable UUID id) {

                // This performs a SOFT DELETE - role is marked as deleted but not removed from
                // database
                // This maintains referential integrity with user_roles table
                roleApplicationService.deleteRole(id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Assign role to user", description = "Assigns a role to a specific user. " +
                        "Creates a user-role association if it doesn't already exist. " +
                        "Idempotent operation - safe to call multiple times.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Role assigned successfully to user"),
                        @ApiResponse(responseCode = "404", description = "User or role not found"),
                        @ApiResponse(responseCode = "409", description = "User already has this role")
        })
        @PostMapping("/{roleId}/assign/{userId}")
        public ResponseEntity<Void> assignRoleToUser(
                        @Parameter(description = "Role ID to assign", required = true) @PathVariable UUID roleId,
                        @Parameter(description = "User ID to receive the role", required = true) @PathVariable UUID userId) {

                roleApplicationService.assignRoleToUser(userId, roleId);
                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Remove role from user", description = "Removes a role from a specific user. " +
                        "Deletes the user-role association.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Role removed successfully from user"),
                        @ApiResponse(responseCode = "404", description = "User, role, or user-role association not found")
        })
        @DeleteMapping("/{roleId}/remove/{userId}")
        public ResponseEntity<Void> removeRoleFromUser(
                        @Parameter(description = "Role ID to remove", required = true) @PathVariable UUID roleId,
                        @Parameter(description = "User ID to remove the role from", required = true) @PathVariable UUID userId) {

                roleApplicationService.removeRoleFromUser(userId, roleId);
                return ResponseEntity.ok().build();
        }
}
