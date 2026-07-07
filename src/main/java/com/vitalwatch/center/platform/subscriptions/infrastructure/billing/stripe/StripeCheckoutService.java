package com.vitalwatch.center.platform.subscriptions.infrastructure.billing.stripe;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.PlanJpaEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Stripe integration service used to create Checkout sessions and validate webhook payloads.
 */
@Service
public class StripeCheckoutService {

    private final boolean enabled;
    private final String secretKey;
    private final String webhookSecret;

    public StripeCheckoutService(
            @Value("${application.billing.stripe.enabled:false}") boolean enabled,
            @Value("${application.billing.stripe.secret-key:}") String secretKey,
            @Value("${application.billing.stripe.webhook-secret:}") String webhookSecret
    ) {
        this.enabled = enabled;
        this.secretKey = secretKey;
        this.webhookSecret = webhookSecret;
    }

    public boolean isConfigured() {
        return enabled && hasText(secretKey);
    }

    public boolean canVerifyWebhooks() {
        return isConfigured() && hasText(webhookSecret);
    }

    public Session createSubscriptionCheckoutSession(
            PlanJpaEntity plan,
            OrganizationJpaEntity organization,
            String successUrl,
            String cancelUrl
    ) throws StripeException {
        var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(normalizeSuccessUrl(successUrl))
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(plan.getCurrency().toLowerCase())
                                                .setUnitAmount(toStripeAmount(plan.getPrice()))
                                                .setRecurring(
                                                        SessionCreateParams.LineItem.PriceData.Recurring.builder()
                                                                .setInterval(resolveInterval(plan.getBillingPeriod()))
                                                                .build()
                                                )
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(plan.getName())
                                                                .setDescription(plan.getDescription())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .putMetadata("planId", plan.getId().toString())
                .putMetadata("planCode", plan.getCode());

        if (organization != null) {
            builder
                    .setClientReferenceId(organization.getId().toString())
                    .setCustomerEmail(organization.getEmail())
                    .putMetadata("organizationId", organization.getId().toString());
        }

        return Session.create(builder.build(), requestOptions());
    }

    public Session retrieveCheckoutSession(String sessionId) throws StripeException {
        return Session.retrieve(sessionId, requestOptions());
    }

    public Event constructWebhookEvent(String payload, String signatureHeader)
            throws SignatureVerificationException {
        return Webhook.constructEvent(payload, signatureHeader, webhookSecret);
    }

    private RequestOptions requestOptions() {
        return RequestOptions.builder()
                .setApiKey(secretKey)
                .build();
    }

    private String normalizeSuccessUrl(String successUrl) {
        if (successUrl.contains("{CHECKOUT_SESSION_ID}")) {
            return successUrl;
        }

        var separator = successUrl.contains("?") ? "&" : "?";
        return successUrl + separator + "session_id={CHECKOUT_SESSION_ID}";
    }

    private long toStripeAmount(BigDecimal price) {
        return price
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
    }

    private SessionCreateParams.LineItem.PriceData.Recurring.Interval resolveInterval(String billingPeriod) {
        if (billingPeriod != null && billingPeriod.equalsIgnoreCase("YEARLY")) {
            return SessionCreateParams.LineItem.PriceData.Recurring.Interval.YEAR;
        }

        return SessionCreateParams.LineItem.PriceData.Recurring.Interval.MONTH;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
