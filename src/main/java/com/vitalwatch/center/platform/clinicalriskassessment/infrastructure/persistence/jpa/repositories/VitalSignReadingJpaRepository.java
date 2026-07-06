package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.VitalSignReadingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for vital sign reading persistence.
 */
public interface VitalSignReadingJpaRepository extends JpaRepository<VitalSignReadingJpaEntity, Long> {

    List<VitalSignReadingJpaEntity> findByOrganizationIdOrderByRecordedAtDesc(Long organizationId);

    List<VitalSignReadingJpaEntity> findByOrganizationIdAndUserIdOrderByRecordedAtDesc(
            Long organizationId,
            Long userId
    );
}