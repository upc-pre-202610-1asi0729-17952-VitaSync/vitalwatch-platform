package com.vitalwatch.center.platform.subscriptions.interfaces.rest.controllers;

import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.CheckoutSessionJpaRepository;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.CheckoutSessionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.CheckoutSessionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for checkout session history.
 */
@RestController
@RequestMapping("/checkoutSessions")
@Tag(name = "Checkout Sessions", description = "Checkout session history endpoints")
public class CheckoutSessionsController {

    private final CheckoutSessionJpaRepository checkoutSessionRepository;

    public CheckoutSessionsController(CheckoutSessionJpaRepository checkoutSessionRepository) {
        this.checkoutSessionRepository = checkoutSessionRepository;
    }

    @GetMapping
    @Operation(summary = "Get checkout sessions by organization id")
    public ResponseEntity<List<CheckoutSessionResource>> getCheckoutSessions(
            @RequestParam Long organizationId
    ) {
        var sessions = checkoutSessionRepository
                .findByOrganization_IdOrderByCreatedAtDesc(organizationId)
                .stream()
                .map(CheckoutSessionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(sessions);
    }
}