package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.VitalSignAnomalyStatus;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.VitalSignAnomalyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
    DELETE vsa
    FROM vital_sign_anomalies vsa
    LEFT JOIN (
        SELECT latest_anomalies.id
        FROM (
            SELECT id
            FROM vital_sign_anomalies
            WHERE organization_id = :organizationId
              AND user_id = :userId
            ORDER BY 
                CASE WHEN status = 'OPEN' THEN 0 ELSE 1 END,
                detected_at DESC,
                id DESC
            LIMIT :retentionLimit
        ) latest_anomalies
    ) retained ON retained.id = vsa.id
    WHERE vsa.organization_id = :organizationId
      AND vsa.user_id = :userId
      AND retained.id IS NULL
    """, nativeQuery = true)
    int deleteOlderThanLatestByOrganizationAndUser(
            @Param("organizationId") Long organizationId,
            @Param("userId") Long userId,
            @Param("retentionLimit") int retentionLimit
    );
}