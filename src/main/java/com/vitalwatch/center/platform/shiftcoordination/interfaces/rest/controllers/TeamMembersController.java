package com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.controllers;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities.TeamMemberJpaEntity;
import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.repositories.CareTeamJpaRepository;
import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.repositories.TeamMemberJpaRepository;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.CreateTeamMemberResource;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.resources.TeamMemberResource;
import com.vitalwatch.center.platform.shiftcoordination.interfaces.rest.transform.TeamMemberResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for team members.
 */
@RestController
@RequestMapping("/teamMembers")
@Tag(name = "Team Members", description = "Clinical team member endpoints")
public class TeamMembersController {

    private final TeamMemberJpaRepository teamMemberRepository;
    private final CareTeamJpaRepository careTeamRepository;
    private final UserJpaRepository userRepository;
    private final MessageResolver messageResolver;

    public TeamMembersController(
            TeamMemberJpaRepository teamMemberRepository,
            CareTeamJpaRepository careTeamRepository,
            UserJpaRepository userRepository,
            MessageResolver messageResolver
    ) {
        this.teamMemberRepository = teamMemberRepository;
        this.careTeamRepository = careTeamRepository;
        this.userRepository = userRepository;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @Operation(summary = "Get all team members or filter them")
    public ResponseEntity<List<TeamMemberResource>> getTeamMembers(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) Long userId
    ) {
        var members = teamId != null
                ? teamMemberRepository.findByTeamId(teamId)
                : userId != null
                  ? teamMemberRepository.findByUserId(userId)
                  : teamMemberRepository.findAll();

        var resources = members.stream()
                .map(TeamMemberResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{teamMemberId}")
    @Operation(summary = "Get team member by id")
    public ResponseEntity<?> getTeamMemberById(@PathVariable Long teamMemberId) {
        var member = teamMemberRepository.findById(teamMemberId);

        if (member.isEmpty()) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("shift.teamMember.notFound")
                    )
            );
        }

        return ResponseEntity.ok(
                TeamMemberResourceFromEntityAssembler.toResourceFromEntity(member.get())
        );
    }

    @PostMapping
    @Operation(summary = "Add user to team")
    public ResponseEntity<?> createTeamMember(
            @Valid @RequestBody CreateTeamMemberResource resource
    ) {
        if (!careTeamRepository.existsById(resource.teamId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("shift.careTeam.notFound")
                    )
            );
        }

        if (!userRepository.existsById(resource.userId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("iam.user.notFound")
                    )
            );
        }

        if (teamMemberRepository.existsByTeamIdAndUserId(resource.teamId(), resource.userId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict(
                            messageResolver.get("shift.teamMember.duplicate")
                    )
            );
        }

        if (teamMemberRepository.existsByUserId(resource.userId())) {
            return ErrorResponseAssembler.toResponseEntity(
                    ApplicationError.conflict(
                            messageResolver.get("shift.teamMember.alreadyAssigned")
                    )
            );
        }

        var member = new TeamMemberJpaEntity(
                resource.teamId(),
                resource.userId()
        );

        var savedMember = teamMemberRepository.save(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                TeamMemberResourceFromEntityAssembler.toResourceFromEntity(savedMember)
        );
    }

    @DeleteMapping("/{teamMemberId}")
    @Operation(summary = "Delete team member")
    public ResponseEntity<?> deleteTeamMember(@PathVariable Long teamMemberId) {
        if (!teamMemberRepository.existsById(teamMemberId)) {
            return ErrorResponseAssembler.toResponseEntity(
                    new ApplicationError(
                            "RESOURCE_NOT_FOUND",
                            messageResolver.get("shift.teamMember.notFound")
                    )
            );
        }

        teamMemberRepository.deleteById(teamMemberId);

        return ResponseEntity.noContent().build();
    }
}