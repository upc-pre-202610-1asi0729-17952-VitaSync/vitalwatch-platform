package com.vitalwatch.center.platform.clinicalrisk.domain.repositories;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for clinical risk assessments.
 */
public interface ClinicalRiskAssessmentRepository {

    Optional<ClinicalRiskAssessment> findById(Long id);

    Optional<ClinicalRiskAssessment> findLatestByUserAccountId(Long userAccountId);

    List<ClinicalRiskAssessment> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    ClinicalRiskAssessment save(ClinicalRiskAssessment clinicalRiskAssessment);
}