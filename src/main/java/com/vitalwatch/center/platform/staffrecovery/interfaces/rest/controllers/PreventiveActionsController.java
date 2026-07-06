package com.vitalwatch.center.platform.staffrecovery.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities.PreventiveActionJpaEntity;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.repositories.PreventiveActionJpaRepository;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.PreventiveActionStatus;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.CreatePreventiveActionResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.PreventiveActionResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.UpdatePreventiveActionStatusResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform.PreventiveActionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * REST controller for preventive recovery actions.
 */
@RestController
@RequestMapping("/preventiveActions")
@Tag(name = "Preventive Actions", description = "Staff recovery preventive action endpoints")
public class PreventiveActionsController {

    private final PreventiveActionJpaRepository preventiveActionRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final UserJpaRepository userRepository;
    private final MessageResolver messageResolver;

    public PreventiveActionsController(
            PreventiveActionJpaRepository preventiveActionRepository,
            OrganizationJpaRepository organizationRepository,
            UserJpaRepository userRepository,
            MessageResolver messageResolver
    ) {
        this.preventiveActionRepository = preventiveActionRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all preventive actions or filter them")
    public ResponseEntity<List<PreventiveActionResource>> getPreventiveActions(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long supervisorId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) PreventiveActionStatus status
    ) {
        var actions = organizationId != null && supervisorId != null
                ? preventiveActionRepository.findByOrganizationIdAndSupervisorIdOrderByCreatedAtDesc(
                organizationId,
                supervisorId
        )
                : organizationId != null && userId != null
                  ? preventiveActionRepository.findByOrganizationIdAndUserIdOrderByCreatedAtDesc(
                organizationId,
                userId
        )
                  : organizationId != null && status != null
                    ? preventiveActionRepository.findByOrganizationIdAndStatusOrderByCreatedAtDesc(
                organizationId,
                status
        )
                    : organizationId != null
                      ? preventiveActionRepository.findByOrganizationIdOrderByCreatedAtDesc(organizationId)
                      : preventiveActionRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(PreventiveActionJpaEntity::getCreatedAt).reversed())
                .toList();

        var resources = actions.stream()
                .map(PreventiveActionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{preventiveActionId}")
    @Operation(summary = "Get preventive action by id")
    public ResponseEntity<?> getPreventiveActionById(@PathVariable Long preventiveActionId) {
        var action = preventiveActionRepository.findById(preventiveActionId);

        if (action.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("staffRecovery.preventiveAction.notFound")
                    )
            );
        }

        return ResponseEntity.ok(
                PreventiveActionResourceFromEntityAssembler.toResourceFromEntity(action.get())
        );
    }

    @PostMapping
    @Operation(summary = "Create preventive action")
    public ResponseEntity<?> createPreventiveAction(
            @Valid @RequestBody CreatePreventiveActionResource resource
    ) {
        if (!organizationRepository.existsById(resource.organizationId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.organization.notFound")
                    )
            );
        }

        if (!userRepository.existsById(resource.supervisorId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("staffRecovery.supervisor.notFound")
                    )
            );
        }

        if (!userRepository.existsById(resource.userId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.user.notFound")
                    )
            );
        }

        var action = new PreventiveActionJpaEntity(
                resource.organizationId(),
                resource.supervisorId(),
                resource.userId(),
                resource.type(),
                resource.status() == null ? PreventiveActionStatus.PENDING : resource.status(),
                resource.notes(),
                resource.createdAt(),
                resource.completedAt()
        );

        var savedAction = preventiveActionRepository.save(action);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                PreventiveActionResourceFromEntityAssembler.toResourceFromEntity(savedAction)
        );
    }

    @PatchMapping("/{preventiveActionId}")
    @Operation(summary = "Update preventive action status")
    public ResponseEntity<?> updatePreventiveActionStatus(
            @PathVariable Long preventiveActionId,
            @Valid @RequestBody UpdatePreventiveActionStatusResource resource
    ) {
        var action = preventiveActionRepository.findById(preventiveActionId);

        if (action.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("staffRecovery.preventiveAction.notFound")
                    )
            );
        }

        var foundAction = action.get();

        foundAction.updateStatus(
                resource.status(),
                resource.completedAt()
        );

        var savedAction = preventiveActionRepository.save(foundAction);

        return ResponseEntity.ok(
                PreventiveActionResourceFromEntityAssembler.toResourceFromEntity(savedAction)
        );
    }
}