package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.VitalSignReadingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        DELETE vsr
        FROM vital_sign_readings vsr
        LEFT JOIN (
            SELECT latest_readings.id
            FROM (
                SELECT id
                FROM vital_sign_readings
                WHERE organization_id = :organizationId
                  AND user_id = :userId
                ORDER BY recorded_at DESC, id DESC
                LIMIT :retentionLimit
            ) latest_readings
        ) retained ON retained.id = vsr.id
        WHERE vsr.organization_id = :organizationId
          AND vsr.user_id = :userId
          AND retained.id IS NULL
        """, nativeQuery = true)
    int deleteOlderThanLatestByOrganizationAndUser(
            @Param("organizationId") Long organizationId,
            @Param("userId") Long userId,
            @Param("retentionLimit") int retentionLimit
    );
}