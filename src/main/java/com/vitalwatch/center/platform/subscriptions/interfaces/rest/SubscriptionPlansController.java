package com.vitalwatch.center.platform.subscriptions.interfaces.rest;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.vitalwatch.center.platform.subscriptions.application.commandservices.SubscriptionCommandService;
import com.vitalwatch.center.platform.subscriptions.application.queryservices.SubscriptionQueryService;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetAllSubscriptionPlansQuery;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionPlanByIdQuery;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.CreateSubscriptionPlanResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.SubscriptionPlanResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.CreateSubscriptionPlanCommandFromResourceAssembler;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.SubscriptionPlanResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for VitalWatch subscription plans.
 */
@RestController
@RequestMapping(value = "/api/v1/subscription-plans", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscription Plans", description = "Subscription plan management endpoints")
public class SubscriptionPlansController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;

    public SubscriptionPlansController(
            SubscriptionCommandService subscriptionCommandService,
            SubscriptionQueryService subscriptionQueryService
    ) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @PostMapping
    @Operation(summary = "Create subscription plan", description = "Creates a new VitalWatch subscription plan.")
    public ResponseEntity<?> createSubscriptionPlan(@Valid @RequestBody CreateSubscriptionPlanResource resource) {
        var command = CreateSubscriptionPlanCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = subscriptionCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get all subscription plans", description = "Retrieves all VitalWatch subscription plans.")
    public ResponseEntity<List<SubscriptionPlanResource>> getAllSubscriptionPlans() {
        var plans = subscriptionQueryService.handle(new GetAllSubscriptionPlansQuery());

        var resources = plans.stream()
                .map(SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{subscriptionPlanId}")
    @Operation(summary = "Get subscription plan by id", description = "Retrieves a subscription plan by id.")
    public ResponseEntity<?> getSubscriptionPlanById(@PathVariable @Positive Long subscriptionPlanId) {
        var plan = subscriptionQueryService.handle(new GetSubscriptionPlanByIdQuery(subscriptionPlanId));

        if (plan.isEmpty()) {
            var error = ApplicationError.notFound("SubscriptionPlan", subscriptionPlanId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = SubscriptionPlanResourceFromEntityAssembler.toResourceFromEntity(plan.get());
        return ResponseEntity.ok(resource);
    }
}