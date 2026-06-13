package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.HospitalSubscriptionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for hospital subscriptions.
 */
public interface HospitalSubscriptionPersistenceRepository extends JpaRepository<HospitalSubscriptionPersistenceEntity, Long> {

    Optional<HospitalSubscriptionPersistenceEntity> findFirstByHospitalWorkspaceIdOrderByStartedAtDesc(Long hospitalWorkspaceId);

    long countByHospitalWorkspaceIdAndStatus(Long hospitalWorkspaceId, SubscriptionStatus status);
}