package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendSubscriptionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendSubscriptionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendSubscriptionResourceFromEntityAssembler;
import com.vitalwatch.center.platform.iam.domain.repositories.HospitalWorkspaceRepository;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.SubscribeHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.subscriptions.domain.repositories.HospitalSubscriptionRepository;
import com.vitalwatch.center.platform.subscriptions.domain.repositories.SubscriptionPlanRepository;
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
 * Frontend compatibility controller for subscription endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend Compatibility - Subscriptions", description = "Subscription endpoints compatible with the Angular frontend")
public class SubscriptionsCompatibilityController {

    private final HospitalSubscriptionRepository hospitalSubscriptionRepository;
    private final HospitalWorkspaceRepository hospitalWorkspaceRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionsCompatibilityController(
            HospitalSubscriptionRepository hospitalSubscriptionRepository,
            HospitalWorkspaceRepository hospitalWorkspaceRepository,
            SubscriptionPlanRepository subscriptionPlanRepository
    ) {
        this.hospitalSubscriptionRepository = hospitalSubscriptionRepository;
        this.hospitalWorkspaceRepository = hospitalWorkspaceRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @GetMapping
    @Operation(summary = "Get frontend-compatible subscriptions")
    public ResponseEntity<List<FrontendSubscriptionResource>> getSubscriptions() {
        var subscriptions = hospitalSubscriptionRepository.findAll()
                .stream()
                .map(FrontendSubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping(params = "organizationId")
    @Operation(summary = "Get frontend-compatible subscription by organization id")
    public ResponseEntity<FrontendSubscriptionResource> getSubscriptionByOrganizationId(
            @RequestParam @Positive Long organizationId
    ) {
        return hospitalSubscriptionRepository.findByHospitalWorkspaceId(organizationId)
                .map(FrontendSubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{subscriptionId}")
    @Operation(summary = "Get frontend-compatible subscription by id")
    public ResponseEntity<FrontendSubscriptionResource> getSubscriptionById(
            @PathVariable @Positive Long subscriptionId
    ) {
        return hospitalSubscriptionRepository.findById(subscriptionId)
                .map(FrontendSubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create frontend-compatible subscription")
    public ResponseEntity<FrontendSubscriptionResource> createSubscription(
            @Valid @RequestBody CreateFrontendSubscriptionResource resource
    ) {
        var organization = hospitalWorkspaceRepository.findById(resource.organizationId());

        if (organization.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var plan = subscriptionPlanRepository.findById(resource.planId());

        if (plan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var existingSubscription = hospitalSubscriptionRepository.findByHospitalWorkspaceId(resource.organizationId());

        if (existingSubscription.isPresent() && existingSubscription.get().isActive()) {
            var response = FrontendSubscriptionResourceFromEntityAssembler.toResourceFromEntity(existingSubscription.get());
            return ResponseEntity.ok(response);
        }

        var subscription = new HospitalSubscription(
                new SubscribeHospitalWorkspaceCommand(
                        resource.organizationId(),
                        resource.planId()
                )
        );

        var savedSubscription = hospitalSubscriptionRepository.save(subscription);
        var response = FrontendSubscriptionResourceFromEntityAssembler.toResourceFromEntity(savedSubscription);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}