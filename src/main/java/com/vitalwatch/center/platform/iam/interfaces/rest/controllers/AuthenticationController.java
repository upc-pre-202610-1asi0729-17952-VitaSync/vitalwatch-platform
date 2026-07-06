package com.vitalwatch.center.platform.iam.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.application.internal.outboundservices.TokenService;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.SignInResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication endpoints.
 */
@RestController
@RequestMapping("/authentication")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MessageResolver messageResolver;

    public AuthenticationController(
            UserJpaRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            MessageResolver messageResolver
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.messageResolver = messageResolver;
    }

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
}