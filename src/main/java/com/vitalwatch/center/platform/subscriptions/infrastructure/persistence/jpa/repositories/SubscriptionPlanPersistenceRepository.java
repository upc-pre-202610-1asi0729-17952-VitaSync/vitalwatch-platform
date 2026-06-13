package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for subscription plans.
 */
public interface SubscriptionPlanPersistenceRepository extends JpaRepository<SubscriptionPlanPersistenceEntity, Long> {

    Optional<SubscriptionPlanPersistenceEntity> findByCode(SubscriptionPlanCode code);

    long countByCode(SubscriptionPlanCode code);
}