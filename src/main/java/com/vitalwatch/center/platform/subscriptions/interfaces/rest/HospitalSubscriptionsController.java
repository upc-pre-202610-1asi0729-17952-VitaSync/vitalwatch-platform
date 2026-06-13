package com.vitalwatch.center.platform.subscriptions.interfaces.rest;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.vitalwatch.center.platform.subscriptions.application.commandservices.SubscriptionCommandService;
import com.vitalwatch.center.platform.subscriptions.application.queryservices.SubscriptionQueryService;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.CancelSubscriptionCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.subscriptions.domain.model.queries.GetSubscriptionByIdQuery;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.ChangeSubscriptionPlanResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.HospitalSubscriptionResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.resources.SubscribeHospitalWorkspaceResource;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.ChangeSubscriptionPlanCommandFromResourceAssembler;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.HospitalSubscriptionResourceFromEntityAssembler;
import com.vitalwatch.center.platform.subscriptions.interfaces.rest.transform.SubscribeHospitalWorkspaceCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for hospital subscriptions.
 */
@RestController
@RequestMapping(value = "/api/v1/hospital-subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Hospital Subscriptions", description = "Hospital subscription management endpoints")
public class HospitalSubscriptionsController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;

    public HospitalSubscriptionsController(
            SubscriptionCommandService subscriptionCommandService,
            SubscriptionQueryService subscriptionQueryService
    ) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @PostMapping
    @Operation(summary = "Subscribe hospital workspace", description = "Creates a hospital subscription for a workspace.")
    public ResponseEntity<?> subscribeHospitalWorkspace(@Valid @RequestBody SubscribeHospitalWorkspaceResource resource) {
        var command = SubscribeHospitalWorkspaceCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = subscriptionCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                HospitalSubscriptionResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{subscriptionId}")
    @Operation(summary = "Get hospital subscription by id", description = "Retrieves a hospital subscription by id.")
    public ResponseEntity<?> getSubscriptionById(@PathVariable @Positive Long subscriptionId) {
        var subscription = subscriptionQueryService.handle(new GetSubscriptionByIdQuery(subscriptionId));

        if (subscription.isEmpty()) {
            var error = ApplicationError.notFound("HospitalSubscription", subscriptionId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = HospitalSubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping(params = "hospitalWorkspaceId")
    @Operation(summary = "Get subscription by hospital workspace", description = "Retrieves the latest subscription for a hospital workspace.")
    public ResponseEntity<?> getSubscriptionByHospitalWorkspaceId(@RequestParam @Positive Long hospitalWorkspaceId) {
        var subscription = subscriptionQueryService.handle(
                new GetSubscriptionByHospitalWorkspaceIdQuery(hospitalWorkspaceId)
        );

        if (subscription.isEmpty()) {
            var error = ApplicationError.notFound("HospitalSubscription", hospitalWorkspaceId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = HospitalSubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return ResponseEntity.ok(resource);
    }

    @PatchMapping("/{subscriptionId}/plan")
    @Operation(summary = "Change subscription plan", description = "Changes the plan of an active hospital subscription.")
    public ResponseEntity<?> changeSubscriptionPlan(
            @PathVariable @Positive Long subscriptionId,
            @Valid @RequestBody ChangeSubscriptionPlanResource resource
    ) {
        var command = ChangeSubscriptionPlanCommandFromResourceAssembler.toCommandFromResource(subscriptionId, resource);
        var result = subscriptionCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                HospitalSubscriptionResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{subscriptionId}/cancel")
    @Operation(summary = "Cancel subscription", description = "Cancels a hospital subscription.")
    public ResponseEntity<?> cancelSubscription(@PathVariable @Positive Long subscriptionId) {
        var result = subscriptionCommandService.handle(new CancelSubscriptionCommand(subscriptionId));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                HospitalSubscriptionResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}