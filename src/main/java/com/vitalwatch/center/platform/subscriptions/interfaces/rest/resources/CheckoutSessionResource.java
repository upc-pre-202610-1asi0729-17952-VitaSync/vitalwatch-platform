package com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Resource used to expose checkout session data through the REST API.
 */
public record CheckoutSessionResource(
        Long id,
        String sessionId,
        String stripeSessionId,
        String checkoutUrl,
        String status,
        Long organizationId,
        Long planId,
        String planCode,
        String planName,
        BigDecimal planPrice,
        String currency,
        String billingPeriod,
        LocalDateTime createdAt
) {
}