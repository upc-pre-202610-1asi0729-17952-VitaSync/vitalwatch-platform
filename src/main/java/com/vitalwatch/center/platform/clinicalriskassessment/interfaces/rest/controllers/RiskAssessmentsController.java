package com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.controllers;

import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.RiskAssessmentJpaEntity;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories.RiskAssessmentJpaRepository;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.RiskAssessmentResource;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.transform.RiskAssessmentResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * REST controller for risk assessments.
 */
@RestController
@RequestMapping("/riskAssessments")
@Tag(name = "Risk Assessments", description = "Clinical risk assessment endpoints")
public class RiskAssessmentsController {

    private final RiskAssessmentJpaRepository riskAssessmentRepository;
    private final MessageResolver messageResolver;

    public RiskAssessmentsController(
            RiskAssessmentJpaRepository riskAssessmentRepository,
            MessageResolver messageResolver
    ) {
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all risk assessments or filter by organization and user")
    public ResponseEntity<List<RiskAssessmentResource>> getRiskAssessments(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long userId
    ) {
        var assessments = organizationId != null && userId != null
                ? riskAssessmentRepository.findByOrganizationIdAndUserIdOrderByLastUpdatedAtDesc(
                organizationId,
                userId
        )
                : organizationId != null
                  ? riskAssessmentRepository.findByOrganizationIdOrderByLastUpdatedAtDesc(organizationId)
                  : riskAssessmentRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(RiskAssessmentJpaEntity::getLastUpdatedAt).reversed())
                .toList();

        var resources = assessments.stream()
                .map(RiskAssessmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{riskAssessmentId}")
    @Operation(summary = "Get risk assessment by id")
    public ResponseEntity<?> getRiskAssessmentById(@PathVariable Long riskAssessmentId) {
        var assessment = riskAssessmentRepository.findById(riskAssessmentId);

        if (assessment.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("clinical.riskAssessment.notFound")
                    )
            );
        }

        return ResponseEntity.ok(
                RiskAssessmentResourceFromEntityAssembler.toResourceFromEntity(assessment.get())
        );
    }
}