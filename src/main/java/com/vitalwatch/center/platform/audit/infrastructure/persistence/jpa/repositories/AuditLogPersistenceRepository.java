package com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.entities.AuditLogPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for audit logs.
 */
public interface AuditLogPersistenceRepository extends JpaRepository<AuditLogPersistenceEntity, Long> {

    List<AuditLogPersistenceEntity> findAllByHospitalWorkspaceIdOrderByOccurredAtDesc(Long hospitalWorkspaceId);

    List<AuditLogPersistenceEntity> findAllByActorUserAccountIdOrderByOccurredAtDesc(Long actorUserAccountId);
}