package com.vitalwatch.center.platform.clinicalrisk.interfaces.rest;

import com.vitalwatch.center.platform.clinicalrisk.application.commandservices.ClinicalRiskCommandService;
import com.vitalwatch.center.platform.clinicalrisk.application.queryservices.ClinicalRiskQueryService;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.EscalateClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.ReviewClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetClinicalRiskAssessmentByIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetClinicalRiskAssessmentsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetLatestClinicalRiskAssessmentByUserAccountIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources.ClinicalRiskAssessmentResource;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.resources.CreateClinicalRiskAssessmentResource;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.transform.ClinicalRiskAssessmentResourceFromEntityAssembler;
import com.vitalwatch.center.platform.clinicalrisk.interfaces.rest.transform.CreateClinicalRiskAssessmentCommandFromResourceAssembler;
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
 * REST controller for clinical risk assessments.
 */
@RestController
@RequestMapping(value = "/api/v1/clinical-risk-assessments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Clinical Risk Assessments", description = "Fatigue and clinical risk assessment endpoints")
public class ClinicalRiskAssessmentsController {

    private final ClinicalRiskCommandService clinicalRiskCommandService;
    private final ClinicalRiskQueryService clinicalRiskQueryService;

    public ClinicalRiskAssessmentsController(
            ClinicalRiskCommandService clinicalRiskCommandService,
            ClinicalRiskQueryService clinicalRiskQueryService
    ) {
        this.clinicalRiskCommandService = clinicalRiskCommandService;
        this.clinicalRiskQueryService = clinicalRiskQueryService;
    }

    @PostMapping
    @Operation(summary = "Create clinical risk assessment", description = "Creates a fatigue and clinical risk assessment from a vital sign reading.")
    public ResponseEntity<?> createClinicalRiskAssessment(@Valid @RequestBody CreateClinicalRiskAssessmentResource resource) {
        var command = CreateClinicalRiskAssessmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = clinicalRiskCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ClinicalRiskAssessmentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{clinicalRiskAssessmentId}")
    @Operation(summary = "Get clinical risk assessment by id", description = "Retrieves a clinical risk assessment by id.")
    public ResponseEntity<?> getClinicalRiskAssessmentById(
            @PathVariable @Positive Long clinicalRiskAssessmentId
    ) {
        var assessment = clinicalRiskQueryService.handle(
                new GetClinicalRiskAssessmentByIdQuery(clinicalRiskAssessmentId)
        );

        if (assessment.isEmpty()) {
            var error = ApplicationError.notFound("ClinicalRiskAssessment", clinicalRiskAssessmentId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = ClinicalRiskAssessmentResourceFromEntityAssembler.toResourceFromEntity(assessment.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping(params = "hospitalWorkspaceId")
    @Operation(summary = "Get assessments by hospital workspace", description = "Retrieves clinical risk assessments for a hospital workspace.")
    public ResponseEntity<List<ClinicalRiskAssessmentResource>> getClinicalRiskAssessmentsByHospitalWorkspaceId(
            @RequestParam @Positive Long hospitalWorkspaceId
    ) {
        var assessments = clinicalRiskQueryService.handle(
                new GetClinicalRiskAssessmentsByHospitalWorkspaceIdQuery(hospitalWorkspaceId)
        );

        var resources = assessments.stream()
                .map(ClinicalRiskAssessmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/latest", params = "userAccountId")
    @Operation(summary = "Get latest assessment by user account", description = "Retrieves the latest clinical risk assessment for a user account.")
    public ResponseEntity<?> getLatestClinicalRiskAssessmentByUserAccountId(
            @RequestParam @Positive Long userAccountId
    ) {
        var assessment = clinicalRiskQueryService.handle(
                new GetLatestClinicalRiskAssessmentByUserAccountIdQuery(userAccountId)
        );

        if (assessment.isEmpty()) {
            var error = ApplicationError.notFound("ClinicalRiskAssessment", userAccountId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = ClinicalRiskAssessmentResourceFromEntityAssembler.toResourceFromEntity(assessment.get());
        return ResponseEntity.ok(resource);
    }

    @PatchMapping("/{clinicalRiskAssessmentId}/review")
    @Operation(summary = "Review clinical risk assessment", description = "Marks a clinical risk assessment as reviewed.")
    public ResponseEntity<?> reviewClinicalRiskAssessment(
            @PathVariable @Positive Long clinicalRiskAssessmentId
    ) {
        var result = clinicalRiskCommandService.handle(
                new ReviewClinicalRiskAssessmentCommand(clinicalRiskAssessmentId)
        );

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ClinicalRiskAssessmentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{clinicalRiskAssessmentId}/escalate")
    @Operation(summary = "Escalate clinical risk assessment", description = "Escalates a high or critical clinical risk assessment.")
    public ResponseEntity<?> escalateClinicalRiskAssessment(
            @PathVariable @Positive Long clinicalRiskAssessmentId
    ) {
        var result = clinicalRiskCommandService.handle(
                new EscalateClinicalRiskAssessmentCommand(clinicalRiskAssessmentId)
        );

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ClinicalRiskAssessmentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}