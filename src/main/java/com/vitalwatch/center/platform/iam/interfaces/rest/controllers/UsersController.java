package com.vitalwatch.center.platform.iam.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.UserResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user read operations.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management endpoints")
public class UsersController {

    private final UserJpaRepository userRepository;

    public UsersController(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Get all users or users by organization id")
    public ResponseEntity<List<UserResource>> getUsers(
            @RequestParam(required = false) Long organizationId
    ) {
        var users = organizationId == null
                ? userRepository.findAll()
                : userRepository.findByOrganization_Id(organizationId);

        var resources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        var user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.notFound("User")
            );
        }

        var resource = UserResourceFromEntityAssembler.toResourceFromEntity(
                user.get()
        );

        return ResponseEntity.ok(resource);
    }
}