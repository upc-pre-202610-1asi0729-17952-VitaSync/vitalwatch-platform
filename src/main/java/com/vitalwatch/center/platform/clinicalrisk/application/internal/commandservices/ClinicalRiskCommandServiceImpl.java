package com.vitalwatch.center.platform.clinicalrisk.application.internal.commandservices;

import com.vitalwatch.center.platform.clinicalrisk.application.commandservices.ClinicalRiskCommandService;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.CreateClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.EscalateClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.RegisterVitalSignReadingCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.ReviewClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.ClinicalRiskAssessmentRepository;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.VitalSignReadingRepository;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clinical risk command service implementation.
 */
@Service
public class ClinicalRiskCommandServiceImpl implements ClinicalRiskCommandService {

    private final VitalSignReadingRepository vitalSignReadingRepository;
    private final ClinicalRiskAssessmentRepository clinicalRiskAssessmentRepository;

    public ClinicalRiskCommandServiceImpl(
            VitalSignReadingRepository vitalSignReadingRepository,
            ClinicalRiskAssessmentRepository clinicalRiskAssessmentRepository
    ) {
        this.vitalSignReadingRepository = vitalSignReadingRepository;
        this.clinicalRiskAssessmentRepository = clinicalRiskAssessmentRepository;
    }

    @Override
    @Transactional
    public Result<VitalSignReading, ApplicationError> handle(RegisterVitalSignReadingCommand command) {
        try {
            var reading = new VitalSignReading(command);
            var savedReading = vitalSignReadingRepository.save(reading);

            return Result.success(savedReading);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("VitalSignReading", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Vital sign reading registration", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ClinicalRiskAssessment, ApplicationError> handle(CreateClinicalRiskAssessmentCommand command) {
        try {
            var reading = vitalSignReadingRepository.findById(command.vitalSignReadingId());

            if (reading.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "VitalSignReading",
                        command.vitalSignReadingId().toString()
                ));
            }

            var vitalSignReading = reading.get();

            if (!vitalSignReading.getHospitalWorkspaceId().equals(command.hospitalWorkspaceId())) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "Hospital workspace mismatch",
                        "Vital sign reading does not belong to the provided hospital workspace"
                ));
            }

            if (!vitalSignReading.getUserAccountId().equals(command.userAccountId())) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "User account mismatch",
                        "Vital sign reading does not belong to the provided user account"
                ));
            }

            var assessment = new ClinicalRiskAssessment(command, vitalSignReading);
            var savedAssessment = clinicalRiskAssessmentRepository.save(assessment);

            return Result.success(savedAssessment);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("ClinicalRiskAssessment", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Clinical risk assessment creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ClinicalRiskAssessment, ApplicationError> handle(ReviewClinicalRiskAssessmentCommand command) {
        try {
            var assessment = clinicalRiskAssessmentRepository.findById(command.clinicalRiskAssessmentId());

            if (assessment.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "ClinicalRiskAssessment",
                        command.clinicalRiskAssessmentId().toString()
                ));
            }

            var clinicalRiskAssessment = assessment.get();
            clinicalRiskAssessment.review();

            var savedAssessment = clinicalRiskAssessmentRepository.save(clinicalRiskAssessment);

            return Result.success(savedAssessment);

        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("ClinicalRiskAssessment", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Clinical risk assessment review", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ClinicalRiskAssessment, ApplicationError> handle(EscalateClinicalRiskAssessmentCommand command) {
        try {
            var assessment = clinicalRiskAssessmentRepository.findById(command.clinicalRiskAssessmentId());

            if (assessment.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "ClinicalRiskAssessment",
                        command.clinicalRiskAssessmentId().toString()
                ));
            }

            var clinicalRiskAssessment = assessment.get();
            clinicalRiskAssessment.escalate();

            var savedAssessment = clinicalRiskAssessmentRepository.save(clinicalRiskAssessment);

            return Result.success(savedAssessment);

        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("ClinicalRiskAssessment", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Clinical risk assessment escalation", exception.getMessage()));
        }
    }
}