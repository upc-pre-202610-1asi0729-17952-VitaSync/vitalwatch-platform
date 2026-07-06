package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.VitalSignAnomalyStatus;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.VitalSignAnomalyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for vital sign anomaly persistence.
 */
public interface VitalSignAnomalyJpaRepository extends JpaRepository<VitalSignAnomalyJpaEntity, Long> {

    List<VitalSignAnomalyJpaEntity> findByOrganizationIdOrderByDetectedAtDesc(Long organizationId);

    List<VitalSignAnomalyJpaEntity> findByOrganizationIdAndUserIdOrderByDetectedAtDesc(
            Long organizationId,
            Long userId
    );

    List<VitalSignAnomalyJpaEntity> findByOrganizationIdAndStatusOrderByDetectedAtDesc(
            Long organizationId,
            VitalSignAnomalyStatus status
    );
}