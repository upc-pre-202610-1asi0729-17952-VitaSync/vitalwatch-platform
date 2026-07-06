package com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity for team memberships.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "team_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_team_members_team_user",
                        columnNames = {"team_id", "user_id"}
                )
        }
)
public class TeamMemberJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public TeamMemberJpaEntity(Long teamId, Long userId) {
        this.teamId = teamId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}