package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.BillingCheckoutSessionResponseResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.BillingCheckoutSessionStatusResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CancelBillingCheckoutSessionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CancelBillingCheckoutSessionResponseResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateBillingCheckoutSessionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendCheckoutSessionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendCheckoutSessionResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendRoleMapper;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.commands.CreateHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.iam.domain.model.commands.RegisterUserAccountCommand;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.Ruc;
import com.vitalwatch.center.platform.iam.domain.repositories.HospitalWorkspaceRepository;
import com.vitalwatch.center.platform.iam.domain.repositories.UserAccountRepository;
import com.vitalwatch.center.platform.profiles.domain.model.aggregates.Profile;
import com.vitalwatch.center.platform.profiles.domain.repositories.ProfileRepository;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.HospitalSubscription;
import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.SubscribeHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;
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

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Frontend compatibility controller for checkout and billing endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend API - Checkout", description = "Checkout and billing endpoints compatible with Angular")
public class CheckoutCompatibilityController {

    private static final AtomicLong CHECKOUT_ID_SEQUENCE = new AtomicLong(1);
    private static final ConcurrentHashMap<Long, FrontendCheckoutSessionResource> CHECKOUT_SESSIONS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> STRIPE_SESSION_TO_CHECKOUT_ID = new ConcurrentHashMap<>();

    private final HospitalWorkspaceRepository hospitalWorkspaceRepository;
    private final UserAccountRepository userAccountRepository;
    private final ProfileRepository profileRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final HospitalSubscriptionRepository hospitalSubscriptionRepository;

    public CheckoutCompatibilityController(
            HospitalWorkspaceRepository hospitalWorkspaceRepository,
            UserAccountRepository userAccountRepository,
            ProfileRepository profileRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            HospitalSubscriptionRepository hospitalSubscriptionRepository
    ) {
        this.hospitalWorkspaceRepository = hospitalWorkspaceRepository;
        this.userAccountRepository = userAccountRepository;
        this.profileRepository = profileRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.hospitalSubscriptionRepository = hospitalSubscriptionRepository;
    }

    @GetMapping("/checkoutSessions")
    @Operation(summary = "Get frontend-compatible checkout sessions")
    public ResponseEntity<List<FrontendCheckoutSessionResource>> getCheckoutSessions(
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId
    ) {
        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;

        if (workspaceId == null) {
            return ResponseEntity.ok(List.of());
        }

        var sessions = CHECKOUT_SESSIONS.values()
                .stream()
                .filter(session -> session.organizationId().equals(workspaceId))
                .sorted(Comparator.comparing(FrontendCheckoutSessionResource::createdAt).reversed())
                .toList();

        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/checkoutSessions")
    @Operation(summary = "Create frontend-compatible checkout session")
    public ResponseEntity<FrontendCheckoutSessionResource> createCheckoutSession(
            @Valid @RequestBody CreateFrontendCheckoutSessionResource resource
    ) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        var administratorId = resource.administratorId() != null
                ? resource.administratorId()
                : resource.administratorUserAccountId();

        var planId = resource.planId() != null
                ? resource.planId()
                : resource.subscriptionPlanId();

        if (workspaceId == null || workspaceId <= 0 ||
                administratorId == null || administratorId <= 0 ||
                resource.subscriptionId() == null || resource.subscriptionId() <= 0 ||
                planId == null || planId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var session = new FrontendCheckoutSessionResource(
                CHECKOUT_ID_SEQUENCE.getAndIncrement(),
                workspaceId,
                workspaceId,
                administratorId,
                administratorId,
                resource.subscriptionId(),
                planId,
                planId,
                firstNonBlank(resource.planCode(), "professional"),
                firstNonBlank(resource.status(), "COMPLETED"),
                Instant.now()
        );

        CHECKOUT_SESSIONS.put(session.id(), session);

        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @PostMapping("/billing/create-checkout-session")
    @Operation(summary = "Create billing checkout session compatible with frontend")
    public ResponseEntity<?> createBillingCheckoutSession(
            @Valid @RequestBody CreateBillingCheckoutSessionResource resource
    ) {
        if (resource.organization() == null ||
                resource.administrator() == null ||
                resource.planCode() == null ||
                resource.planCode().isBlank()) {
            return ResponseEntity.badRequest().body("Invalid checkout request");
        }

        try {
            var organization = resource.organization();
            var administrator = resource.administrator();

            var emailAddress = new EmailAddress(administrator.email());
            var ruc = new Ruc(organization.ruc());

            if (userAccountRepository.existsByEmailAddress(emailAddress)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with this email already exists.");
            }

            if (hospitalWorkspaceRepository.existsByRuc(ruc)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A hospital workspace with this RUC already exists.");
            }

            var plan = resolvePlanByCode(resource.planCode());

            if (plan.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var profile = new Profile(
                    firstNonBlank(administrator.firstName(), "Hospital"),
                    firstNonBlank(administrator.lastName(), "Administrator"),
                    administrator.email(),
                    firstNonBlank(organization.address(), "Default Street"),
                    "1",
                    "Lima",
                    "15001",
                    "Peru"
            );

            var savedProfile = profileRepository.save(profile);

            var workspace = new HospitalWorkspace(
                    new CreateHospitalWorkspaceCommand(
                            organization.name(),
                            organization.ruc(),
                            savedProfile.getId(),
                            administrator.email()
                    )
            );

            var savedWorkspace = hospitalWorkspaceRepository.save(workspace);

            var administratorAccount = new UserAccount(
                    new RegisterUserAccountCommand(
                            savedWorkspace.getId(),
                            savedProfile.getId(),
                            administrator.email(),
                            FrontendRoleMapper.toBackendRole("HOSPITAL_ADMIN")
                    )
            );

            var savedAdministrator = userAccountRepository.save(administratorAccount);

            var subscription = new HospitalSubscription(
                    new SubscribeHospitalWorkspaceCommand(
                            savedWorkspace.getId(),
                            plan.get().getId()
                    )
            );

            var savedSubscription = hospitalSubscriptionRepository.save(subscription);

            var checkoutSessionId = CHECKOUT_ID_SEQUENCE.getAndIncrement();
            var stripeSessionId = "cs_test_" + UUID.randomUUID().toString().replace("-", "");
            var planCode = plan.get().getCode().name().toLowerCase();

            var checkoutSession = new FrontendCheckoutSessionResource(
                    checkoutSessionId,
                    savedWorkspace.getId(),
                    savedWorkspace.getId(),
                    savedAdministrator.getId(),
                    savedAdministrator.getId(),
                    savedSubscription.getId(),
                    plan.get().getId(),
                    plan.get().getId(),
                    planCode,
                    "PENDING",
                    Instant.now()
            );

            CHECKOUT_SESSIONS.put(checkoutSession.id(), checkoutSession);
            STRIPE_SESSION_TO_CHECKOUT_ID.put(stripeSessionId, checkoutSession.id());

            var checkoutUrl = frontendBaseUrl()
                    + "/checkout/success?session_id="
                    + stripeSessionId
                    + "&checkoutSessionId="
                    + checkoutSession.id()
                    + "&plan="
                    + planCode;

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new BillingCheckoutSessionResponseResource(
                            checkoutUrl,
                            stripeSessionId,
                            savedWorkspace.getId(),
                            savedAdministrator.getId(),
                            savedSubscription.getId(),
                            checkoutSession.id()
                    )
            );

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/billing/checkout-session-status")
    @Operation(summary = "Get billing checkout session status compatible with frontend")
    public ResponseEntity<BillingCheckoutSessionStatusResource> getCheckoutSessionStatus(
            @RequestParam(name = "session_id") String sessionId
    ) {
        var checkoutSessionId = STRIPE_SESSION_TO_CHECKOUT_ID.get(sessionId);

        if (checkoutSessionId != null) {
            var session = CHECKOUT_SESSIONS.get(checkoutSessionId);

            if (session != null) {
                var completedSession = new FrontendCheckoutSessionResource(
                        session.id(),
                        session.organizationId(),
                        session.hospitalWorkspaceId(),
                        session.administratorId(),
                        session.administratorUserAccountId(),
                        session.subscriptionId(),
                        session.planId(),
                        session.subscriptionPlanId(),
                        session.planCode(),
                        "COMPLETED",
                        session.createdAt()
                );

                CHECKOUT_SESSIONS.put(completedSession.id(), completedSession);
            }
        }

        return ResponseEntity.ok(
                new BillingCheckoutSessionStatusResource(
                        sessionId,
                        "COMPLETED",
                        "paid",
                        true
                )
        );
    }

    @PostMapping("/billing/cancel-checkout-session")
    @Operation(summary = "Cancel billing checkout session compatible with frontend")
    public ResponseEntity<CancelBillingCheckoutSessionResponseResource> cancelCheckoutSession(
            @Valid @RequestBody CancelBillingCheckoutSessionResource resource
    ) {
        if (resource.checkoutSessionId() == null || resource.checkoutSessionId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var existingSession = CHECKOUT_SESSIONS.get(resource.checkoutSessionId());

        if (existingSession != null) {
            var cancelledSession = new FrontendCheckoutSessionResource(
                    existingSession.id(),
                    existingSession.organizationId(),
                    existingSession.hospitalWorkspaceId(),
                    existingSession.administratorId(),
                    existingSession.administratorUserAccountId(),
                    existingSession.subscriptionId(),
                    existingSession.planId(),
                    existingSession.subscriptionPlanId(),
                    existingSession.planCode(),
                    "FAILED",
                    existingSession.createdAt()
            );

            CHECKOUT_SESSIONS.put(cancelledSession.id(), cancelledSession);
        }

        return ResponseEntity.ok(
                new CancelBillingCheckoutSessionResponseResource(
                        true,
                        resource.checkoutSessionId()
                )
        );
    }

    private Optional<SubscriptionPlan> resolvePlanByCode(String value) {
        try {
            var planCode = SubscriptionPlanCode.valueOf(value.trim().toUpperCase().replace("-", "_"));
            return subscriptionPlanRepository.findByCode(planCode);
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    private String frontendBaseUrl() {
        var value = System.getenv("FRONTEND_BASE_URL");

        if (value == null || value.isBlank()) {
            return "http://localhost:4200";
        }

        return value.endsWith("/")
                ? value.substring(0, value.length() - 1)
                : value;
    }

    private String firstNonBlank(String... values) {
        for (var value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return "";
    }
}