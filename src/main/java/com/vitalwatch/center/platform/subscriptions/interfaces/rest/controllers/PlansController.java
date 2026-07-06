package com.vitalwatch.center.platform.subscriptions.interfaces.rest.controllers;

import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.PlanJpaRepository;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.PlanResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.PlanResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for subscription plan read operations.
 */
@RestController
@RequestMapping("/plans")
@Tag(name = "Plans", description = "Subscription plan endpoints")
public class PlansController {

    private final PlanJpaRepository planRepository;
    private final MessageResolver messageResolver;

    public PlansController(
            PlanJpaRepository planRepository,
            MessageResolver messageResolver
    ) {
        this.planRepository = planRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all subscription plans")
    public ResponseEntity<List<PlanResource>> getAllPlans() {
        var plans = planRepository.findAll()
                .stream()
                .map(PlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{planId}")
    @Operation(summary = "Get subscription plan by id")
    public ResponseEntity<?> getPlanById(@PathVariable Long planId) {
        var plan = planRepository.findById(planId);

        if (plan.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("subscriptions.plan.notFound")
                    )
            );
        }

        var resource = PlanResourceFromEntityAssembler.toResourceFromEntity(plan.get());

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/by-code/{code}")
    @Operation(summary = "Get subscription plan by code")
    public ResponseEntity<?> getPlanByCode(@PathVariable String code) {
        var plan = planRepository.findByCode(code);

        if (plan.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("subscriptions.plan.notFound")
                    )
            );
        }

        var resource = PlanResourceFromEntityAssembler.toResourceFromEntity(plan.get());

        return ResponseEntity.ok(resource);
    }
}