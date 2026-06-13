package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;
import com.vitalwatch.center.platform.subscriptions.domain.repositories.HospitalSubscriptionRepository;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.assemblers.HospitalSubscriptionPersistenceAssembler;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.HospitalSubscriptionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for HospitalSubscription.
 */
@Repository
public class HospitalSubscriptionRepositoryImpl implements HospitalSubscriptionRepository {

    private final HospitalSubscriptionPersistenceRepository repository;

    public HospitalSubscriptionRepositoryImpl(HospitalSubscriptionPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<HospitalSubscription> findById(Long id) {
        return repository.findById(id)
                .map(HospitalSubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<HospitalSubscription> findByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findFirstByHospitalWorkspaceIdOrderByStartedAtDesc(hospitalWorkspaceId)
                .map(HospitalSubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<HospitalSubscription> findAll() {
        return repository.findAll()
                .stream()
                .map(HospitalSubscriptionPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public HospitalSubscription save(HospitalSubscription hospitalSubscription) {
        var savedEntity = repository.save(
                HospitalSubscriptionPersistenceAssembler.toPersistenceFromDomain(hospitalSubscription)
        );
        return HospitalSubscriptionPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }

    @Override
    public boolean existsActiveByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.countByHospitalWorkspaceIdAndStatus(
                hospitalWorkspaceId,
                SubscriptionStatus.ACTIVE
        ) > 0;
    }
}