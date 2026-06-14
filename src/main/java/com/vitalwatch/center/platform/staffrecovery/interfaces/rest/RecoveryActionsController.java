package com.vitalwatch.center.platform.staffrecovery.interfaces.rest;

import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.vitalwatch.center.platform.staffrecovery.application.commandservices.RecoveryCommandService;
import com.vitalwatch.center.platform.staffrecovery.application.queryservices.RecoveryQueryService;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryActionsByRecoveryPlanIdQuery;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.AddRecoveryActionResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.CompleteRecoveryActionResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.RecoveryActionResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform.AddRecoveryActionCommandFromResourceAssembler;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform.CompleteRecoveryActionCommandFromResourceAssembler;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform.RecoveryActionResourceFromEntityAssembler;
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
 * REST controller for staff recovery actions.
 */
@RestController
@RequestMapping(value = "/api/v1/recovery-actions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Recovery Actions", description = "Staff recovery action management endpoints")
public class RecoveryActionsController {

    private final RecoveryCommandService recoveryCommandService;
    private final RecoveryQueryService recoveryQueryService;

    public RecoveryActionsController(
            RecoveryCommandService recoveryCommandService,
            RecoveryQueryService recoveryQueryService
    ) {
        this.recoveryCommandService = recoveryCommandService;
        this.recoveryQueryService = recoveryQueryService;
    }

    @PostMapping
    @Operation(summary = "Add recovery action", description = "Adds a recovery action to a recovery plan.")
    public ResponseEntity<?> addRecoveryAction(@Valid @RequestBody AddRecoveryActionResource resource) {
        var command = AddRecoveryActionCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = recoveryCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                RecoveryActionResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping(params = "recoveryPlanId")
    @Operation(summary = "Get recovery actions by recovery plan", description = "Retrieves recovery actions for a recovery plan.")
    public ResponseEntity<List<RecoveryActionResource>> getRecoveryActionsByRecoveryPlanId(
            @RequestParam @Positive Long recoveryPlanId
    ) {
        var recoveryActions = recoveryQueryService.handle(new GetRecoveryActionsByRecoveryPlanIdQuery(recoveryPlanId));

        var resources = recoveryActions.stream()
                .map(RecoveryActionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{recoveryActionId}/complete")
    @Operation(summary = "Complete recovery action", description = "Marks a recovery action as completed.")
    public ResponseEntity<?> completeRecoveryAction(
            @PathVariable @Positive Long recoveryActionId,
            @Valid @RequestBody CompleteRecoveryActionResource resource
    ) {
        var command = CompleteRecoveryActionCommandFromResourceAssembler.toCommandFromResource(
                recoveryActionId,
                resource
        );
        var result = recoveryCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                RecoveryActionResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}