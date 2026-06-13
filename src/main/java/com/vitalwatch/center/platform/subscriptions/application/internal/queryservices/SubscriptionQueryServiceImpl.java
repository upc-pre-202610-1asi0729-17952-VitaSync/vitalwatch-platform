package com.vitalwatch.center.platform.subscriptions.application.internal.queryservices;

import com.vitalwatch.center.platform.subscriptions.application.queryservices.SubscriptionQueryService;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetAllSubscriptionPlansQuery;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionByIdQuery;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionPlanByIdQuery;
import com.vitalwatch.center.platform.subscriptions.domain.repositories.HospitalSubscriptionRepository;
import com.vitalwatch.center.platform.subscriptions.domain.repositories.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Subscription query service implementation.
 */
@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final HospitalSubscriptionRepository hospitalSubscriptionRepository;

    public SubscriptionQueryServiceImpl(
            SubscriptionPlanRepository subscriptionPlanRepository,
            HospitalSubscriptionRepository hospitalSubscriptionRepository
    ) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.hospitalSubscriptionRepository = hospitalSubscriptionRepository;
    }

    @Override
    public List<SubscriptionPlan> handle(GetAllSubscriptionPlansQuery query) {
        return subscriptionPlanRepository.findAll();
    }

    @Override
    public Optional<SubscriptionPlan> handle(GetSubscriptionPlanByIdQuery query) {
        return subscriptionPlanRepository.findById(query.subscriptionPlanId());
    }

    @Override
    public Optional<HospitalSubscription> handle(GetSubscriptionByIdQuery query) {
        return hospitalSubscriptionRepository.findById(query.subscriptionId());
    }

    @Override
    public Optional<HospitalSubscription> handle(GetSubscriptionByHospitalWorkspaceIdQuery query) {
        return hospitalSubscriptionRepository.findByHospitalWorkspaceId(query.hospitalWorkspaceId());
    }
}