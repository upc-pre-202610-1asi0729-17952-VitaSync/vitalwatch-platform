package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendPlanResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendPlanResourceFromEntityAssembler;
import com.vitalwatch.center.platform.subscriptions.application.queryservices.SubscriptionQueryService;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetAllSubscriptionPlansQuery;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionPlanByIdQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Frontend compatibility controller for plan endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1/plans", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend Compatibility - Plans", description = "Plan endpoints compatible with the Angular frontend")
public class PlansCompatibilityController {

    private final SubscriptionQueryService subscriptionQueryService;

    public PlansCompatibilityController(SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @GetMapping
    @Operation(summary = "Get frontend-compatible plans")
    public ResponseEntity<List<FrontendPlanResource>> getPlans() {
        var plans = subscriptionQueryService.handle(new GetAllSubscriptionPlansQuery());

        var resources = plans.stream()
                .map(FrontendPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{planId}")
    @Operation(summary = "Get frontend-compatible plan by id")
    public ResponseEntity<FrontendPlanResource> getPlanById(@PathVariable @Positive Long planId) {
        var plan = subscriptionQueryService.handle(new GetSubscriptionPlanByIdQuery(planId));

        return plan
                .map(FrontendPlanResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}