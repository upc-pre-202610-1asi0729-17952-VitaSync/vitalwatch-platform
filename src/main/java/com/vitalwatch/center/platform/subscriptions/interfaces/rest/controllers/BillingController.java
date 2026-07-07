package com.vitalwatch.center.platform.subscriptions.interfaces.rest.controllers;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.resources.MessageResource;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;
import com.vitalwatch.center.platform.subscriptions.infrastructure.billing.stripe.StripeCheckoutService;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.CheckoutSessionJpaEntity;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.PlanJpaEntity;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionJpaEntity;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.CheckoutSessionJpaRepository;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.PlanJpaRepository;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.SubscriptionJpaRepository;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.ActivateCheckoutSessionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.CreateCheckoutSessionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.SubscriptionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.CheckoutSessionResourceFromEntityAssembler;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final StripeCheckoutService stripeCheckoutService;

    public BillingController(
            CheckoutSessionJpaRepository checkoutSessionRepository,
            PlanJpaRepository planRepository,
            OrganizationJpaRepository organizationRepository,
            SubscriptionJpaRepository subscriptionRepository,
            MessageResolver messageResolver,
            StripeCheckoutService stripeCheckoutService
    ) {
        this.checkoutSessionRepository = checkoutSessionRepository;
        this.planRepository = planRepository;
        this.organizationRepository = organizationRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.messageResolver = messageResolver;
        this.stripeCheckoutService = stripeCheckoutService;
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

        try {
            var checkoutSession = stripeCheckoutService.isConfigured()
                    ? createStripeCheckoutSession(resource, plan.get(), organization.orElse(null))
                    : createSimulatedCheckoutSession(resource, plan.get(), organization.orElse(null));

            var savedCheckoutSession = checkoutSessionRepository.save(checkoutSession);

            var checkoutSessionResource = CheckoutSessionResourceFromEntityAssembler
                    .toResourceFromEntity(savedCheckoutSession);

            return ResponseEntity.ok(checkoutSessionResource);
        } catch (StripeException exception) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict(
                            "Stripe checkout session could not be created. " + exception.getMessage()
                    )
            );
        }
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
    @Transactional
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

        var savedCheckoutSession = checkoutSession.get();
        Session stripeSession = null;

        if (stripeCheckoutService.isConfigured()) {
            try {
                stripeSession = stripeCheckoutService.retrieveCheckoutSession(resolvedSessionId);

                if (!isStripeCheckoutComplete(stripeSession)) {
                    return ErrorResponseAssembler.toResponseEntity(
                            ApplicationError.validation("Stripe checkout session is not complete yet.")
                    );
                }
            } catch (StripeException exception) {
                return ErrorResponseAssembler.toResponseEntity(
                        ApplicationError.conflict(
                                "Stripe checkout session could not be verified. " + exception.getMessage()
                        )
                );
            }
        }

        savedCheckoutSession.setOrganization(organization.get());
        savedCheckoutSession.complete();
        checkoutSessionRepository.save(savedCheckoutSession);

        var savedSubscription = activateOrUpdateSubscription(
                organization.get(),
                savedCheckoutSession.getPlan(),
                stripeSession == null ? null : stripeSession.getCustomer(),
                stripeSession == null ? null : stripeSession.getSubscription()
        );

        var subscriptionResource = SubscriptionResourceFromEntityAssembler
                .toResourceFromEntity(savedSubscription);

        return ResponseEntity.ok(subscriptionResource);
    }

    @SecurityRequirements()
    @PostMapping("/webhook")
    @Transactional
    @Operation(summary = "Handle Stripe webhook events")
    public ResponseEntity<?> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader(name = "Stripe-Signature", required = false) String signatureHeader
    ) {
        if (!stripeCheckoutService.canVerifyWebhooks()) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation("Stripe webhook is not configured.")
            );
        }

        if (signatureHeader == null || signatureHeader.isBlank()) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation("Missing Stripe-Signature header.")
            );
        }

        try {
            var event = stripeCheckoutService.constructWebhookEvent(payload, signatureHeader);
            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

            if (stripeObject instanceof Session session) {
                if (event.getType().equals("checkout.session.completed")) {
                    completeLocalCheckoutSession(session);
                }

                if (event.getType().equals("checkout.session.expired")) {
                    expireLocalCheckoutSession(session);
                }
            }

            return ResponseEntity.ok(new MessageResource("Stripe webhook processed."));
        } catch (SignatureVerificationException exception) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.validation("Invalid Stripe webhook signature.")
            );
        }
    }

    private CheckoutSessionJpaEntity createStripeCheckoutSession(
            CreateCheckoutSessionResource resource,
            PlanJpaEntity plan,
            OrganizationJpaEntity organization
    ) throws StripeException {
        var stripeSession = stripeCheckoutService.createSubscriptionCheckoutSession(
                plan,
                organization,
                resource.successUrl(),
                resource.cancelUrl()
        );

        return new CheckoutSessionJpaEntity(
                stripeSession.getId(),
                stripeSession.getUrl(),
                organization,
                plan,
                resource.successUrl(),
                resource.cancelUrl()
        );
    }

    private CheckoutSessionJpaEntity createSimulatedCheckoutSession(
            CreateCheckoutSessionResource resource,
            PlanJpaEntity plan,
            OrganizationJpaEntity organization
    ) {
        var sessionId = "cs_test_" + UUID.randomUUID().toString().replace("-", "");
        var checkoutUrl = buildSimulatedCheckoutUrl(resource.successUrl(), sessionId);

        var checkoutSession = new CheckoutSessionJpaEntity(
                sessionId,
                checkoutUrl,
                organization,
                plan,
                resource.successUrl(),
                resource.cancelUrl()
        );

        checkoutSession.complete();
        return checkoutSession;
    }

    private void completeLocalCheckoutSession(Session stripeSession) {
        var localCheckoutSession = checkoutSessionRepository.findBySessionId(stripeSession.getId());

        if (localCheckoutSession.isEmpty()) {
            return;
        }

        var checkoutSession = localCheckoutSession.get();
        checkoutSession.complete();
        checkoutSessionRepository.save(checkoutSession);

        if (checkoutSession.getOrganization() == null) {
            return;
        }

        activateOrUpdateSubscription(
                checkoutSession.getOrganization(),
                checkoutSession.getPlan(),
                stripeSession.getCustomer(),
                stripeSession.getSubscription()
        );
    }

    private void expireLocalCheckoutSession(Session stripeSession) {
        checkoutSessionRepository.findBySessionId(stripeSession.getId())
                .ifPresent(checkoutSession -> {
                    checkoutSession.expire();
                    checkoutSessionRepository.save(checkoutSession);
                });
    }

    private SubscriptionJpaEntity activateOrUpdateSubscription(
            OrganizationJpaEntity organization,
            PlanJpaEntity plan,
            String stripeCustomerId,
            String stripeSubscriptionId
    ) {
        var activeSubscription = subscriptionRepository
                .findFirstByOrganization_IdAndStatusOrderByUpdatedAtDesc(
                        organization.getId(),
                        SubscriptionStatus.ACTIVE
                );

        if (activeSubscription.isPresent()) {
            var subscription = activeSubscription.get();
            subscription.setPlan(plan);
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setExpiresAt(LocalDate.now().plusMonths(1));
            subscription.setUpdatedAt(LocalDateTime.now());

            if (stripeCustomerId != null && !stripeCustomerId.isBlank()) {
                subscription.setStripeCustomerId(stripeCustomerId);
            }

            if (stripeSubscriptionId != null && !stripeSubscriptionId.isBlank()) {
                subscription.setStripeSubscriptionId(stripeSubscriptionId);
            }

            return subscriptionRepository.save(subscription);
        }

        var startedAt = LocalDate.now();
        var expiresAt = startedAt.plusMonths(1);

        var subscription = new SubscriptionJpaEntity(
                organization,
                plan,
                startedAt,
                expiresAt
        );

        subscription.setStripeCustomerId(stripeCustomerId);
        subscription.setStripeSubscriptionId(stripeSubscriptionId);

        return subscriptionRepository.save(subscription);
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

    private boolean isStripeCheckoutComplete(Session stripeSession) {
        return "complete".equalsIgnoreCase(stripeSession.getStatus());
    }

    private String buildSimulatedCheckoutUrl(String successUrl, String sessionId) {
        var separator = successUrl.contains("?") ? "&" : "?";
        return successUrl + separator + "session_id=" + sessionId;
    }
}
