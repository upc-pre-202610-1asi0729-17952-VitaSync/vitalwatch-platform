package com.vitalwatch.center.platform.subscriptions.application.internal.commandservices;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import com.vitalwatch.center.platform.subscriptions.application.commandservices.SubscriptionCommandService;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.CancelSubscriptionCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.ChangeSubscriptionPlanCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.CreateSubscriptionPlanCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.SubscribeHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.subscriptions.domain.repositories.HospitalSubscriptionRepository;
import com.vitalwatch.center.platform.subscriptions.domain.repositories.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Subscription command service implementation.
 */
@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final HospitalSubscriptionRepository hospitalSubscriptionRepository;

    public SubscriptionCommandServiceImpl(
            SubscriptionPlanRepository subscriptionPlanRepository,
            HospitalSubscriptionRepository hospitalSubscriptionRepository
    ) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.hospitalSubscriptionRepository = hospitalSubscriptionRepository;
    }

    @Override
    @Transactional
    public Result<SubscriptionPlan, ApplicationError> handle(CreateSubscriptionPlanCommand command) {
        try {
            if (subscriptionPlanRepository.existsByCode(command.code())) {
                return Result.failure(ApplicationError.conflict(
                        "SubscriptionPlan",
                        "A subscription plan with code '%s' already exists".formatted(command.code())
                ));
            }

            var subscriptionPlan = new SubscriptionPlan(command);
            var savedPlan = subscriptionPlanRepository.save(subscriptionPlan);

            return Result.success(savedPlan);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("SubscriptionPlan", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Subscription plan creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<HospitalSubscription, ApplicationError> handle(SubscribeHospitalWorkspaceCommand command) {
        try {
            if (command.hospitalWorkspaceId() == null || command.hospitalWorkspaceId() <= 0) {
                return Result.failure(ApplicationError.validationError(
                        "hospitalWorkspaceId",
                        "Hospital workspace id must be a positive number"
                ));
            }

            var subscriptionPlan = subscriptionPlanRepository.findById(command.subscriptionPlanId());

            if (subscriptionPlan.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "SubscriptionPlan",
                        command.subscriptionPlanId().toString()
                ));
            }

            if (!subscriptionPlan.get().isActive()) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "Inactive subscription plan",
                        "Hospital workspace cannot subscribe to an inactive subscription plan"
                ));
            }

            if (hospitalSubscriptionRepository.existsActiveByHospitalWorkspaceId(command.hospitalWorkspaceId())) {
                return Result.failure(ApplicationError.conflict(
                        "HospitalSubscription",
                        "Hospital workspace '%s' already has an active subscription".formatted(command.hospitalWorkspaceId())
                ));
            }

            var subscription = new HospitalSubscription(command);
            var savedSubscription = hospitalSubscriptionRepository.save(subscription);

            return Result.success(savedSubscription);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("HospitalSubscription", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Hospital subscription creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<HospitalSubscription, ApplicationError> handle(ChangeSubscriptionPlanCommand command) {
        try {
            var subscription = hospitalSubscriptionRepository.findById(command.subscriptionId());

            if (subscription.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "HospitalSubscription",
                        command.subscriptionId().toString()
                ));
            }

            var newPlan = subscriptionPlanRepository.findById(command.newSubscriptionPlanId());

            if (newPlan.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "SubscriptionPlan",
                        command.newSubscriptionPlanId().toString()
                ));
            }

            if (!newPlan.get().isActive()) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "Inactive subscription plan",
                        "Hospital subscription cannot change to an inactive subscription plan"
                ));
            }

            var hospitalSubscription = subscription.get();
            hospitalSubscription.changePlan(command.newSubscriptionPlanId());

            var savedSubscription = hospitalSubscriptionRepository.save(hospitalSubscription);

            return Result.success(savedSubscription);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("HospitalSubscription", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("HospitalSubscription", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Subscription plan change", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<HospitalSubscription, ApplicationError> handle(CancelSubscriptionCommand command) {
        try {
            var subscription = hospitalSubscriptionRepository.findById(command.subscriptionId());

            if (subscription.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "HospitalSubscription",
                        command.subscriptionId().toString()
                ));
            }

            var hospitalSubscription = subscription.get();
            hospitalSubscription.cancel();

            var savedSubscription = hospitalSubscriptionRepository.save(hospitalSubscription);

            return Result.success(savedSubscription);

        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("HospitalSubscription", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Subscription cancellation", exception.getMessage()));
        }
    }
}