package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendShiftAssignmentResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendShiftRecordResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendShiftActionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendShiftAssignmentResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendShiftRecordResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendShiftAssignmentResourceFromEntityAssembler;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendShiftRecordResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;
import com.vitalwatch.center.platform.shifts.domain.model.commands.AssignUserToShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CancelWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CompleteWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.ConfirmShiftAssignmentCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.CreateWorkShiftCommand;
import com.vitalwatch.center.platform.shifts.domain.model.commands.ReleaseShiftAssignmentCommand;
import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftType;
import com.vitalwatch.center.platform.shifts.domain.repositories.ShiftAssignmentRepository;
import com.vitalwatch.center.platform.shifts.domain.repositories.WorkShiftRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * Frontend compatibility controller for shift endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend API - Shifts", description = "Shift endpoints compatible with the Angular frontend")
public class ShiftsCompatibilityController {

    private final WorkShiftRepository workShiftRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;

    public ShiftsCompatibilityController(
            WorkShiftRepository workShiftRepository,
            ShiftAssignmentRepository shiftAssignmentRepository
    ) {
        this.workShiftRepository = workShiftRepository;
        this.shiftAssignmentRepository = shiftAssignmentRepository;
    }

    @GetMapping("/shiftRecords")
    @Operation(summary = "Get frontend-compatible shift records")
    public ResponseEntity<List<FrontendShiftRecordResource>> getShiftRecords(
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId
    ) {
        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;

        if (workspaceId == null) {
            return ResponseEntity.ok(List.of());
        }

        var records = workShiftRepository.findAllByHospitalWorkspaceId(workspaceId)
                .stream()
                .map(FrontendShiftRecordResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(records);
    }

    @GetMapping("/shiftRecords/{shiftRecordId}")
    @Operation(summary = "Get frontend-compatible shift record by id")
    public ResponseEntity<FrontendShiftRecordResource> getShiftRecordById(
            @PathVariable @Positive Long shiftRecordId
    ) {
        return workShiftRepository.findById(shiftRecordId)
                .map(FrontendShiftRecordResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/shiftRecords")
    @Operation(summary = "Create frontend-compatible shift record")
    public ResponseEntity<FrontendShiftRecordResource> createShiftRecord(
            @Valid @RequestBody CreateFrontendShiftRecordResource resource
    ) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        if (workspaceId == null || workspaceId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var startsAt = resolveStartTime(resource);
        var endsAt = resolveEndTime(resource, startsAt);

        try {
            var shift = new WorkShift(
                    new CreateWorkShiftCommand(
                            workspaceId,
                            firstNonBlank(resource.label(), resource.name(), "General Shift"),
                            firstNonBlank(resource.workArea(), "General Care"),
                            resolveShiftType(firstNonBlank(resource.shiftType(), resource.type(), "DAY")),
                            startsAt,
                            endsAt
                    )
            );

            var savedShift = workShiftRepository.save(shift);
            var response = FrontendShiftRecordResourceFromEntityAssembler.toResourceFromEntity(savedShift);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/shiftRecords/{shiftRecordId}/complete")
    @Operation(summary = "Complete frontend-compatible shift record")
    public ResponseEntity<FrontendShiftRecordResource> completeShiftRecord(
            @PathVariable @Positive Long shiftRecordId,
            @Valid @RequestBody FrontendShiftActionResource resource
    ) {
        var selectedUserId = resolveUserId(resource);

        if (selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return workShiftRepository.findById(shiftRecordId)
                .map(shift -> {
                    try {
                        shift.complete(new CompleteWorkShiftCommand(shiftRecordId, selectedUserId));
                        var savedShift = workShiftRepository.save(shift);
                        return ResponseEntity.ok(FrontendShiftRecordResourceFromEntityAssembler.toResourceFromEntity(savedShift));
                    } catch (IllegalStateException exception) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).<FrontendShiftRecordResource>build();
                    } catch (IllegalArgumentException exception) {
                        return ResponseEntity.badRequest().<FrontendShiftRecordResource>build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/shiftRecords/{shiftRecordId}/cancel")
    @Operation(summary = "Cancel frontend-compatible shift record")
    public ResponseEntity<FrontendShiftRecordResource> cancelShiftRecord(
            @PathVariable @Positive Long shiftRecordId,
            @Valid @RequestBody FrontendShiftActionResource resource
    ) {
        var selectedUserId = resolveUserId(resource);
        var reason = firstNonBlank(resource.cancellationReason(), resource.reason(), "Cancelled from frontend");

        if (selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return workShiftRepository.findById(shiftRecordId)
                .map(shift -> {
                    try {
                        shift.cancel(new CancelWorkShiftCommand(shiftRecordId, selectedUserId, reason));
                        var savedShift = workShiftRepository.save(shift);
                        return ResponseEntity.ok(FrontendShiftRecordResourceFromEntityAssembler.toResourceFromEntity(savedShift));
                    } catch (IllegalStateException exception) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).<FrontendShiftRecordResource>build();
                    } catch (IllegalArgumentException exception) {
                        return ResponseEntity.badRequest().<FrontendShiftRecordResource>build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/shiftAssignments")
    @Operation(summary = "Get frontend-compatible shift assignments")
    public ResponseEntity<List<FrontendShiftAssignmentResource>> getShiftAssignments(
            @RequestParam(required = false) @Positive Long shiftRecordId,
            @RequestParam(required = false) @Positive Long workShiftId,
            @RequestParam(required = false) @Positive Long userAccountId,
            @RequestParam(required = false) @Positive Long userId,
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId
    ) {
        var selectedShiftId = shiftRecordId != null ? shiftRecordId : workShiftId;
        var selectedUserId = userAccountId != null ? userAccountId : userId;
        var selectedWorkspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;

        List<ShiftAssignment> assignments;

        if (selectedShiftId != null) {
            assignments = shiftAssignmentRepository.findAllByWorkShiftId(selectedShiftId);
        } else if (selectedUserId != null) {
            assignments = shiftAssignmentRepository.findAllByUserAccountId(selectedUserId);
        } else if (selectedWorkspaceId != null) {
            assignments = workShiftRepository.findAllByHospitalWorkspaceId(selectedWorkspaceId)
                    .stream()
                    .flatMap(shift -> shiftAssignmentRepository.findAllByWorkShiftId(shift.getId()).stream())
                    .toList();
        } else {
            assignments = List.of();
        }

        var resources = assignments.stream()
                .map(FrontendShiftAssignmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PostMapping("/shiftAssignments")
    @Operation(summary = "Create frontend-compatible shift assignment")
    public ResponseEntity<FrontendShiftAssignmentResource> createShiftAssignment(
            @Valid @RequestBody CreateFrontendShiftAssignmentResource resource
    ) {
        var selectedShiftId = resource.shiftRecordId() != null
                ? resource.shiftRecordId()
                : resource.workShiftId();

        var selectedUserId = resource.userAccountId() != null
                ? resource.userAccountId()
                : resource.userId();

        if (selectedShiftId == null || selectedShiftId <= 0 ||
                selectedUserId == null || selectedUserId <= 0 ||
                resource.assignedByUserAccountId() == null || resource.assignedByUserAccountId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var shift = workShiftRepository.findById(selectedShiftId);

        if (shift.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (shift.get().isCancelled() || shift.get().isCompleted()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (shiftAssignmentRepository.existsActiveAssignmentByUserAccountId(selectedUserId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {
            var assignment = new ShiftAssignment(
                    new AssignUserToShiftCommand(
                            selectedShiftId,
                            selectedUserId,
                            resource.assignedByUserAccountId()
                    )
            );

            var savedAssignment = shiftAssignmentRepository.save(assignment);
            var response = FrontendShiftAssignmentResourceFromEntityAssembler.toResourceFromEntity(savedAssignment);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/shiftAssignments/{shiftAssignmentId}/confirm")
    @Operation(summary = "Confirm frontend-compatible shift assignment")
    public ResponseEntity<FrontendShiftAssignmentResource> confirmShiftAssignment(
            @PathVariable @Positive Long shiftAssignmentId,
            @Valid @RequestBody FrontendShiftActionResource resource
    ) {
        var selectedUserId = resolveUserId(resource);

        if (selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return shiftAssignmentRepository.findById(shiftAssignmentId)
                .map(assignment -> {
                    try {
                        assignment.confirm(new ConfirmShiftAssignmentCommand(shiftAssignmentId, selectedUserId));
                        var savedAssignment = shiftAssignmentRepository.save(assignment);
                        return ResponseEntity.ok(FrontendShiftAssignmentResourceFromEntityAssembler.toResourceFromEntity(savedAssignment));
                    } catch (IllegalStateException exception) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).<FrontendShiftAssignmentResource>build();
                    } catch (IllegalArgumentException exception) {
                        return ResponseEntity.badRequest().<FrontendShiftAssignmentResource>build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/shiftAssignments/{shiftAssignmentId}/release")
    @Operation(summary = "Release frontend-compatible shift assignment")
    public ResponseEntity<FrontendShiftAssignmentResource> releaseShiftAssignment(
            @PathVariable @Positive Long shiftAssignmentId,
            @Valid @RequestBody FrontendShiftActionResource resource
    ) {
        var selectedUserId = resolveUserId(resource);
        var reason = firstNonBlank(resource.releaseReason(), resource.reason(), "Released from frontend");

        if (selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return shiftAssignmentRepository.findById(shiftAssignmentId)
                .map(assignment -> {
                    try {
                        assignment.release(new ReleaseShiftAssignmentCommand(shiftAssignmentId, selectedUserId, reason));
                        var savedAssignment = shiftAssignmentRepository.save(assignment);
                        return ResponseEntity.ok(FrontendShiftAssignmentResourceFromEntityAssembler.toResourceFromEntity(savedAssignment));
                    } catch (IllegalStateException exception) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).<FrontendShiftAssignmentResource>build();
                    } catch (IllegalArgumentException exception) {
                        return ResponseEntity.badRequest().<FrontendShiftAssignmentResource>build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private String firstNonBlank(String... values) {
        for (var value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return "";
    }

    private Instant resolveStartTime(CreateFrontendShiftRecordResource resource) {
        if (resource.startsAt() != null) {
            return resource.startsAt();
        }
        if (resource.startTime() != null) {
            return resource.startTime();
        }
        return Instant.now();
    }

    private Instant resolveEndTime(CreateFrontendShiftRecordResource resource, Instant startsAt) {
        var candidate = resource.endsAt() != null
                ? resource.endsAt()
                : resource.endTime();

        if (candidate == null || !candidate.isAfter(startsAt)) {
            return startsAt.plusSeconds(8 * 60 * 60);
        }

        return candidate;
    }

    private ShiftType resolveShiftType(String value) {
        if (value == null || value.isBlank()) {
            return ShiftType.DAY;
        }

        try {
            return ShiftType.valueOf(value.trim().toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return ShiftType.DAY;
        }
    }

    private Long resolveUserId(FrontendShiftActionResource resource) {
        if (resource.userAccountId() != null) {
            return resource.userAccountId();
        }
        return resource.userId();
    }
}