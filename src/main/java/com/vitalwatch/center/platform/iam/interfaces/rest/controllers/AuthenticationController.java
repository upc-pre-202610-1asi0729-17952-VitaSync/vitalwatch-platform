package com.vitalwatch.center.platform.iam.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.application.internal.outboundservices.TokenService;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.SignInResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.SignUpResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

/**
 * REST controller for authentication endpoints.
 */
@RestController
@RequestMapping("/authentication")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

    private final UserJpaRepository userRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MessageResolver messageResolver;

    public AuthenticationController(
            UserJpaRepository userRepository,
            OrganizationJpaRepository organizationRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            MessageResolver messageResolver
    ) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.messageResolver = messageResolver;
    }

    @SecurityRequirements()
    @PostMapping("/sign-in")
    @Operation(summary = "Sign in with email and password")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody SignInResource resource
    ) {
        var user = userRepository.findByEmail(resource.email());

        if (user.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation(
                            messageResolver.get("iam.authentication.invalidCredentials")
                    )
            );
        }

        var authenticatedUser = user.get();

        if (!passwordEncoder.matches(resource.password(), authenticatedUser.getPasswordHash())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation(
                            messageResolver.get("iam.authentication.invalidCredentials")
                    )
            );
        }

        var token = tokenService.generateToken(authenticatedUser);

        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
                .toResourceFromEntity(authenticatedUser, token);

        return ResponseEntity.ok(authenticatedUserResource);
    }

    @SecurityRequirements()
    @PostMapping("/sign-up")
    @Operation(summary = "Register a new user and return authentication token")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody SignUpResource resource
    ) {
        if (userRepository.existsByEmail(resource.email())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict(
                            messageResolver.get("iam.user.emailConflict")
                    )
            );
        }

        if (userRepository.existsByUsername(resource.username())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict(
                            messageResolver.get("iam.user.usernameConflict")
                    )
            );
        }

        var organization = resource.organizationId() == null
                ? null
                : organizationRepository.findById(resource.organizationId()).orElse(null);

        if (resource.organizationId() != null && organization == null) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.organization.notFound")
                    )
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

        var token = tokenService.generateToken(savedUser);

        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
                .toResourceFromEntity(savedUser, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticatedUserResource);
    }

    @GetMapping("/me")
    @Operation(summary = "Get authenticated user profile")
    public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserJpaEntity principal)) {
            return ErrorResponseAssembler.toResponseEntity(
                    "AUTHENTICATION_REQUIRED",
                    messageResolver.get("iam.authentication.required"),
                    null,
                    HttpStatus.UNAUTHORIZED
            );
        }

        var user = userRepository.findById(principal.getId());

        if (user.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.user.notFound")
                    )
            );
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());

        return ResponseEntity.ok(userResource);
    }
}