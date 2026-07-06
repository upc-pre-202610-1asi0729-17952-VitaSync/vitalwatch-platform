package com.vitalwatch.center.platform.iam.invitations.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.vitalwatch.center.platform.iam.invitations.domain.model.enums.InvitationStatus;
import com.vitalwatch.center.platform.iam.invitations.infrastructure.persistence.jpa.entities.InvitationJpaEntity;
import com.vitalwatch.center.platform.iam.invitations.infrastructure.persistence.jpa.repositories.InvitationJpaRepository;
import com.vitalwatch.center.platform.iam.invitations.interfaces.rest.resources.AcceptInvitationResource;
import com.vitalwatch.center.platform.iam.invitations.interfaces.rest.resources.InvitationResource;
import com.vitalwatch.center.platform.iam.invitations.interfaces.rest.resources.SendInvitationResource;
import com.vitalwatch.center.platform.iam.invitations.interfaces.rest.transform.InvitationResourceFromEntityAssembler;
import com.vitalwatch.center.platform.iam.application.internal.outboundservices.TokenService;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for invitation operations.
 */
@RestController
@RequestMapping("/invitations")
@Tag(name = "Invitations", description = "Organization invitation endpoints")
public class InvitationsController {

    private final InvitationJpaRepository invitationRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MessageResolver messageResolver;

    public InvitationsController(
            InvitationJpaRepository invitationRepository,
            OrganizationJpaRepository organizationRepository,
            UserJpaRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            MessageResolver messageResolver
    ) {
        this.invitationRepository = invitationRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all invitations or invitations by organization id")
    public ResponseEntity<List<InvitationResource>> getInvitations(
            @RequestParam(required = false) Long organizationId
    ) {
        var invitations = organizationId == null
                ? invitationRepository.findAll()
                : invitationRepository.findByOrganization_Id(organizationId);

        var resources = invitations.stream()
                .map(InvitationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{invitationId}")
    @Operation(summary = "Get invitation by id")
    public ResponseEntity<?> getInvitationById(@PathVariable Long invitationId) {
        var invitation = invitationRepository.findById(invitationId);

        if (invitation.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.invitation.notFound")
                    )
            );
        }

        var resource = InvitationResourceFromEntityAssembler.toResourceFromEntity(
                invitation.get()
        );

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/by-token/{token}")
    @Operation(summary = "Get invitation by token")
    public ResponseEntity<?> getInvitationByToken(@PathVariable String token) {
        var invitation = invitationRepository.findByToken(token);

        if (invitation.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.invitation.notFound")
                    )
            );
        }

        var foundInvitation = invitation.get();

        if (foundInvitation.isExpired()) {
            foundInvitation.expire();
            invitationRepository.save(foundInvitation);

            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation(
                            messageResolver.get("iam.invitation.expired")
                    )
            );
        }

        var resource = InvitationResourceFromEntityAssembler.toResourceFromEntity(
                foundInvitation
        );

        return ResponseEntity.ok(resource);
    }

    @PostMapping("/send")
    @Operation(summary = "Send invitation")
    public ResponseEntity<?> sendInvitation(
            @Valid @RequestBody SendInvitationResource resource
    ) {
        var organization = organizationRepository.findById(resource.organizationId());

        if (organization.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.organization.notFound")
                    )
            );
        }

        if (userRepository.existsByEmail(resource.email())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict(
                            messageResolver.get("iam.user.emailConflict")
                    )
            );
        }

        if (invitationRepository.existsByEmailAndOrganization_IdAndStatus(
                resource.email(),
                resource.organizationId(),
                InvitationStatus.SENT
        )) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict(
                            messageResolver.get("iam.invitation.pendingConflict")
                    )
            );
        }

        var token = UUID.randomUUID().toString();
        var expiresAt = LocalDateTime.now().plusDays(7);

        var invitation = new InvitationJpaEntity(
                token,
                resource.email(),
                resource.role(),
                organization.get(),
                resource.specialtyId(),
                resource.workAreaId(),
                expiresAt
        );

        var savedInvitation = invitationRepository.save(invitation);

        var invitationResource = InvitationResourceFromEntityAssembler.toResourceFromEntity(
                savedInvitation
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(invitationResource);
    }

    @PostMapping("/accept")
    @Operation(summary = "Accept invitation and create user account")
    public ResponseEntity<?> acceptInvitation(
            @Valid @RequestBody AcceptInvitationResource resource
    ) {
        var invitation = invitationRepository.findByToken(resource.token());

        if (invitation.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.invitation.notFound")
                    )
            );
        }

        var foundInvitation = invitation.get();

        if (foundInvitation.isExpired()) {
            foundInvitation.expire();
            invitationRepository.save(foundInvitation);

            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation(
                            messageResolver.get("iam.invitation.expired")
                    )
            );
        }

        if (foundInvitation.getStatus() != InvitationStatus.SENT) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation(
                            messageResolver.get("iam.invitation.notAvailable")
                    )
            );
        }

        if (userRepository.existsByEmail(foundInvitation.getEmail())) {
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

        var user = new UserJpaEntity(
                resource.firstName(),
                resource.lastName(),
                resource.username(),
                foundInvitation.getEmail(),
                passwordEncoder.encode(resource.password()),
                foundInvitation.getRole(),
                foundInvitation.getOrganization()
        );

        user.setSpecialtyId(foundInvitation.getSpecialtyId());
        user.setWorkAreaId(foundInvitation.getWorkAreaId());

        var savedUser = userRepository.save(user);

        foundInvitation.accept(savedUser.getId());
        invitationRepository.save(foundInvitation);

        var token = tokenService.generateToken(savedUser);

        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
                .toResourceFromEntity(savedUser, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticatedUserResource);
    }
}