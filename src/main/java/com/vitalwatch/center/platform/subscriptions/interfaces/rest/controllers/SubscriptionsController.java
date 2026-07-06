package com.vitalwatch.center.platform.subscriptions.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionJpaEntity;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.PlanJpaRepository;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.SubscriptionJpaRepository;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.CreateSubscriptionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.SubscriptionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for subscription operations.
 */
@RestController
@RequestMapping("/subscriptions")
@Tag(name = "Subscriptions", description = "Organization subscription endpoints")
public class SubscriptionsController {

    private final SubscriptionJpaRepository subscriptionRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final PlanJpaRepository planRepository;
    private final MessageResolver messageResolver;

    public SubscriptionsController(
            SubscriptionJpaRepository subscriptionRepository,
            OrganizationJpaRepository organizationRepository,
            PlanJpaRepository planRepository,
            MessageResolver messageResolver
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.organizationRepository = organizationRepository;
        this.planRepository = planRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all subscriptions")
    public ResponseEntity<List<SubscriptionResource>> getAllSubscriptions() {
        var subscriptions = subscriptionRepository.findAll()
                .stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{subscriptionId}")
    @Operation(summary = "Get subscription by id")
    public ResponseEntity<?> getSubscriptionById(@PathVariable Long subscriptionId) {
        var subscription = subscriptionRepository.findById(subscriptionId);

        if (subscription.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("subscriptions.subscription.notFound")
                    )
            );
        }

        var resource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(
                subscription.get()
        );

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get subscriptions by organization id")
    public ResponseEntity<List<SubscriptionResource>> getSubscriptionsByOrganizationId(
            @PathVariable Long organizationId
    ) {
        var subscriptions = subscriptionRepository.findByOrganization_Id(organizationId)
                .stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(subscriptions);
    }

    @PostMapping
    @Operation(summary = "Create subscription")
    public ResponseEntity<?> createSubscription(
            @Valid @RequestBody CreateSubscriptionResource resource
    ) {
        var organization = organizationRepository.findById(resource.organizationId());

        if (organization.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.organization.notFound")
                    )
            );
        }

        var plan = planRepository.findById(resource.planId());

        if (plan.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("subscriptions.plan.notFound")
                    )
            );
        }

        if (subscriptionRepository.existsByOrganization_IdAndStatus(
                resource.organizationId(),
                SubscriptionStatus.ACTIVE
        )) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict(
                            messageResolver.get("subscriptions.subscription.activeConflict")
                    )
            );
        }

        var startedAt = resource.startedAt() == null
                ? LocalDate.now()
                : resource.startedAt();

        var expiresAt = resource.expiresAt() == null
                ? startedAt.plusMonths(1)
                : resource.expiresAt();

        var subscription = new SubscriptionJpaEntity(
                organization.get(),
                plan.get(),
                startedAt,
                expiresAt
        );

        var savedSubscription = subscriptionRepository.save(subscription);

        var subscriptionResource = SubscriptionResourceFromEntityAssembler
                .toResourceFromEntity(savedSubscription);

        return ResponseEntity
                .created(URI.create("/subscriptions/" + savedSubscription.getId()))
                .body(subscriptionResource);
    }
}