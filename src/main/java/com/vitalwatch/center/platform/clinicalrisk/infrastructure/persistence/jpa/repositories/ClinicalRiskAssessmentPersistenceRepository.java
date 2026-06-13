package com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.entities.ClinicalRiskAssessmentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for clinical risk assessments.
 */
public interface ClinicalRiskAssessmentPersistenceRepository extends JpaRepository<ClinicalRiskAssessmentPersistenceEntity, Long> {

    Optional<ClinicalRiskAssessmentPersistenceEntity> findFirstByUserAccountIdOrderByAssessedAtDesc(Long userAccountId);

    List<ClinicalRiskAssessmentPersistenceEntity> findAllByHospitalWorkspaceIdOrderByAssessedAtDesc(Long hospitalWorkspaceId);
}