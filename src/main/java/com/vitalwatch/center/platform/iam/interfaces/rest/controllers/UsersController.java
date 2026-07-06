package com.vitalwatch.center.platform.iam.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.CreateUserResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.UserResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for user operations.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management endpoints")
public class UsersController {

    private final UserJpaRepository userRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersController(
            UserJpaRepository userRepository,
            OrganizationJpaRepository organizationRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
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

    @PostMapping
    @Operation(summary = "Create user")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody CreateUserResource resource
    ) {
        if (userRepository.existsByEmail(resource.email())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict("A user with this email already exists.")
            );
        }

        if (userRepository.existsByUsername(resource.username())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict("A user with this username already exists.")
            );
        }

        var organization = resource.organizationId() == null
                ? null
                : organizationRepository.findById(resource.organizationId()).orElse(null);

        if (resource.organizationId() != null && organization == null) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.notFound("Organization")
            );
        }

        var user = new UserJpaEntity(
                resource.firstName(),
                resource.lastName(),
                resource.username(),
                resource.email(),
                passwordEncoder.encode(resource.password()),
                resource.role(),
                organization
        );

        user.setSpecialtyId(resource.specialtyId());
        user.setWorkAreaId(resource.workAreaId());

        var savedUser = userRepository.save(user);

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(savedUser);

        return ResponseEntity
                .created(URI.create("/users/" + savedUser.getId()))
                .body(userResource);
    }
}