package com.vitalwatch.center.platform.audit.domain.repositories;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.AuditLog;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for audit logs.
 */
public interface AuditLogRepository {

    Optional<AuditLog> findById(Long id);

    List<AuditLog> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    List<AuditLog> findAllByActorUserAccountId(Long actorUserAccountId);

    AuditLog save(AuditLog auditLog);
}