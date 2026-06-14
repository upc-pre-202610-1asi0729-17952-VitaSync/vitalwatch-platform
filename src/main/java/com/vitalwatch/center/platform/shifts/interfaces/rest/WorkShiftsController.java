package com.vitalwatch.center.platform.shifts.interfaces.rest;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.vitalwatch.center.platform.shifts.application.commandservices.ShiftCommandService;
import com.vitalwatch.center.platform.shifts.application.queryservices.ShiftQueryService;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetWorkShiftByIdQuery;
import com.vitalwatch.center.platform.shifts.domain.model.queries.GetWorkShiftsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.CancelWorkShiftResource;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.CompleteWorkShiftResource;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.CreateWorkShiftResource;
import com.vitalwatch.center.platform.shifts.interfaces.rest.resources.WorkShiftResource;
import com.vitalwatch.center.platform.shifts.interfaces.rest.transform.CancelWorkShiftCommandFromResourceAssembler;
import com.vitalwatch.center.platform.shifts.interfaces.rest.transform.CompleteWorkShiftCommandFromResourceAssembler;
import com.vitalwatch.center.platform.shifts.interfaces.rest.transform.CreateWorkShiftCommandFromResourceAssembler;
import com.vitalwatch.center.platform.shifts.interfaces.rest.transform.WorkShiftResourceFromEntityAssembler;
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
 * REST controller for work shifts.
 */
@RestController
@RequestMapping(value = "/api/v1/work-shifts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Work Shifts", description = "Work shift management endpoints")
public class WorkShiftsController {

    private final ShiftCommandService shiftCommandService;
    private final ShiftQueryService shiftQueryService;

    public WorkShiftsController(
            ShiftCommandService shiftCommandService,
            ShiftQueryService shiftQueryService
    ) {
        this.shiftCommandService = shiftCommandService;
        this.shiftQueryService = shiftQueryService;
    }

    @PostMapping
    @Operation(summary = "Create work shift", description = "Creates a hospital work shift.")
    public ResponseEntity<?> createWorkShift(@Valid @RequestBody CreateWorkShiftResource resource) {
        var command = CreateWorkShiftCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = shiftCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                WorkShiftResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{workShiftId}")
    @Operation(summary = "Get work shift by id", description = "Retrieves a work shift by id.")
    public ResponseEntity<?> getWorkShiftById(@PathVariable @Positive Long workShiftId) {
        var workShift = shiftQueryService.handle(new GetWorkShiftByIdQuery(workShiftId));

        if (workShift.isEmpty()) {
            var error = ApplicationError.notFound("WorkShift", workShiftId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = WorkShiftResourceFromEntityAssembler.toResourceFromEntity(workShift.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping(params = "hospitalWorkspaceId")
    @Operation(summary = "Get work shifts by hospital workspace", description = "Retrieves work shifts for a hospital workspace.")
    public ResponseEntity<List<WorkShiftResource>> getWorkShiftsByHospitalWorkspaceId(
            @RequestParam @Positive Long hospitalWorkspaceId
    ) {
        var workShifts = shiftQueryService.handle(new GetWorkShiftsByHospitalWorkspaceIdQuery(hospitalWorkspaceId));

        var resources = workShifts.stream()
                .map(WorkShiftResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{workShiftId}/cancel")
    @Operation(summary = "Cancel work shift", description = "Cancels a hospital work shift.")
    public ResponseEntity<?> cancelWorkShift(
            @PathVariable @Positive Long workShiftId,
            @Valid @RequestBody CancelWorkShiftResource resource
    ) {
        var command = CancelWorkShiftCommandFromResourceAssembler.toCommandFromResource(workShiftId, resource);
        var result = shiftCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                WorkShiftResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{workShiftId}/complete")
    @Operation(summary = "Complete work shift", description = "Marks a hospital work shift as completed.")
    public ResponseEntity<?> completeWorkShift(
            @PathVariable @Positive Long workShiftId,
            @Valid @RequestBody CompleteWorkShiftResource resource
    ) {
        var command = CompleteWorkShiftCommandFromResourceAssembler.toCommandFromResource(workShiftId, resource);
        var result = shiftCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                WorkShiftResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}