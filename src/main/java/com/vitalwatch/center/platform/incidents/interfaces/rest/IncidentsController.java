package com.vitalwatch.center.platform.incidents.interfaces.rest;

import com.vitalwatch.center.platform.incidents.application.commandservices.IncidentCommandService;
import com.vitalwatch.center.platform.incidents.application.queryservices.IncidentQueryService;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetIncidentByIdQuery;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetIncidentsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetIncidentsByReportedUserAccountIdQuery;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetOpenIncidentsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.AcknowledgeIncidentResource;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.CancelIncidentResource;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.CreateIncidentResource;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.EscalateIncidentResource;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.IncidentResource;
import com.vitalwatch.center.platform.incidents.interfaces.rest.resources.ResolveIncidentResource;
import com.vitalwatch.center.platform.incidents.interfaces.rest.transform.AcknowledgeIncidentCommandFromResourceAssembler;
import com.vitalwatch.center.platform.incidents.interfaces.rest.transform.CancelIncidentCommandFromResourceAssembler;
import com.vitalwatch.center.platform.incidents.interfaces.rest.transform.CreateIncidentCommandFromResourceAssembler;
import com.vitalwatch.center.platform.incidents.interfaces.rest.transform.EscalateIncidentCommandFromResourceAssembler;
import com.vitalwatch.center.platform.incidents.interfaces.rest.transform.IncidentResourceFromEntityAssembler;
import com.vitalwatch.center.platform.incidents.interfaces.rest.transform.ResolveIncidentCommandFromResourceAssembler;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
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
 * REST controller for incidents and escalation.
 */
@RestController
@RequestMapping(value = "/api/v1/incidents", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Incidents", description = "Incident and escalation management endpoints")
public class IncidentsController {

    private final IncidentCommandService incidentCommandService;
    private final IncidentQueryService incidentQueryService;

    public IncidentsController(
            IncidentCommandService incidentCommandService,
            IncidentQueryService incidentQueryService
    ) {
        this.incidentCommandService = incidentCommandService;
        this.incidentQueryService = incidentQueryService;
    }

    @PostMapping
    @Operation(summary = "Create incident", description = "Creates an incident from a clinical risk situation, manual report, or system alert.")
    public ResponseEntity<?> createIncident(@Valid @RequestBody CreateIncidentResource resource) {
        var command = CreateIncidentCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = incidentCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                IncidentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{incidentId}")
    @Operation(summary = "Get incident by id", description = "Retrieves an incident by id.")
    public ResponseEntity<?> getIncidentById(@PathVariable @Positive Long incidentId) {
        var incident = incidentQueryService.handle(new GetIncidentByIdQuery(incidentId));

        if (incident.isEmpty()) {
            var error = ApplicationError.notFound("Incident", incidentId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = IncidentResourceFromEntityAssembler.toResourceFromEntity(incident.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping(params = "hospitalWorkspaceId")
    @Operation(summary = "Get incidents by hospital workspace", description = "Retrieves incidents for a hospital workspace.")
    public ResponseEntity<List<IncidentResource>> getIncidentsByHospitalWorkspaceId(
            @RequestParam @Positive Long hospitalWorkspaceId
    ) {
        var incidents = incidentQueryService.handle(new GetIncidentsByHospitalWorkspaceIdQuery(hospitalWorkspaceId));

        var resources = incidents.stream()
                .map(IncidentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/open", params = "hospitalWorkspaceId")
    @Operation(summary = "Get open incidents by hospital workspace", description = "Retrieves open incidents for a hospital workspace.")
    public ResponseEntity<List<IncidentResource>> getOpenIncidentsByHospitalWorkspaceId(
            @RequestParam @Positive Long hospitalWorkspaceId
    ) {
        var incidents = incidentQueryService.handle(new GetOpenIncidentsByHospitalWorkspaceIdQuery(hospitalWorkspaceId));

        var resources = incidents.stream()
                .map(IncidentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/reported-user", params = "reportedUserAccountId")
    @Operation(summary = "Get incidents by reported user", description = "Retrieves incidents associated with a reported user account.")
    public ResponseEntity<List<IncidentResource>> getIncidentsByReportedUserAccountId(
            @RequestParam @Positive Long reportedUserAccountId
    ) {
        var incidents = incidentQueryService.handle(new GetIncidentsByReportedUserAccountIdQuery(reportedUserAccountId));

        var resources = incidents.stream()
                .map(IncidentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{incidentId}/acknowledge")
    @Operation(summary = "Acknowledge incident", description = "Marks an incident as acknowledged.")
    public ResponseEntity<?> acknowledgeIncident(
            @PathVariable @Positive Long incidentId,
            @Valid @RequestBody AcknowledgeIncidentResource resource
    ) {
        var command = AcknowledgeIncidentCommandFromResourceAssembler.toCommandFromResource(incidentId, resource);
        var result = incidentCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                IncidentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{incidentId}/escalate")
    @Operation(summary = "Escalate incident", description = "Escalates a high or critical incident.")
    public ResponseEntity<?> escalateIncident(
            @PathVariable @Positive Long incidentId,
            @Valid @RequestBody EscalateIncidentResource resource
    ) {
        var command = EscalateIncidentCommandFromResourceAssembler.toCommandFromResource(incidentId, resource);
        var result = incidentCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                IncidentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{incidentId}/resolve")
    @Operation(summary = "Resolve incident", description = "Marks an incident as resolved.")
    public ResponseEntity<?> resolveIncident(
            @PathVariable @Positive Long incidentId,
            @Valid @RequestBody ResolveIncidentResource resource
    ) {
        var command = ResolveIncidentCommandFromResourceAssembler.toCommandFromResource(incidentId, resource);
        var result = incidentCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                IncidentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{incidentId}/cancel")
    @Operation(summary = "Cancel incident", description = "Cancels an incident.")
    public ResponseEntity<?> cancelIncident(
            @PathVariable @Positive Long incidentId,
            @Valid @RequestBody CancelIncidentResource resource
    ) {
        var command = CancelIncidentCommandFromResourceAssembler.toCommandFromResource(incidentId, resource);
        var result = incidentCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                IncidentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}