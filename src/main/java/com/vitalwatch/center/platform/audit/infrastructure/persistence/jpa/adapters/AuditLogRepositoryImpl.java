package com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.AuditLog;
import com.vitalwatch.center.platform.audit.domain.repositories.AuditLogRepository;
import com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.assemblers.AuditLogPersistenceAssembler;
import com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.repositories.AuditLogPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for AuditLog.
 */
@Repository
public class AuditLogRepositoryImpl implements AuditLogRepository {

    private final AuditLogPersistenceRepository repository;

    public AuditLogRepositoryImpl(AuditLogPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AuditLog> findById(Long id) {
        return repository.findById(id)
                .map(AuditLogPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<AuditLog> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findAllByHospitalWorkspaceIdOrderByOccurredAtDesc(hospitalWorkspaceId)
                .stream()
                .map(AuditLogPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<AuditLog> findAllByActorUserAccountId(Long actorUserAccountId) {
        return repository.findAllByActorUserAccountIdOrderByOccurredAtDesc(actorUserAccountId)
                .stream()
                .map(AuditLogPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        var savedEntity = repository.save(
                AuditLogPersistenceAssembler.toPersistenceFromDomain(auditLog)
        );

        return AuditLogPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }
}