package com.vitalwatch.center.platform.iam.interfaces.rest;

import com.vitalwatch.center.platform.iam.application.commandservices.IamCommandService;
import com.vitalwatch.center.platform.iam.application.queryservices.IamQueryService;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetInvitationsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.InviteUserResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.UserInvitationResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.InviteUserCommandFromResourceAssembler;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.UserInvitationResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
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
 * REST controller for institutional user invitations.
 */
@RestController
@RequestMapping(value = "/api/v1/user-invitations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Invitations", description = "Institutional invitation management endpoints")
public class UserInvitationsController {

    private final IamCommandService iamCommandService;
    private final IamQueryService iamQueryService;

    public UserInvitationsController(
            IamCommandService iamCommandService,
            IamQueryService iamQueryService
    ) {
        this.iamCommandService = iamCommandService;
        this.iamQueryService = iamQueryService;
    }

    @PostMapping
    @Operation(summary = "Invite hospital user", description = "Registers an institutional invitation for a hospital user.")
    public ResponseEntity<?> inviteUser(@Valid @RequestBody InviteUserResource resource) {
        var command = InviteUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = iamCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                UserInvitationResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get invitations by hospital workspace", description = "Retrieves invitations for a hospital workspace.")
    public ResponseEntity<List<UserInvitationResource>> getInvitationsByHospitalWorkspaceId(
            @RequestParam @Positive Long hospitalWorkspaceId
    ) {
        var invitations = iamQueryService.handle(new GetInvitationsByHospitalWorkspaceIdQuery(hospitalWorkspaceId));

        var resources = invitations.stream()
                .map(UserInvitationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}