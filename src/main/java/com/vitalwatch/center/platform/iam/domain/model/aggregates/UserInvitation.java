package com.vitalwatch.center.platform.iam.domain.model.aggregates;

import com.vitalwatch.center.platform.iam.domain.model.commands.InviteUserCommand;
import com.vitalwatch.center.platform.iam.domain.model.enums.InvitationStatus;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate root that represents an institutional invitation sent to a hospital user.
 */
public class UserInvitation extends AbstractDomainAggregateRoot<UserInvitation> {

    private Long id;
    private Long hospitalWorkspaceId;
    private EmailAddress emailAddress;
    private UserRole role;
    private InvitationStatus status;
    private String token;
    private Instant invitedAt;
    private Instant expiresAt;
    private Instant acceptedAt;

    public UserInvitation(
            Long id,
            Long hospitalWorkspaceId,
            EmailAddress emailAddress,
            UserRole role,
            InvitationStatus status,
            String token,
            Instant invitedAt,
            Instant expiresAt,
            Instant acceptedAt
    ) {
        this.id = id;
        this.hospitalWorkspaceId = Objects.requireNonNull(hospitalWorkspaceId, "hospitalWorkspaceId must not be null");
        this.emailAddress = Objects.requireNonNull(emailAddress, "emailAddress must not be null");
        this.role = Objects.requireNonNull(role, "role must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.token = Objects.requireNonNull(token, "token must not be null");
        this.invitedAt = Objects.requireNonNull(invitedAt, "invitedAt must not be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt must not be null");
        this.acceptedAt = acceptedAt;
    }

    public UserInvitation(InviteUserCommand command) {
        this(
                null,
                command.hospitalWorkspaceId(),
                new EmailAddress(command.email()),
                command.role(),
                InvitationStatus.PENDING,
                UUID.randomUUID().toString(),
                Instant.now(),
                Instant.now().plus(7, ChronoUnit.DAYS),
                null
        );
    }

    public void accept() {
        if (isExpired()) {
            this.status = InvitationStatus.EXPIRED;
            throw new IllegalStateException("Invitation has expired");
        }
        if (this.status != InvitationStatus.PENDING) {
            throw new IllegalStateException("Only pending invitations can be accepted");
        }

        this.status = InvitationStatus.ACCEPTED;
        this.acceptedAt = Instant.now();
    }

    public void cancel() {
        if (this.status == InvitationStatus.ACCEPTED) {
            throw new IllegalStateException("Accepted invitations cannot be cancelled");
        }
        this.status = InvitationStatus.CANCELLED;
    }

    public void markAsExpired() {
        this.status = InvitationStatus.EXPIRED;
    }

    public boolean isPending() {
        return this.status == InvitationStatus.PENDING;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHospitalWorkspaceId() {
        return hospitalWorkspaceId;
    }

    public EmailAddress getEmailAddressValue() {
        return emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress.value();
    }

    public UserRole getRole() {
        return role;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public Instant getInvitedAt() {
        return invitedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }
}