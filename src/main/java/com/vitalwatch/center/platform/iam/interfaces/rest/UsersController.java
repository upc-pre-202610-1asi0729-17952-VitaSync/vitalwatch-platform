package com.vitalwatch.center.platform.iam.interfaces.rest;

import com.vitalwatch.center.platform.iam.application.queryservices.IamQueryService;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetUsersByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.UserAccountResource;
import com.vitalwatch.center.platform.iam.interfaces.rest.transform.UserAccountResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
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

    public UsersController(IamQueryService iamQueryService) {
        this.iamQueryService = iamQueryService;
    }

    @GetMapping
    @Operation(summary = "Get users by hospital workspace", description = "Retrieves institutional users for a hospital workspace.")
    public ResponseEntity<List<UserAccountResource>> getUsersByHospitalWorkspaceId(
            @RequestParam @Positive Long hospitalWorkspaceId
    ) {
        var users = iamQueryService.handle(new GetUsersByHospitalWorkspaceIdQuery(hospitalWorkspaceId));

        var resources = users.stream()
                .map(UserAccountResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}