package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryPlan;
import com.vitalwatch.center.platform.staffrecovery.domain.repositories.RecoveryPlanRepository;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.assemblers.RecoveryPlanPersistenceAssembler;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.repositories.RecoveryPlanPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for RecoveryPlan.
 */
@Repository
public class RecoveryPlanRepositoryImpl implements RecoveryPlanRepository {

    private final RecoveryPlanPersistenceRepository repository;

    public RecoveryPlanRepositoryImpl(RecoveryPlanPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecoveryPlan> findById(Long id) {
        return repository.findById(id)
                .map(RecoveryPlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<RecoveryPlan> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findAllByHospitalWorkspaceIdOrderByPlanCreatedAtDesc(hospitalWorkspaceId)
                .stream()
                .map(RecoveryPlanPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<RecoveryPlan> findAllByUserAccountId(Long userAccountId) {
        return repository.findAllByUserAccountIdOrderByPlanCreatedAtDesc(userAccountId)
                .stream()
                .map(RecoveryPlanPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public RecoveryPlan save(RecoveryPlan recoveryPlan) {
        var savedEntity = repository.save(
                RecoveryPlanPersistenceAssembler.toPersistenceFromDomain(recoveryPlan)
        );

        return RecoveryPlanPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }
}