package com.vitalwatch.center.platform.subscriptions.domain.repositories;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for hospital subscriptions.
 */
public interface HospitalSubscriptionRepository {

    Optional<HospitalSubscription> findById(Long id);

    Optional<HospitalSubscription> findByHospitalWorkspaceId(Long hospitalWorkspaceId);

    List<HospitalSubscription> findAll();

    HospitalSubscription save(HospitalSubscription hospitalSubscription);

    boolean existsActiveByHospitalWorkspaceId(Long hospitalWorkspaceId);
}