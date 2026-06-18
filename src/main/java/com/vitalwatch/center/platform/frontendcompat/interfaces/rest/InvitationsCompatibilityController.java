package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendInvitationResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendInvitationResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.PatchFrontendInvitationResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendInvitationResourceFromEntityAssembler;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendRoleMapper;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;
import com.vitalwatch.center.platform.iam.domain.model.commands.InviteUserCommand;
import com.vitalwatch.center.platform.iam.domain.model.enums.InvitationStatus;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.repositories.HospitalWorkspaceRepository;
import com.vitalwatch.center.platform.iam.domain.repositories.UserAccountRepository;
import com.vitalwatch.center.platform.iam.domain.repositories.UserInvitationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Frontend compatibility controller for invitation endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1/invitations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend API - Invitations", description = "Invitation endpoints compatible with the Angular frontend")
public class InvitationsCompatibilityController {

    private final HospitalWorkspaceRepository hospitalWorkspaceRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserInvitationRepository userInvitationRepository;

    public InvitationsCompatibilityController(
            HospitalWorkspaceRepository hospitalWorkspaceRepository,
            UserAccountRepository userAccountRepository,
            UserInvitationRepository userInvitationRepository
    ) {
        this.hospitalWorkspaceRepository = hospitalWorkspaceRepository;
        this.userAccountRepository = userAccountRepository;
        this.userInvitationRepository = userInvitationRepository;
    }

    @GetMapping
    @Operation(summary = "Get frontend-compatible invitations")
    public ResponseEntity<List<FrontendInvitationResource>> getInvitations(
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId,
            @RequestParam(required = false) String token
    ) {
        if (token != null && !token.isBlank()) {
            var invitation = userInvitationRepository.findByToken(token.trim());

            return ResponseEntity.ok(
                    invitation.stream()
                            .map(FrontendInvitationResourceFromEntityAssembler::toResourceFromEntity)
                            .toList()
            );
        }

        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;

        if (workspaceId == null) {
            return ResponseEntity.ok(List.of());
        }

        var invitations = userInvitationRepository.findAllByHospitalWorkspaceId(workspaceId)
                .stream()
                .map(FrontendInvitationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(invitations);
    }

    @PostMapping
    @Operation(summary = "Create frontend-compatible invitation")
    public ResponseEntity<FrontendInvitationResource> createInvitation(
            @Valid @RequestBody CreateFrontendInvitationResource resource
    ) {
        return createInvitationInternal(resource);
    }

    @PostMapping("/send")
    @Operation(summary = "Send frontend-compatible invitation")
    public ResponseEntity<FrontendInvitationResource> sendInvitation(
            @Valid @RequestBody CreateFrontendInvitationResource resource
    ) {
        return createInvitationInternal(resource);
    }

    @PatchMapping("/{invitationId}")
    @Operation(summary = "Patch frontend-compatible invitation")
    public ResponseEntity<FrontendInvitationResource> patchInvitation(
            @PathVariable @Positive Long invitationId,
            @Valid @RequestBody PatchFrontendInvitationResource resource
    ) {
        var invitation = userInvitationRepository.findById(invitationId);

        if (invitation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            var invitationToUpdate = invitation.get();
            var status = resource.status() == null ? "" : resource.status().trim().toUpperCase();

            if ("ACCEPTED".equals(status)) {
                invitationToUpdate.accept();
            } else if ("CANCELLED".equals(status) || "CANCELED".equals(status)) {
                invitationToUpdate.cancel();
            } else if ("EXPIRED".equals(status)) {
                invitationToUpdate.markAsExpired();
            }

            var savedInvitation = userInvitationRepository.save(invitationToUpdate);
            return ResponseEntity.ok(FrontendInvitationResourceFromEntityAssembler.toResourceFromEntity(savedInvitation));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{invitationId}")
    @Operation(summary = "Delete frontend-compatible invitation")
    public ResponseEntity<Void> deleteInvitation(
            @PathVariable @Positive Long invitationId
    ) {
        var invitation = userInvitationRepository.findById(invitationId);

        invitation.ifPresent(value -> {
            if (value.getStatus() == InvitationStatus.PENDING) {
                value.cancel();
                userInvitationRepository.save(value);
            }
        });

        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<FrontendInvitationResource> createInvitationInternal(CreateFrontendInvitationResource resource) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        if (workspaceId == null || workspaceId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var workspace = hospitalWorkspaceRepository.findById(workspaceId);

        if (workspace.isEmpty() || !workspace.get().isActive()) {
            return ResponseEntity.notFound().build();
        }

        try {
            var emailAddress = new EmailAddress(resource.email());
            var role = FrontendRoleMapper.toBackendRole(resource.role());

            if (role == UserRole.HOSPITAL_ADMIN) {
                return ResponseEntity.badRequest().build();
            }

            if (userAccountRepository.existsByEmailAddress(emailAddress)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            var existingPendingInvitation = userInvitationRepository.findAllByHospitalWorkspaceId(workspaceId)
                    .stream()
                    .filter(invitation -> invitation.getEmailAddress().equalsIgnoreCase(resource.email()))
                    .filter(invitation -> invitation.getStatus() == InvitationStatus.PENDING)
                    .findFirst();

            if (existingPendingInvitation.isPresent()) {
                var response = FrontendInvitationResourceFromEntityAssembler.toResourceFromEntity(existingPendingInvitation.get());
                return ResponseEntity.ok(response);
            }

            var invitation = new UserInvitation(
                    new InviteUserCommand(
                            workspaceId,
                            resource.email(),
                            role
                    )
            );

            var savedInvitation = userInvitationRepository.save(invitation);
            var response = FrontendInvitationResourceFromEntityAssembler.toResourceFromEntity(savedInvitation);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}