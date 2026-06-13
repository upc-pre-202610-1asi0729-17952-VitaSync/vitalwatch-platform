package com.vitalwatch.center.platform.clinicalrisk.application.queryservices;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetClinicalRiskAssessmentByIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetClinicalRiskAssessmentsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetLatestClinicalRiskAssessmentByUserAccountIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetVitalSignReadingsByUserAccountIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for clinical risk queries.
 */
public interface ClinicalRiskQueryService {

    Optional<ClinicalRiskAssessment> handle(GetClinicalRiskAssessmentByIdQuery query);

    Optional<ClinicalRiskAssessment> handle(GetLatestClinicalRiskAssessmentByUserAccountIdQuery query);

    List<ClinicalRiskAssessment> handle(GetClinicalRiskAssessmentsByHospitalWorkspaceIdQuery query);

    List<VitalSignReading> handle(GetVitalSignReadingsByUserAccountIdQuery query);
}