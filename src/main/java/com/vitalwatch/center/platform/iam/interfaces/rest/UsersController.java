package com.vitalwatch.center.platform.iam.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendUserResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendUserResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.PatchFrontendUserResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendRoleMapper;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendUserResourceFromEntityAssembler;
import com.vitalwatch.center.platform.iam.application.queryservices.IamQueryService;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.commands.RegisterUserAccountCommand;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetUsersByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.repositories.UserAccountRepository;
import com.vitalwatch.center.platform.profiles.domain.model.aggregates.Profile;
import com.vitalwatch.center.platform.profiles.domain.repositories.ProfileRepository;
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
 * REST controller for institutional users.
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Institutional user account query endpoints")
public class UsersController {

    private final IamQueryService iamQueryService;
    private final UserAccountRepository userAccountRepository;
    private final ProfileRepository profileRepository;

    public UsersController(
            IamQueryService iamQueryService,
            UserAccountRepository userAccountRepository,
            ProfileRepository profileRepository
    ) {
        this.iamQueryService = iamQueryService;
        this.userAccountRepository = userAccountRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping
    @Operation(summary = "Get users", description = "Retrieves users by hospital workspace, organization id, or email.")
    public ResponseEntity<List<FrontendUserResource>> getUsers(
            @RequestParam(required = false) Long hospitalWorkspaceId,
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password
    ) {
        if (email != null && !email.isBlank()) {
            try {
                var user = userAccountRepository.findByEmailAddress(new EmailAddress(email));

                var resources = user.stream()
                        .map(FrontendUserResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.ok(resources);

            } catch (IllegalArgumentException exception) {
                return ResponseEntity.badRequest().build();
            }
        }

        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;

        if (workspaceId == null) {
            return ResponseEntity.ok(List.of());
        }

        if (workspaceId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var users = iamQueryService.handle(new GetUsersByHospitalWorkspaceIdQuery(workspaceId));

        var resources = users.stream()
                .map(FrontendUserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<FrontendUserResource> getUserById(
            @PathVariable @Positive Long userId
    ) {
        return userAccountRepository.findById(userId)
                .map(FrontendUserResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user compatible with frontend")
    public ResponseEntity<FrontendUserResource> createUser(
            @Valid @RequestBody CreateFrontendUserResource resource
    ) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        if (workspaceId == null || workspaceId <= 0 ||
                resource.email() == null || resource.email().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            var emailAddress = new EmailAddress(resource.email());

            if (userAccountRepository.existsByEmailAddress(emailAddress)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            var profile = new Profile(
                    firstNonBlank(resource.firstName(), "VitalWatch"),
                    firstNonBlank(resource.lastName(), "User"),
                    resource.email(),
                    "Default Street",
                    "1",
                    "Lima",
                    "15001",
                    "Peru"
            );

            var savedProfile = profileRepository.save(profile);

            var user = new UserAccount(
                    new RegisterUserAccountCommand(
                            workspaceId,
                            savedProfile.getId(),
                            resource.email(),
                            FrontendRoleMapper.toBackendRole(firstNonBlank(resource.role(), "DOCTOR"))
                    )
            );

            if ("INACTIVE".equalsIgnoreCase(resource.status())) {
                user.deactivate();
            }

            var savedUser = userAccountRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(FrontendUserResourceFromEntityAssembler.toResourceFromEntity(savedUser));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Patch user compatible with frontend")
    public ResponseEntity<FrontendUserResource> patchUser(
            @PathVariable @Positive Long userId,
            @Valid @RequestBody PatchFrontendUserResource resource
    ) {
        var user = userAccountRepository.findById(userId);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            var userToUpdate = user.get();

            if (resource.role() != null && !resource.role().isBlank()) {
                userToUpdate.assignRole(FrontendRoleMapper.toBackendRole(resource.role()));
            }

            if (resource.status() != null && !resource.status().isBlank()) {
                if ("ACTIVE".equalsIgnoreCase(resource.status())) {
                    userToUpdate.activate();
                } else if ("SUSPENDED".equalsIgnoreCase(resource.status())) {
                    userToUpdate.suspend();
                } else {
                    userToUpdate.deactivate();
                }
            }

            var savedUser = userAccountRepository.save(userToUpdate);

            return ResponseEntity.ok(FrontendUserResourceFromEntityAssembler.toResourceFromEntity(savedUser));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
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