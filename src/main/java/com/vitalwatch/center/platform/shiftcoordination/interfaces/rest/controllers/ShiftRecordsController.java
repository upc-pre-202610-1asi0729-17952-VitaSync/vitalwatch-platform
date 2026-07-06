package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shiftcoordination.domain.model.enums.ShiftStatus;
import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities.ShiftRecordJpaEntity;
import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.repositories.ShiftRecordJpaRepository;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.CreateShiftRecordResource;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.ShiftRecordResource;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.UpdateShiftRecordResource;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.transform.ShiftRecordResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * REST controller for shift records.
 */
@RestController
@RequestMapping("/shiftRecords")
@Tag(name = "Shift Records", description = "Medical shift record endpoints")
public class ShiftRecordsController {

    private final ShiftRecordJpaRepository shiftRecordRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final UserJpaRepository userRepository;
    private final MessageResolver messageResolver;

    public ShiftRecordsController(
            ShiftRecordJpaRepository shiftRecordRepository,
            OrganizationJpaRepository organizationRepository,
            UserJpaRepository userRepository,
            MessageResolver messageResolver
    ) {
        this.shiftRecordRepository = shiftRecordRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all shift records or filter by organization and user")
    public ResponseEntity<List<ShiftRecordResource>> getShiftRecords(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long userId
    ) {
        var shifts = organizationId != null && userId != null
                ? shiftRecordRepository.findByOrganizationIdAndUserIdOrderByScheduledStartDesc(
                organizationId,
                userId
        )
                : organizationId != null
                  ? shiftRecordRepository.findByOrganizationIdOrderByScheduledStartDesc(organizationId)
                  : shiftRecordRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(ShiftRecordJpaEntity::getScheduledStart).reversed())
                .toList();

        var resources = shifts.stream()
                .map(ShiftRecordResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{shiftRecordId}")
    @Operation(summary = "Get shift record by id")
    public ResponseEntity<?> getShiftRecordById(@PathVariable Long shiftRecordId) {
        var shift = shiftRecordRepository.findById(shiftRecordId);

        if (shift.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("shift.shiftRecord.notFound")
                    )
            );
        }

        return ResponseEntity.ok(
                ShiftRecordResourceFromEntityAssembler.toResourceFromEntity(shift.get())
        );
    }

    @PostMapping
    @Operation(summary = "Create shift record")
    public ResponseEntity<?> createShiftRecord(
            @Valid @RequestBody CreateShiftRecordResource resource
    ) {
        if (!organizationRepository.existsById(resource.organizationId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.organization.notFound")
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

        if (!resource.scheduledEnd().isAfter(resource.scheduledStart())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation(
                            messageResolver.get("shift.shiftRecord.invalidDateRange")
                    )
            );
        }

        var shift = new ShiftRecordJpaEntity(
                resource.organizationId(),
                resource.userId(),
                resource.workAreaId(),
                resource.type(),
                resource.status() == null ? ShiftStatus.SCHEDULED : resource.status(),
                resource.scheduledStart(),
                resource.scheduledEnd(),
                resource.checkInAt(),
                resource.checkOutAt()
        );

        var savedShift = shiftRecordRepository.save(shift);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ShiftRecordResourceFromEntityAssembler.toResourceFromEntity(savedShift)
        );
    }

    @PatchMapping("/{shiftRecordId}")
    @Operation(summary = "Update shift record status")
    public ResponseEntity<?> updateShiftRecord(
            @PathVariable Long shiftRecordId,
            @Valid @RequestBody UpdateShiftRecordResource resource
    ) {
        var shift = shiftRecordRepository.findById(shiftRecordId);

        if (shift.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("shift.shiftRecord.notFound")
                    )
            );
        }

        var foundShift = shift.get();

        foundShift.updateStatus(
                resource.status(),
                resource.checkInAt(),
                resource.checkOutAt()
        );

        var savedShift = shiftRecordRepository.save(foundShift);

        return ResponseEntity.ok(
                ShiftRecordResourceFromEntityAssembler.toResourceFromEntity(savedShift)
        );
    }
}