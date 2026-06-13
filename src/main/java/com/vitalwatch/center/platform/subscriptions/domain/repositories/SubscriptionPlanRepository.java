package com.vitalwatch.center.platform.subscriptions.domain.repositories;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for subscription plans.
 */
public interface SubscriptionPlanRepository {

    Optional<SubscriptionPlan> findById(Long id);

    Optional<SubscriptionPlan> findByCode(SubscriptionPlanCode code);

    List<SubscriptionPlan> findAll();

    SubscriptionPlan save(SubscriptionPlan subscriptionPlan);

    boolean existsByCode(SubscriptionPlanCode code);
}