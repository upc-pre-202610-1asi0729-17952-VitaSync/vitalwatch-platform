package com.vitalwatch.center.platform.profiles.interfaces.rest;

import com.vitalwatch.center.platform.profiles.application.commandservices.ProfileCommandService;
import com.vitalwatch.center.platform.profiles.application.queryservices.ProfileQueryService;
import com.vitalwatch.center.platform.profiles.domain.model.queries.GetAllProfilesQuery;
import com.vitalwatch.center.platform.profiles.domain.model.queries.GetProfileByIdQuery;
import com.vitalwatch.center.platform.profiles.interfaces.rest.resources.CreateProfileResource;
import com.vitalwatch.center.platform.profiles.interfaces.rest.resources.ProfileResource;
import com.vitalwatch.center.platform.profiles.interfaces.rest.transform.CreateProfileCommandFromResourceAssembler;
import com.vitalwatch.center.platform.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * REST controller that exposes profile resources.
 */
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profile management endpoints")
public class ProfilesController {

    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfilesController(
            ProfileCommandService profileCommandService,
            ProfileQueryService profileQueryService
    ) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new profile",
            description = "Creates a new user profile with contact and address information."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Profile created successfully",
                    content = @Content(schema = @Schema(implementation = ProfileResource.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Profile already exists")
    })
    public ResponseEntity<?> createProfile(@Valid @RequestBody CreateProfileResource resource) {
        var command = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = profileCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ProfileResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{profileId}")
    @Operation(
            summary = "Get profile by ID",
            description = "Retrieves a specific profile by its identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile found",
                    content = @Content(schema = @Schema(implementation = ProfileResource.class))
            ),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<?> getProfileById(
            @PathVariable
            @Parameter(description = "Profile unique identifier", example = "1", required = true)
            Long profileId
    ) {
        var query = new GetProfileByIdQuery(profileId);
        var profile = profileQueryService.handle(query);

        if (profile.isEmpty()) {
            var error = ApplicationError.notFound("Profile", profileId.toString());
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
        }

        var resource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(
            summary = "Get all profiles",
            description = "Retrieves all profiles in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profiles found",
                    content = @Content(schema = @Schema(implementation = ProfileResource.class))
            )
    })
    public ResponseEntity<List<ProfileResource>> getAllProfiles() {
        var profiles = profileQueryService.handle(new GetAllProfilesQuery());

        if (profiles.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        var resources = profiles.stream()
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}