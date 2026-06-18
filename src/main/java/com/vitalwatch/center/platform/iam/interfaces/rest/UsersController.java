package com.vitalwatch.center.platform.iam.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendUserResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendUserResourceFromEntityAssembler;
import com.vitalwatch.center.platform.iam.application.queryservices.IamQueryService;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetUsersByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.repositories.UserAccountRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    public UsersController(
            IamQueryService iamQueryService,
            UserAccountRepository userAccountRepository
    ) {
        this.iamQueryService = iamQueryService;
        this.userAccountRepository = userAccountRepository;
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
}