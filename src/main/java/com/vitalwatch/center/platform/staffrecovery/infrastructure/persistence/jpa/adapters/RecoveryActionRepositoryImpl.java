package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryAction;
import com.vitalwatch.center.platform.staffrecovery.domain.repositories.RecoveryActionRepository;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.assemblers.RecoveryActionPersistenceAssembler;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.repositories.RecoveryActionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for RecoveryAction.
 */
@Repository
public class RecoveryActionRepositoryImpl implements RecoveryActionRepository {

    private final RecoveryActionPersistenceRepository repository;

    public RecoveryActionRepositoryImpl(RecoveryActionPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecoveryAction> findById(Long id) {
        return repository.findById(id)
                .map(RecoveryActionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<RecoveryAction> findAllByRecoveryPlanId(Long recoveryPlanId) {
        return repository.findAllByRecoveryPlanIdOrderByActionCreatedAtDesc(recoveryPlanId)
                .stream()
                .map(RecoveryActionPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public RecoveryAction save(RecoveryAction recoveryAction) {
        var savedEntity = repository.save(
                RecoveryActionPersistenceAssembler.toPersistenceFromDomain(recoveryAction)
        );

        return RecoveryActionPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }
}