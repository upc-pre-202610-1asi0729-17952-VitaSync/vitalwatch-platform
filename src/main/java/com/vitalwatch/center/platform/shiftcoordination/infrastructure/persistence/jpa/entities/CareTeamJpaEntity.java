package com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.shiftcoordination.domain.model.enums.CareTeamStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity for clinical care teams.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "care_teams",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_care_teams_organization_name",
                        columnNames = {"organization_id", "name"}
                )
        }
)
public class CareTeamJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "work_area_id", nullable = false)
    private Long workAreaId;

    @Column(name = "supervisor_id")
    private Long supervisorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private CareTeamStatus status = CareTeamStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public CareTeamJpaEntity(
            Long organizationId,
            String name,
            Long workAreaId,
            Long supervisorId
    ) {
        this.organizationId = organizationId;
        this.name = name;
        this.workAreaId = workAreaId;
        this.supervisorId = supervisorId;
        this.status = CareTeamStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSupervisor(Long supervisorId) {
        this.supervisorId = supervisorId;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(CareTeamStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}