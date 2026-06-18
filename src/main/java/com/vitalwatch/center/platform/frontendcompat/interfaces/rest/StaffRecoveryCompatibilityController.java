package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CompleteFrontendPreventiveActionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendPreventiveActionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendRecoveryPlanResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendPreventiveActionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendRecoveryPlanActionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendRecoveryPlanResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.PatchFrontendPreventiveActionResource;
import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryAction;
import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryPlan;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.AddRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CancelRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryActionCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CompleteRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.CreateRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.commands.StartRecoveryPlanCommand;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryActionType;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPlanReason;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.RecoveryPriority;
import com.vitalwatch.center.platform.staffrecovery.domain.repositories.RecoveryActionRepository;
import com.vitalwatch.center.platform.staffrecovery.domain.repositories.RecoveryPlanRepository;
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
 * Frontend compatibility controller for staff recovery endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend API - Staff Recovery", description = "Staff recovery endpoints compatible with the Angular frontend")
public class StaffRecoveryCompatibilityController {

    private final RecoveryPlanRepository recoveryPlanRepository;
    private final RecoveryActionRepository recoveryActionRepository;

    public StaffRecoveryCompatibilityController(
            RecoveryPlanRepository recoveryPlanRepository,
            RecoveryActionRepository recoveryActionRepository
    ) {
        this.recoveryPlanRepository = recoveryPlanRepository;
        this.recoveryActionRepository = recoveryActionRepository;
    }

    @GetMapping("/recoveryPlans")
    @Operation(summary = "Get frontend-compatible recovery plans")
    public ResponseEntity<List<FrontendRecoveryPlanResource>> getRecoveryPlans(
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId,
            @RequestParam(required = false) @Positive Long userAccountId,
            @RequestParam(required = false) @Positive Long userId
    ) {
        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;
        var selectedUserId = userAccountId != null ? userAccountId : userId;

        List<RecoveryPlan> plans;

        if (selectedUserId != null) {
            plans = recoveryPlanRepository.findAllByUserAccountId(selectedUserId);
        } else if (workspaceId != null) {
            plans = recoveryPlanRepository.findAllByHospitalWorkspaceId(workspaceId);
        } else {
            plans = List.of();
        }

        var resources = plans.stream()
                .map(this::toRecoveryPlanResource)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/recoveryPlans/{recoveryPlanId}")
    @Operation(summary = "Get frontend-compatible recovery plan by id")
    public ResponseEntity<FrontendRecoveryPlanResource> getRecoveryPlanById(
            @PathVariable @Positive Long recoveryPlanId
    ) {
        return recoveryPlanRepository.findById(recoveryPlanId)
                .map(this::toRecoveryPlanResource)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/recoveryPlans")
    @Operation(summary = "Create frontend-compatible recovery plan")
    public ResponseEntity<FrontendRecoveryPlanResource> createRecoveryPlan(
            @Valid @RequestBody CreateFrontendRecoveryPlanResource resource
    ) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        var selectedUserId = resource.userAccountId() != null
                ? resource.userAccountId()
                : resource.userId();

        if (workspaceId == null || workspaceId <= 0 || selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            var plan = new RecoveryPlan(
                    new CreateRecoveryPlanCommand(
                            workspaceId,
                            selectedUserId,
                            resource.clinicalRiskAssessmentId(),
                            resource.incidentId(),
                            resolvePlanReason(resource.reason()),
                            resolvePriority(resource.priority()),
                            resolveRestHours(resource.recommendedRestHours()),
                            firstNonBlank(resource.notes(), "Staff member requires a preventive recovery plan.")
                    )
            );

            var savedPlan = recoveryPlanRepository.save(plan);
            return ResponseEntity.status(HttpStatus.CREATED).body(toRecoveryPlanResource(savedPlan));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/recoveryPlans/{recoveryPlanId}/start")
    @Operation(summary = "Start frontend-compatible recovery plan")
    public ResponseEntity<FrontendRecoveryPlanResource> startRecoveryPlan(
            @PathVariable @Positive Long recoveryPlanId,
            @Valid @RequestBody FrontendRecoveryPlanActionResource resource
    ) {
        var selectedUserId = resolveStartUserId(resource);

        if (selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var plan = recoveryPlanRepository.findById(recoveryPlanId);

        if (plan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            var planToStart = plan.get();
            planToStart.start(new StartRecoveryPlanCommand(recoveryPlanId, selectedUserId));
            var savedPlan = recoveryPlanRepository.save(planToStart);

            return ResponseEntity.ok(toRecoveryPlanResource(savedPlan));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PatchMapping("/recoveryPlans/{recoveryPlanId}/complete")
    @Operation(summary = "Complete frontend-compatible recovery plan")
    public ResponseEntity<FrontendRecoveryPlanResource> completeRecoveryPlan(
            @PathVariable @Positive Long recoveryPlanId,
            @Valid @RequestBody FrontendRecoveryPlanActionResource resource
    ) {
        var selectedUserId = resolveCompleteUserId(resource);

        if (selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var plan = recoveryPlanRepository.findById(recoveryPlanId);

        if (plan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            var planToComplete = plan.get();
            planToComplete.complete(new CompleteRecoveryPlanCommand(
                    recoveryPlanId,
                    selectedUserId,
                    firstNonBlank(resource.completionNotes(), resource.notes(), "Recovery plan completed.")
            ));

            var savedPlan = recoveryPlanRepository.save(planToComplete);

            return ResponseEntity.ok(toRecoveryPlanResource(savedPlan));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PatchMapping("/recoveryPlans/{recoveryPlanId}/cancel")
    @Operation(summary = "Cancel frontend-compatible recovery plan")
    public ResponseEntity<FrontendRecoveryPlanResource> cancelRecoveryPlan(
            @PathVariable @Positive Long recoveryPlanId,
            @Valid @RequestBody FrontendRecoveryPlanActionResource resource
    ) {
        var selectedUserId = resolveCancelUserId(resource);

        if (selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var plan = recoveryPlanRepository.findById(recoveryPlanId);

        if (plan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            var planToCancel = plan.get();
            planToCancel.cancel(new CancelRecoveryPlanCommand(
                    recoveryPlanId,
                    selectedUserId,
                    firstNonBlank(resource.cancellationReason(), resource.reason(), "Recovery plan cancelled.")
            ));

            var savedPlan = recoveryPlanRepository.save(planToCancel);

            return ResponseEntity.ok(toRecoveryPlanResource(savedPlan));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping({"/preventiveActions", "/recoveryActions"})
    @Operation(summary = "Get frontend-compatible preventive actions")
    public ResponseEntity<List<FrontendPreventiveActionResource>> getPreventiveActions(
            @RequestParam(required = false) @Positive Long recoveryPlanId,
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId,
            @RequestParam(required = false) @Positive Long userAccountId,
            @RequestParam(required = false) @Positive Long userId
    ) {
        if (recoveryPlanId != null) {
            var plan = recoveryPlanRepository.findById(recoveryPlanId).orElse(null);

            var actions = recoveryActionRepository.findAllByRecoveryPlanId(recoveryPlanId)
                    .stream()
                    .map(action -> toPreventiveActionResource(action, plan))
                    .toList();

            return ResponseEntity.ok(actions);
        }

        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;
        var selectedUserId = userAccountId != null ? userAccountId : userId;

        List<RecoveryPlan> plans;

        if (selectedUserId != null) {
            plans = recoveryPlanRepository.findAllByUserAccountId(selectedUserId);
        } else if (workspaceId != null) {
            plans = recoveryPlanRepository.findAllByHospitalWorkspaceId(workspaceId);
        } else {
            plans = List.of();
        }

        var actions = plans.stream()
                .flatMap(plan -> recoveryActionRepository.findAllByRecoveryPlanId(plan.getId())
                        .stream()
                        .map(action -> toPreventiveActionResource(action, plan)))
                .toList();

        return ResponseEntity.ok(actions);
    }

    @GetMapping({"/preventiveActions/{preventiveActionId}", "/recoveryActions/{preventiveActionId}"})
    @Operation(summary = "Get frontend-compatible preventive action by id")
    public ResponseEntity<FrontendPreventiveActionResource> getPreventiveActionById(
            @PathVariable @Positive Long preventiveActionId
    ) {
        return recoveryActionRepository.findById(preventiveActionId)
                .map(action -> toPreventiveActionResource(action, null))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping({"/preventiveActions", "/recoveryActions"})
    @Operation(summary = "Create frontend-compatible preventive action")
    public ResponseEntity<FrontendPreventiveActionResource> createPreventiveAction(
            @Valid @RequestBody CreateFrontendPreventiveActionResource resource
    ) {
        Long recoveryPlanId = resource.recoveryPlanId();
        RecoveryPlan plan = null;

        if (recoveryPlanId != null && recoveryPlanId > 0) {
            var existingPlan = recoveryPlanRepository.findById(recoveryPlanId);

            if (existingPlan.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            plan = existingPlan.get();

        } else {
            var workspaceId = resource.organizationId() != null
                    ? resource.organizationId()
                    : resource.hospitalWorkspaceId();

            var selectedUserId = resource.userAccountId() != null
                    ? resource.userAccountId()
                    : resource.userId();

            if (workspaceId == null || workspaceId <= 0 ||
                    selectedUserId == null || selectedUserId <= 0) {
                return ResponseEntity.badRequest().build();
            }

            var newPlan = new RecoveryPlan(
                    new CreateRecoveryPlanCommand(
                            workspaceId,
                            selectedUserId,
                            null,
                            null,
                            RecoveryPlanReason.FATIGUE_RISK,
                            RecoveryPriority.HIGH,
                            resolveRestHours(resource.recommendedRestHours()),
                            firstNonBlank(resource.notes(), "Preventive action generated from frontend.")
                    )
            );

            plan = recoveryPlanRepository.save(newPlan);
            recoveryPlanId = plan.getId();
        }

        try {
            var action = new RecoveryAction(
                    new AddRecoveryActionCommand(
                            recoveryPlanId,
                            resolveActionType(firstNonBlank(resource.actionType(), resource.type(), "REST_PERIOD")),
                            firstNonBlank(resource.notes(), "Preventive recovery action recommended."),
                            resolveRestHours(resource.recommendedRestHours())
                    )
            );

            var savedAction = recoveryActionRepository.save(action);
            return ResponseEntity.status(HttpStatus.CREATED).body(toPreventiveActionResource(savedAction, plan));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping({"/preventiveActions/{preventiveActionId}", "/recoveryActions/{preventiveActionId}"})
    @Operation(summary = "Patch frontend-compatible preventive action")
    public ResponseEntity<FrontendPreventiveActionResource> patchPreventiveAction(
            @PathVariable @Positive Long preventiveActionId,
            @Valid @RequestBody PatchFrontendPreventiveActionResource resource
    ) {
        var action = recoveryActionRepository.findById(preventiveActionId);

        if (action.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            var actionToUpdate = action.get();
            var status = firstNonBlank(resource.status(), "");
            var shouldComplete = Boolean.TRUE.equals(resource.completed()) || "COMPLETED".equalsIgnoreCase(status);

            if (shouldComplete && !Boolean.TRUE.equals(actionToUpdate.getCompleted())) {
                var completedByUserId = resource.completedByUserAccountId() != null
                        ? resource.completedByUserAccountId()
                        : resource.userAccountId() != null
                        ? resource.userAccountId()
                        : resource.userId();

                if (completedByUserId == null || completedByUserId <= 0) {
                    return ResponseEntity.badRequest().build();
                }

                actionToUpdate.complete(new CompleteRecoveryActionCommand(preventiveActionId, completedByUserId));
                var savedAction = recoveryActionRepository.save(actionToUpdate);
                var plan = recoveryPlanRepository.findById(savedAction.getRecoveryPlanId()).orElse(null);

                return ResponseEntity.ok(toPreventiveActionResource(savedAction, plan));
            }

            var plan = recoveryPlanRepository.findById(actionToUpdate.getRecoveryPlanId()).orElse(null);
            return ResponseEntity.ok(toPreventiveActionResource(actionToUpdate, plan));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PatchMapping({"/preventiveActions/{preventiveActionId}/complete", "/recoveryActions/{preventiveActionId}/complete"})
    @Operation(summary = "Complete frontend-compatible preventive action")
    public ResponseEntity<FrontendPreventiveActionResource> completePreventiveAction(
            @PathVariable @Positive Long preventiveActionId,
            @Valid @RequestBody CompleteFrontendPreventiveActionResource resource
    ) {
        var selectedUserId = resource.completedByUserAccountId() != null
                ? resource.completedByUserAccountId()
                : resource.userAccountId() != null
                ? resource.userAccountId()
                : resource.userId();

        if (selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var action = recoveryActionRepository.findById(preventiveActionId);

        if (action.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            var actionToComplete = action.get();
            actionToComplete.complete(new CompleteRecoveryActionCommand(preventiveActionId, selectedUserId));
            var savedAction = recoveryActionRepository.save(actionToComplete);

            var plan = recoveryPlanRepository.findById(savedAction.getRecoveryPlanId()).orElse(null);

            return ResponseEntity.ok(toPreventiveActionResource(savedAction, plan));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    private FrontendRecoveryPlanResource toRecoveryPlanResource(RecoveryPlan entity) {
        return new FrontendRecoveryPlanResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                entity.getUserAccountId(),
                entity.getUserAccountId(),
                entity.getClinicalRiskAssessmentId(),
                entity.getIncidentId(),
                entity.getReason().name(),
                entity.getPriority().name(),
                entity.getStatus().name(),
                entity.getRecommendedRestHours(),
                entity.getNotes(),
                entity.getCompletionNotes(),
                entity.getCancellationReason(),
                entity.getStartedByUserAccountId(),
                entity.getCompletedByUserAccountId(),
                entity.getCancelledByUserAccountId(),
                entity.getCreatedAt(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                entity.getCancelledAt()
        );
    }

    private FrontendPreventiveActionResource toPreventiveActionResource(
            RecoveryAction action,
            RecoveryPlan plan
    ) {
        return new FrontendPreventiveActionResource(
                action.getId(),
                action.getRecoveryPlanId(),
                plan != null ? plan.getHospitalWorkspaceId() : null,
                plan != null ? plan.getHospitalWorkspaceId() : null,
                plan != null ? plan.getUserAccountId() : null,
                plan != null ? plan.getUserAccountId() : null,
                action.getActionType().name(),
                action.getActionType().name(),
                action.getNotes(),
                action.getRecommendedRestHours(),
                action.getCompleted(),
                Boolean.TRUE.equals(action.getCompleted()) ? "COMPLETED" : "PENDING",
                action.getCompletedByUserAccountId(),
                action.getCreatedAt(),
                action.getCompletedAt()
        );
    }

    private RecoveryPlanReason resolvePlanReason(String value) {
        if (value == null || value.isBlank()) {
            return RecoveryPlanReason.FATIGUE_RISK;
        }

        try {
            return RecoveryPlanReason.valueOf(value.trim().toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return RecoveryPlanReason.FATIGUE_RISK;
        }
    }

    private RecoveryPriority resolvePriority(String value) {
        if (value == null || value.isBlank()) {
            return RecoveryPriority.HIGH;
        }

        try {
            return RecoveryPriority.valueOf(value.trim().toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return RecoveryPriority.HIGH;
        }
    }

    private RecoveryActionType resolveActionType(String value) {
        if (value == null || value.isBlank()) {
            return RecoveryActionType.REST_PERIOD;
        }

        try {
            return RecoveryActionType.valueOf(value.trim().toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return RecoveryActionType.REST_PERIOD;
        }
    }

    private Double resolveRestHours(Double value) {
        if (value == null || value < 0) {
            return 12.0;
        }

        return Math.min(value, 72.0);
    }

    private Long resolveStartUserId(FrontendRecoveryPlanActionResource resource) {
        if (resource.startedByUserAccountId() != null) {
            return resource.startedByUserAccountId();
        }

        return resolveGenericUserId(resource);
    }

    private Long resolveCompleteUserId(FrontendRecoveryPlanActionResource resource) {
        if (resource.completedByUserAccountId() != null) {
            return resource.completedByUserAccountId();
        }

        return resolveGenericUserId(resource);
    }

    private Long resolveCancelUserId(FrontendRecoveryPlanActionResource resource) {
        if (resource.cancelledByUserAccountId() != null) {
            return resource.cancelledByUserAccountId();
        }

        return resolveGenericUserId(resource);
    }

    private Long resolveGenericUserId(FrontendRecoveryPlanActionResource resource) {
        if (resource.userAccountId() != null) {
            return resource.userAccountId();
        }

        return resource.userId();
    }

    private String firstNonBlank(String... values) {
        for (var value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return "";
    }
}