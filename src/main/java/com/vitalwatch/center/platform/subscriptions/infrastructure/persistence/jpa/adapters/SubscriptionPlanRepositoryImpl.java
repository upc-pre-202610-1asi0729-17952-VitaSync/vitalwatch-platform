package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;
import com.vitalwatch.center.platform.subscriptions.domain.repositories.SubscriptionPlanRepository;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.assemblers.SubscriptionPlanPersistenceAssembler;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.SubscriptionPlanPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for SubscriptionPlan.
 */
@Repository
public class SubscriptionPlanRepositoryImpl implements SubscriptionPlanRepository {

    private final SubscriptionPlanPersistenceRepository repository;

    public SubscriptionPlanRepositoryImpl(SubscriptionPlanPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<SubscriptionPlan> findById(Long id) {
        return repository.findById(id)
                .map(SubscriptionPlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<SubscriptionPlan> findByCode(SubscriptionPlanCode code) {
        return repository.findByCode(code)
                .map(SubscriptionPlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<SubscriptionPlan> findAll() {
        return repository.findAll()
                .stream()
                .map(SubscriptionPlanPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public SubscriptionPlan save(SubscriptionPlan subscriptionPlan) {
        var savedEntity = repository.save(
                SubscriptionPlanPersistenceAssembler.toPersistenceFromDomain(subscriptionPlan)
        );
        return SubscriptionPlanPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }

    @Override
    public boolean existsByCode(SubscriptionPlanCode code) {
        return repository.countByCode(code) > 0;
    }
}