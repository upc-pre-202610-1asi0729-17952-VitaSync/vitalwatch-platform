package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities.CareTeamJpaEntity;
import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.repositories.CareTeamJpaRepository;
import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.repositories.TeamMemberJpaRepository;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.CareTeamResource;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.CreateCareTeamResource;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.UpdateCareTeamResource;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.transform.CareTeamResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for clinical care teams.
 */
@RestController
@RequestMapping("/careTeams")
@Tag(name = "Care Teams", description = "Clinical care team endpoints")
public class CareTeamsController {

    private final CareTeamJpaRepository careTeamRepository;
    private final TeamMemberJpaRepository teamMemberRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final UserJpaRepository userRepository;
    private final MessageResolver messageResolver;

    public CareTeamsController(
            CareTeamJpaRepository careTeamRepository,
            TeamMemberJpaRepository teamMemberRepository,
            OrganizationJpaRepository organizationRepository,
            UserJpaRepository userRepository,
            MessageResolver messageResolver
    ) {
        this.careTeamRepository = careTeamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all care teams or filter them")
    public ResponseEntity<List<CareTeamResource>> getCareTeams(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long supervisorId,
            @RequestParam(required = false) Long workAreaId
    ) {
        var teams = organizationId != null
                ? careTeamRepository.findByOrganizationIdOrderByNameAsc(organizationId)
                : supervisorId != null
                  ? careTeamRepository.findBySupervisorId(supervisorId)
                  : workAreaId != null
                    ? careTeamRepository.findByWorkAreaId(workAreaId)
                    : careTeamRepository.findAllByOrderByNameAsc();

        var resources = teams.stream()
                .map(CareTeamResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{careTeamId}")
    @Operation(summary = "Get care team by id")
    public ResponseEntity<?> getCareTeamById(@PathVariable Long careTeamId) {
        var team = careTeamRepository.findById(careTeamId);

        if (team.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("shift.careTeam.notFound")
                    )
            );
        }

        return ResponseEntity.ok(
                CareTeamResourceFromEntityAssembler.toResourceFromEntity(team.get())
        );
    }

    @PostMapping
    @Operation(summary = "Create care team")
    public ResponseEntity<?> createCareTeam(
            @Valid @RequestBody CreateCareTeamResource resource
    ) {
        if (!organizationRepository.existsById(resource.organizationId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.organization.notFound")
                    )
            );
        }

        if (resource.supervisorId() != null && !userRepository.existsById(resource.supervisorId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.user.notFound")
                    )
            );
        }

        if (careTeamRepository.existsByOrganizationIdAndNameIgnoreCase(
                resource.organizationId(),
                resource.name()
        )) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict(
                            messageResolver.get("shift.careTeam.nameConflict")
                    )
            );
        }

        var team = new CareTeamJpaEntity(
                resource.organizationId(),
                resource.name(),
                resource.workAreaId(),
                resource.supervisorId()
        );

        var savedTeam = careTeamRepository.save(team);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                CareTeamResourceFromEntityAssembler.toResourceFromEntity(savedTeam)
        );
    }

    @PatchMapping("/{careTeamId}")
    @Operation(summary = "Update care team partially")
    public ResponseEntity<?> updateCareTeam(
            @PathVariable Long careTeamId,
            @RequestBody UpdateCareTeamResource resource
    ) {
        var team = careTeamRepository.findById(careTeamId);

        if (team.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("shift.careTeam.notFound")
                    )
            );
        }

        var foundTeam = team.get();

        if (resource.name() != null && !resource.name().isBlank()) {
            foundTeam.setName(resource.name().trim());
        }

        if (resource.workAreaId() != null) {
            foundTeam.setWorkAreaId(resource.workAreaId());
        }

        if (resource.supervisorId() != null && !userRepository.existsById(resource.supervisorId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.user.notFound")
                    )
            );
        }

        foundTeam.setSupervisorId(resource.supervisorId());

        if (resource.status() != null) {
            foundTeam.updateStatus(resource.status());
        }

        var savedTeam = careTeamRepository.save(foundTeam);

        return ResponseEntity.ok(
                CareTeamResourceFromEntityAssembler.toResourceFromEntity(savedTeam)
        );
    }

    @DeleteMapping("/{careTeamId}")
    @Operation(summary = "Delete care team")
    public ResponseEntity<?> deleteCareTeam(@PathVariable Long careTeamId) {
        if (!careTeamRepository.existsById(careTeamId)) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("shift.careTeam.notFound")
                    )
            );
        }

        var members = teamMemberRepository.findByTeamId(careTeamId);
        teamMemberRepository.deleteAll(members);

        careTeamRepository.deleteById(careTeamId);

        return ResponseEntity.noContent().build();
    }
}