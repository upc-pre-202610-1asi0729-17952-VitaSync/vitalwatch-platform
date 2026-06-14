package com.vitalwatch.center.platform.shifts.interfaces.rest;

import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.vitalwatch.center.platform.shifts.application.commandservices.ShiftCommandService;
import com.vitalwatch.center.platform.shifts.application.queryservices.ShiftQueryService;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetShiftAssignmentsByUserAccountIdQuery;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetShiftAssignmentsByWorkShiftIdQuery;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.AssignUserToShiftResource;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.ConfirmShiftAssignmentResource;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.ReleaseShiftAssignmentResource;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.ShiftAssignmentResource;
import com.vitalwatch.center.platform.shifts.interfaces.rest.transform.AssignUserToShiftCommandFromResourceAssembler;
import com.vitalwatch.center.platform.shifts.interfaces.rest.transform.ConfirmShiftAssignmentCommandFromResourceAssembler;
import com.vitalwatch.center.platform.shifts.interfaces.rest.transform.ReleaseShiftAssignmentCommandFromResourceAssembler;
import com.vitalwatch.center.platform.shifts.interfaces.rest.transform.ShiftAssignmentResourceFromEntityAssembler;
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
 * REST controller for shift assignments.
 */
@RestController
@RequestMapping(value = "/api/v1/shift-assignments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Shift Assignments", description = "Shift assignment management endpoints")
public class ShiftAssignmentsController {

    private final ShiftCommandService shiftCommandService;
    private final ShiftQueryService shiftQueryService;

    public ShiftAssignmentsController(
            ShiftCommandService shiftCommandService,
            ShiftQueryService shiftQueryService
    ) {
        this.shiftCommandService = shiftCommandService;
        this.shiftQueryService = shiftQueryService;
    }

    @PostMapping
    @Operation(summary = "Assign user to shift", description = "Assigns a user account to a work shift.")
    public ResponseEntity<?> assignUserToShift(@Valid @RequestBody AssignUserToShiftResource resource) {
        var command = AssignUserToShiftCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = shiftCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ShiftAssignmentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping(params = "workShiftId")
    @Operation(summary = "Get assignments by work shift", description = "Retrieves assignments for a work shift.")
    public ResponseEntity<List<ShiftAssignmentResource>> getAssignmentsByWorkShiftId(
            @RequestParam @Positive Long workShiftId
    ) {
        var assignments = shiftQueryService.handle(new GetShiftAssignmentsByWorkShiftIdQuery(workShiftId));

        var resources = assignments.stream()
                .map(ShiftAssignmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/user", params = "userAccountId")
    @Operation(summary = "Get assignments by user account", description = "Retrieves assignments for a user account.")
    public ResponseEntity<List<ShiftAssignmentResource>> getAssignmentsByUserAccountId(
            @RequestParam @Positive Long userAccountId
    ) {
        var assignments = shiftQueryService.handle(new GetShiftAssignmentsByUserAccountIdQuery(userAccountId));

        var resources = assignments.stream()
                .map(ShiftAssignmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{shiftAssignmentId}/confirm")
    @Operation(summary = "Confirm shift assignment", description = "Confirms a shift assignment.")
    public ResponseEntity<?> confirmShiftAssignment(
            @PathVariable @Positive Long shiftAssignmentId,
            @Valid @RequestBody ConfirmShiftAssignmentResource resource
    ) {
        var command = ConfirmShiftAssignmentCommandFromResourceAssembler.toCommandFromResource(
                shiftAssignmentId,
                resource
        );
        var result = shiftCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ShiftAssignmentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{shiftAssignmentId}/release")
    @Operation(summary = "Release shift assignment", description = "Releases a user from a shift assignment.")
    public ResponseEntity<?> releaseShiftAssignment(
            @PathVariable @Positive Long shiftAssignmentId,
            @Valid @RequestBody ReleaseShiftAssignmentResource resource
    ) {
        var command = ReleaseShiftAssignmentCommandFromResourceAssembler.toCommandFromResource(
                shiftAssignmentId,
                resource
        );
        var result = shiftCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ShiftAssignmentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}