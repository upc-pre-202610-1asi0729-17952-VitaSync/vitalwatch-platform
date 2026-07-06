package com.vitalwatch.center.platform.subscriptions.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.CheckoutSessionJpaEntity;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.PlanJpaEntity;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionJpaEntity;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.CheckoutSessionJpaRepository;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.PlanJpaRepository;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.SubscriptionJpaRepository;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.ActivateCheckoutSessionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.CheckoutSessionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.CreateCheckoutSessionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.SubscriptionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.CheckoutSessionResourceFromEntityAssembler;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for billing and checkout operations.
 */
@RestController
@RequestMapping("/billing")
@Tag(name = "Billing", description = "Billing and checkout endpoints")
public class BillingController {

    private final CheckoutSessionJpaRepository checkoutSessionRepository;
    private final PlanJpaRepository planRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final SubscriptionJpaRepository subscriptionRepository;
    private final MessageResolver messageResolver;

    public BillingController(
            CheckoutSessionJpaRepository checkoutSessionRepository,
            PlanJpaRepository planRepository,
            OrganizationJpaRepository organizationRepository,
            SubscriptionJpaRepository subscriptionRepository,
            MessageResolver messageResolver
    ) {
        this.checkoutSessionRepository = checkoutSessionRepository;
        this.planRepository = planRepository;
        this.organizationRepository = organizationRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.messageResolver = messageResolver;
    }

    @PostMapping("/create-checkout-session")
    @Operation(summary = "Create a checkout session")
    public ResponseEntity<?> createCheckoutSession(
            @Valid @RequestBody CreateCheckoutSessionResource resource
    ) {
        var plan = findPlan(resource);

        if (plan.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("subscriptions.plan.notFound")
                    )
            );
        }

        var organization = findOrganization(resource.organizationId());

        if (resource.organizationId() != null && organization.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.organization.notFound")
                    )
            );
        }

        var sessionId = "cs_test_" + UUID.randomUUID().toString().replace("-", "");
        var checkoutUrl = buildCheckoutUrl(resource.successUrl(), sessionId);

        var checkoutSession = new CheckoutSessionJpaEntity(
                sessionId,
                checkoutUrl,
                organization.orElse(null),
                plan.get(),
                resource.successUrl(),
                resource.cancelUrl()
        );

        var savedCheckoutSession = checkoutSessionRepository.save(checkoutSession);

        var checkoutSessionResource = CheckoutSessionResourceFromEntityAssembler
                .toResourceFromEntity(savedCheckoutSession);

        return ResponseEntity.ok(checkoutSessionResource);
    }

    @GetMapping("/checkout-session-status")
    @Operation(summary = "Get checkout session status")
    public ResponseEntity<?> getCheckoutSessionStatus(
            @RequestParam(required = false) String sessionId,
            @RequestParam(name = "session_id", required = false) String stripeStyleSessionId
    ) {
        var resolvedSessionId = resolveSessionId(sessionId, stripeStyleSessionId);

        if (resolvedSessionId == null || resolvedSessionId.isBlank()) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation(
                            messageResolver.get("subscriptions.checkoutSession.sessionIdRequired")
                    )
            );
        }

        var checkoutSession = checkoutSessionRepository.findBySessionId(resolvedSessionId);

        if (checkoutSession.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("subscriptions.checkoutSession.notFound")
                    )
            );
        }

        var resource = CheckoutSessionResourceFromEntityAssembler
                .toResourceFromEntity(checkoutSession.get());

        return ResponseEntity.ok(resource);
    }

    @PostMapping("/activate-checkout-session")
    @Operation(summary = "Activate subscription from checkout session")
    public ResponseEntity<?> activateCheckoutSession(
            @Valid @RequestBody ActivateCheckoutSessionResource resource
    ) {
        var resolvedSessionId = resolveSessionId(
                resource.sessionId(),
                resource.stripeSessionId()
        );

        if (resolvedSessionId == null || resolvedSessionId.isBlank()) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation(
                            messageResolver.get("subscriptions.checkoutSession.sessionIdRequired")
                    )
            );
        }

        var checkoutSession = checkoutSessionRepository.findBySessionId(resolvedSessionId);

        if (checkoutSession.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("subscriptions.checkoutSession.notFound")
                    )
            );
        }

        var organization = organizationRepository.findById(resource.organizationId());

        if (organization.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.organization.notFound")
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

        var savedCheckoutSession = checkoutSession.get();
        savedCheckoutSession.setOrganization(organization.get());
        savedCheckoutSession.complete();
        checkoutSessionRepository.save(savedCheckoutSession);

        var startedAt = LocalDate.now();
        var expiresAt = startedAt.plusMonths(1);

        var subscription = new SubscriptionJpaEntity(
                organization.get(),
                savedCheckoutSession.getPlan(),
                startedAt,
                expiresAt
        );

        var savedSubscription = subscriptionRepository.save(subscription);

        var subscriptionResource = SubscriptionResourceFromEntityAssembler
                .toResourceFromEntity(savedSubscription);

        return ResponseEntity.ok(subscriptionResource);
    }

    private Optional<PlanJpaEntity> findPlan(CreateCheckoutSessionResource resource) {
        if (resource.planId() != null) {
            return planRepository.findById(resource.planId());
        }

        if (resource.planCode() != null && !resource.planCode().isBlank()) {
            return planRepository.findByCode(resource.planCode());
        }

        return Optional.empty();
    }

    private Optional<OrganizationJpaEntity> findOrganization(Long organizationId) {
        if (organizationId == null) {
            return Optional.empty();
        }

        return organizationRepository.findById(organizationId);
    }

    private String resolveSessionId(String sessionId, String stripeStyleSessionId) {
        if (sessionId != null && !sessionId.isBlank()) {
            return sessionId;
        }

        return stripeStyleSessionId;
    }

    private String buildCheckoutUrl(String successUrl, String sessionId) {
        var separator = successUrl.contains("?") ? "&" : "?";
        return successUrl + separator + "session_id=" + sessionId;
    }
}