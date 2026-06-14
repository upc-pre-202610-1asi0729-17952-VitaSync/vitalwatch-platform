package com.vitalwatch.center.platform.staffrecovery.interfaces.rest;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.vitalwatch.center.platform.staffrecovery.application.commandservices.RecoveryCommandService;
import com.vitalwatch.center.platform.staffrecovery.application.queryservices.RecoveryQueryService;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryPlanByIdQuery;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryPlansByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryPlansByUserAccountIdQuery;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.CancelRecoveryPlanResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.CompleteRecoveryPlanResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.CreateRecoveryPlanResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.RecoveryPlanResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.resources.StartRecoveryPlanResource;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform.CancelRecoveryPlanCommandFromResourceAssembler;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform.CompleteRecoveryPlanCommandFromResourceAssembler;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform.CreateRecoveryPlanCommandFromResourceAssembler;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform.RecoveryPlanResourceFromEntityAssembler;
import com.vitalwatch.center.platform.staffrecovery.interfaces.rest.transform.StartRecoveryPlanCommandFromResourceAssembler;
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
 * REST controller for staff recovery plans.
 */
@RestController
@RequestMapping(value = "/api/v1/recovery-plans", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Recovery Plans", description = "Staff recovery plan management endpoints")
public class RecoveryPlansController {

    private final RecoveryCommandService recoveryCommandService;
    private final RecoveryQueryService recoveryQueryService;

    public RecoveryPlansController(
            RecoveryCommandService recoveryCommandService,
            RecoveryQueryService recoveryQueryService
    ) {
        this.recoveryCommandService = recoveryCommandService;
        this.recoveryQueryService = recoveryQueryService;
    }

    @PostMapping
    @Operation(summary = "Create recovery plan", description = "Creates a staff recovery plan after fatigue risk or incident detection.")
    public ResponseEntity<?> createRecoveryPlan(@Valid @RequestBody CreateRecoveryPlanResource resource) {
        var command = CreateRecoveryPlanCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = recoveryCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                RecoveryPlanResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{recoveryPlanId}")
    @Operation(summary = "Get recovery plan by id", description = "Retrieves a recovery plan by id.")
    public ResponseEntity<?> getRecoveryPlanById(@PathVariable @Positive Long recoveryPlanId) {
        var recoveryPlan = recoveryQueryService.handle(new GetRecoveryPlanByIdQuery(recoveryPlanId));

        if (recoveryPlan.isEmpty()) {
            var error = ApplicationError.notFound("RecoveryPlan", recoveryPlanId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = RecoveryPlanResourceFromEntityAssembler.toResourceFromEntity(recoveryPlan.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping(params = "hospitalWorkspaceId")
    @Operation(summary = "Get recovery plans by hospital workspace", description = "Retrieves recovery plans for a hospital workspace.")
    public ResponseEntity<List<RecoveryPlanResource>> getRecoveryPlansByHospitalWorkspaceId(
            @RequestParam @Positive Long hospitalWorkspaceId
    ) {
        var recoveryPlans = recoveryQueryService.handle(
                new GetRecoveryPlansByHospitalWorkspaceIdQuery(hospitalWorkspaceId)
        );

        var resources = recoveryPlans.stream()
                .map(RecoveryPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/user", params = "userAccountId")
    @Operation(summary = "Get recovery plans by user account", description = "Retrieves recovery plans for a user account.")
    public ResponseEntity<List<RecoveryPlanResource>> getRecoveryPlansByUserAccountId(
            @RequestParam @Positive Long userAccountId
    ) {
        var recoveryPlans = recoveryQueryService.handle(new GetRecoveryPlansByUserAccountIdQuery(userAccountId));

        var resources = recoveryPlans.stream()
                .map(RecoveryPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{recoveryPlanId}/start")
    @Operation(summary = "Start recovery plan", description = "Marks a recovery plan as in progress.")
    public ResponseEntity<?> startRecoveryPlan(
            @PathVariable @Positive Long recoveryPlanId,
            @Valid @RequestBody StartRecoveryPlanResource resource
    ) {
        var command = StartRecoveryPlanCommandFromResourceAssembler.toCommandFromResource(recoveryPlanId, resource);
        var result = recoveryCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                RecoveryPlanResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{recoveryPlanId}/complete")
    @Operation(summary = "Complete recovery plan", description = "Marks a recovery plan as completed.")
    public ResponseEntity<?> completeRecoveryPlan(
            @PathVariable @Positive Long recoveryPlanId,
            @Valid @RequestBody CompleteRecoveryPlanResource resource
    ) {
        var command = CompleteRecoveryPlanCommandFromResourceAssembler.toCommandFromResource(recoveryPlanId, resource);
        var result = recoveryCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                RecoveryPlanResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{recoveryPlanId}/cancel")
    @Operation(summary = "Cancel recovery plan", description = "Cancels a recovery plan.")
    public ResponseEntity<?> cancelRecoveryPlan(
            @PathVariable @Positive Long recoveryPlanId,
            @Valid @RequestBody CancelRecoveryPlanResource resource
    ) {
        var command = CancelRecoveryPlanCommandFromResourceAssembler.toCommandFromResource(recoveryPlanId, resource);
        var result = recoveryCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                RecoveryPlanResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}