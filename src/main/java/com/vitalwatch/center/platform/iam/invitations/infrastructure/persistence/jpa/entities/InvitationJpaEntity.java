package com.vitalwatch.center.platform.iam.invitations.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import com.vitalwatch.center.platform.iam.invitations.domain.model.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity for organization invitations.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "invitations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_invitations_token", columnNames = "token")
        }
)
public class InvitationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, length = 120)
    private String token;

    @Column(name = "email", nullable = false, length = 120)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private InvitationStatus status = InvitationStatus.SENT;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private OrganizationJpaEntity organization;

    @Column(name = "specialty_id")
    private Long specialtyId;

    @Column(name = "work_area_id")
    private Long workAreaId;

    @Column(name = "accepted_user_id")
    private Long acceptedUserId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public InvitationJpaEntity(
            String token,
            String email,
            UserRole role,
            OrganizationJpaEntity organization,
            Long specialtyId,
            Long workAreaId,
            LocalDateTime expiresAt
    ) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.organization = organization;
        this.specialtyId = specialtyId;
        this.workAreaId = workAreaId;
        this.expiresAt = expiresAt;
        this.status = InvitationStatus.SENT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public void accept(Long acceptedUserId) {
        this.status = InvitationStatus.ACCEPTED;
        this.acceptedUserId = acceptedUserId;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = InvitationStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void expire() {
        this.status = InvitationStatus.EXPIRED;
        this.updatedAt = LocalDateTime.now();
    }
}