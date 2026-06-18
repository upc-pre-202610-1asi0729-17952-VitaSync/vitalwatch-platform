package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendCareTeamResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.CreateFrontendTeamMemberResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendCareTeamResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendTeamMemberResource;
import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform.FrontendRoleMapper;
import com.vitalwatch.center.platform.iam.application.queryservices.IamQueryService;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetUsersByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;
import com.vitalwatch.center.platform.shifts.domain.repositories.ShiftAssignmentRepository;
import com.vitalwatch.center.platform.shifts.domain.repositories.WorkShiftRepository;
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

/**
 * Frontend compatibility controller for care teams and team members.
 * These resources are derived from existing users, work areas and shift assignments.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend API - Teams", description = "Care team and team member endpoints compatible with Angular")
public class TeamsCompatibilityController {

    private final WorkShiftRepository workShiftRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final IamQueryService iamQueryService;

    public TeamsCompatibilityController(
            WorkShiftRepository workShiftRepository,
            ShiftAssignmentRepository shiftAssignmentRepository,
            IamQueryService iamQueryService
    ) {
        this.workShiftRepository = workShiftRepository;
        this.shiftAssignmentRepository = shiftAssignmentRepository;
        this.iamQueryService = iamQueryService;
    }

    @GetMapping("/careTeams")
    @Operation(summary = "Get frontend-compatible care teams")
    public ResponseEntity<List<FrontendCareTeamResource>> getCareTeams(
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId
    ) {
        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;

        if (workspaceId == null) {
            return ResponseEntity.ok(List.of());
        }

        var shifts = workShiftRepository.findAllByHospitalWorkspaceId(workspaceId);

        if (shifts.isEmpty()) {
            var users = iamQueryService.handle(new GetUsersByHospitalWorkspaceIdQuery(workspaceId));
            var memberIds = users.stream()
                    .map(UserAccount::getId)
                    .sorted()
                    .toList();

            return ResponseEntity.ok(List.of(
                    new FrontendCareTeamResource(
                            teamIdFor(workspaceId, "General Care"),
                            workspaceId,
                            workspaceId,
                            "General Care Team",
                            "General Care",
                            "ACTIVE",
                            memberIds,
                            memberIds.size()
                    )
            ));
        }

        var teams = shifts.stream()
                .map(WorkShift::getWorkArea)
                .distinct()
                .sorted()
                .map(workArea -> toCareTeamFromWorkArea(workspaceId, workArea, shifts))
                .toList();

        return ResponseEntity.ok(teams);
    }

    @GetMapping("/careTeams/{careTeamId}")
    @Operation(summary = "Get frontend-compatible care team by id")
    public ResponseEntity<FrontendCareTeamResource> getCareTeamById(
            @PathVariable @Positive Long careTeamId
    ) {
        return ResponseEntity.ok(
                new FrontendCareTeamResource(
                        careTeamId,
                        null,
                        null,
                        "Care Team " + careTeamId,
                        "General Care",
                        "ACTIVE",
                        List.of(),
                        0
                )
        );
    }

    @PostMapping("/careTeams")
    @Operation(summary = "Create frontend-compatible care team")
    public ResponseEntity<FrontendCareTeamResource> createCareTeam(
            @Valid @RequestBody CreateFrontendCareTeamResource resource
    ) {
        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        if (workspaceId == null || workspaceId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var workArea = firstNonBlank(resource.workArea(), "General Care");
        var name = firstNonBlank(resource.name(), workArea + " Team");

        var supervisorId = resource.supervisorUserId() != null
                ? resource.supervisorUserId()
                : resource.supervisorId();

        var memberIds = supervisorId != null && supervisorId > 0
                ? List.of(supervisorId)
                : List.<Long>of();

        var response = new FrontendCareTeamResource(
                teamIdFor(workspaceId, workArea),
                workspaceId,
                workspaceId,
                name,
                workArea,
                "ACTIVE",
                memberIds,
                memberIds.size()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/careTeams/{careTeamId}")
    @Operation(summary = "Delete frontend-compatible care team")
    public ResponseEntity<Void> deleteCareTeam(
            @PathVariable @Positive Long careTeamId
    ) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/teamMembers")
    @Operation(summary = "Get frontend-compatible team members")
    public ResponseEntity<List<FrontendTeamMemberResource>> getTeamMembers(
            @RequestParam(required = false) @Positive Long teamId,
            @RequestParam(required = false) @Positive Long careTeamId,
            @RequestParam(required = false) @Positive Long organizationId,
            @RequestParam(required = false) @Positive Long hospitalWorkspaceId,
            @RequestParam(required = false) @Positive Long userAccountId,
            @RequestParam(required = false) @Positive Long userId
    ) {
        var selectedTeamId = teamId != null ? teamId : careTeamId;
        var workspaceId = organizationId != null ? organizationId : hospitalWorkspaceId;
        var selectedUserId = userAccountId != null ? userAccountId : userId;

        if (workspaceId == null) {
            return ResponseEntity.ok(List.of());
        }

        var users = iamQueryService.handle(new GetUsersByHospitalWorkspaceIdQuery(workspaceId));

        if (selectedUserId != null) {
            users = users.stream()
                    .filter(user -> user.getId().equals(selectedUserId))
                    .toList();
        }

        var resolvedTeamId = selectedTeamId != null
                ? selectedTeamId
                : teamIdFor(workspaceId, "General Care");

        var members = users.stream()
                .sorted(Comparator.comparing(UserAccount::getId))
                .map(user -> toTeamMemberFromUser(user, resolvedTeamId))
                .toList();

        return ResponseEntity.ok(members);
    }

    @PostMapping("/teamMembers")
    @Operation(summary = "Create frontend-compatible team member")
    public ResponseEntity<FrontendTeamMemberResource> createTeamMember(
            @Valid @RequestBody CreateFrontendTeamMemberResource resource
    ) {
        var selectedTeamId = resource.teamId() != null
                ? resource.teamId()
                : resource.careTeamId();

        var workspaceId = resource.organizationId() != null
                ? resource.organizationId()
                : resource.hospitalWorkspaceId();

        var selectedUserId = resource.userAccountId() != null
                ? resource.userAccountId()
                : resource.userId();

        if (selectedTeamId == null || selectedTeamId <= 0 ||
                selectedUserId == null || selectedUserId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        if (workspaceId != null && workspaceId > 0) {
            var users = iamQueryService.handle(new GetUsersByHospitalWorkspaceIdQuery(workspaceId));

            var foundUser = users.stream()
                    .filter(user -> user.getId().equals(selectedUserId))
                    .findFirst();

            if (foundUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(toTeamMemberFromUser(foundUser.get(), selectedTeamId));
            }
        }

        var response = new FrontendTeamMemberResource(
                selectedUserId,
                selectedTeamId,
                selectedTeamId,
                workspaceId,
                workspaceId,
                selectedUserId,
                selectedUserId,
                null,
                "user" + selectedUserId + "@vitalwatch.local",
                "User " + selectedUserId,
                firstNonBlank(resource.role(), "MEMBER"),
                "ACTIVE",
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/teamMembers/{teamMemberId}")
    @Operation(summary = "Delete frontend-compatible team member")
    public ResponseEntity<Void> deleteTeamMember(
            @PathVariable @Positive Long teamMemberId
    ) {
        return ResponseEntity.noContent().build();
    }

    private FrontendCareTeamResource toCareTeamFromWorkArea(
            Long workspaceId,
            String workArea,
            List<WorkShift> shifts
    ) {
        var shiftIds = shifts.stream()
                .filter(shift -> shift.getWorkArea().equalsIgnoreCase(workArea))
                .map(WorkShift::getId)
                .toList();

        var memberIds = shiftIds.stream()
                .flatMap(shiftId -> shiftAssignmentRepository.findAllByWorkShiftId(shiftId).stream())
                .filter(assignment -> assignment.isActive())
                .map(assignment -> assignment.getUserAccountId())
                .distinct()
                .sorted()
                .toList();

        return new FrontendCareTeamResource(
                teamIdFor(workspaceId, workArea),
                workspaceId,
                workspaceId,
                workArea + " Team",
                workArea,
                "ACTIVE",
                memberIds,
                memberIds.size()
        );
    }

    private FrontendTeamMemberResource toTeamMemberFromUser(UserAccount user, Long teamId) {
        return new FrontendTeamMemberResource(
                user.getId(),
                teamId,
                teamId,
                user.getHospitalWorkspaceId(),
                user.getHospitalWorkspaceId(),
                user.getId(),
                user.getId(),
                user.getProfileId(),
                user.getEmailAddress(),
                buildDisplayName(user.getEmailAddress()),
                FrontendRoleMapper.toFrontendRole(user.getRole()),
                user.getStatus().name(),
                Instant.now()
        );
    }

    private Long teamIdFor(Long workspaceId, String workArea) {
        var rawValue = workspaceId + ":" + firstNonBlank(workArea, "General Care");
        return Math.abs((long) rawValue.hashCode()) + 1L;
    }

    private String buildDisplayName(String email) {
        if (email == null || email.isBlank()) {
            return "User";
        }

        var atIndex = email.indexOf("@");
        return atIndex > 0 ? email.substring(0, atIndex) : email;
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