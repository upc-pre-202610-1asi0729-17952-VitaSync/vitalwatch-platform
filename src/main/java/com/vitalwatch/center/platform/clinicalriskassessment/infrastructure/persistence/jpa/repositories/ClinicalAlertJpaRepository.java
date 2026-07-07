package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.ClinicalAlertStatus;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.ClinicalAlertJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
    DELETE ca
    FROM clinical_alerts ca
    LEFT JOIN (
        SELECT latest_alerts.id
        FROM (
            SELECT id
            FROM clinical_alerts
            WHERE organization_id = :organizationId
              AND user_id = :userId
            ORDER BY 
                CASE WHEN status = 'ACTIVE' THEN 0 ELSE 1 END,
                created_at DESC,
                id DESC
            LIMIT :retentionLimit
        ) latest_alerts
    ) retained ON retained.id = ca.id
    WHERE ca.organization_id = :organizationId
      AND ca.user_id = :userId
      AND retained.id IS NULL
    """, nativeQuery = true)
    int deleteOlderThanLatestByOrganizationAndUser(
            @Param("organizationId") Long organizationId,
            @Param("userId") Long userId,
            @Param("retentionLimit") int retentionLimit
    );
}