package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.RiskAssessmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for risk assessment persistence.
 */
public interface RiskAssessmentJpaRepository extends JpaRepository<RiskAssessmentJpaEntity, Long> {

    List<RiskAssessmentJpaEntity> findByOrganizationIdOrderByLastUpdatedAtDesc(Long organizationId);

    List<RiskAssessmentJpaEntity> findByOrganizationIdAndUserIdOrderByLastUpdatedAtDesc(
            Long organizationId,
            Long userId
    );

    Optional<RiskAssessmentJpaEntity> findFirstByOrganizationIdAndUserIdOrderByLastUpdatedAtDesc(
            Long organizationId,
            Long userId
    );
}