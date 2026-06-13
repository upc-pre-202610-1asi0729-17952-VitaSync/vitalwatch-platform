package com.vitalwatch.center.platform.clinicalrisk.application.commandservices;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.CreateClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.EscalateClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.RegisterVitalSignReadingCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.ReviewClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;

/**
 * Application service contract for clinical risk commands.
 */
public interface ClinicalRiskCommandService {

    Result<VitalSignReading, ApplicationError> handle(RegisterVitalSignReadingCommand command);

    Result<ClinicalRiskAssessment, ApplicationError> handle(CreateClinicalRiskAssessmentCommand command);

    Result<ClinicalRiskAssessment, ApplicationError> handle(ReviewClinicalRiskAssessmentCommand command);

    Result<ClinicalRiskAssessment, ApplicationError> handle(EscalateClinicalRiskAssessmentCommand command);
}