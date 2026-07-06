package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.ClinicalAlertStatus;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.ClinicalAlertJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for clinical alert persistence.
 */
public interface ClinicalAlertJpaRepository extends JpaRepository<ClinicalAlertJpaEntity, Long> {

    List<ClinicalAlertJpaEntity> findByOrganizationIdOrderByCreatedAtDesc(Long organizationId);

    List<ClinicalAlertJpaEntity> findByOrganizationIdAndUserIdOrderByCreatedAtDesc(
            Long organizationId,
            Long userId
    );

    List<ClinicalAlertJpaEntity> findByOrganizationIdAndStatusOrderByCreatedAtDesc(
            Long organizationId,
            ClinicalAlertStatus status
    );
}