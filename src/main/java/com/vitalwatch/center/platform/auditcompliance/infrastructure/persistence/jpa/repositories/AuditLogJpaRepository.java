package com.vitalwatch.center.platform.auditcompliance.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogSeverity;
import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogType;
import com.vitalwatch.center.platform.auditcompliance.infrastructure.persistence.jpa.entities.AuditLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for audit log persistence.
 */
public interface AuditLogJpaRepository extends JpaRepository<AuditLogJpaEntity, Long> {

    List<AuditLogJpaEntity> findByOrganizationIdOrderByCreatedAtDesc(Long organizationId);

    List<AuditLogJpaEntity> findByOrganizationIdAndActorUserIdOrderByCreatedAtDesc(
            Long organizationId,
            Long actorUserId
    );

    List<AuditLogJpaEntity> findByOrganizationIdAndTypeOrderByCreatedAtDesc(
            Long organizationId,
            AuditLogType type
    );

    List<AuditLogJpaEntity> findByOrganizationIdAndSeverityOrderByCreatedAtDesc(
            Long organizationId,
            AuditLogSeverity severity
    );

    boolean existsByOrganizationId(Long organizationId);
}