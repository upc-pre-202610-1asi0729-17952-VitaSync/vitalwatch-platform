package com.vitalwatch.center.platform.iam.domain.model.aggregates;

import com.vitalwatch.center.platform.iam.domain.model.commands.RegisterUserAccountCommand;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserAccountStatus;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.util.Objects;

/**
 * Aggregate root that represents an institutional user account in VitalWatch.
 */
public class UserAccount extends AbstractDomainAggregateRoot<UserAccount> {

    private Long id;
    private Long hospitalWorkspaceId;
    private Long profileId;
    private EmailAddress emailAddress;
    private UserRole role;
    private UserAccountStatus status;

    public UserAccount(
            Long id,
            Long hospitalWorkspaceId,
            Long profileId,
            EmailAddress emailAddress,
            UserRole role,
            UserAccountStatus status
    ) {
        this.id = id;
        this.hospitalWorkspaceId = Objects.requireNonNull(hospitalWorkspaceId, "hospitalWorkspaceId must not be null");
        this.profileId = Objects.requireNonNull(profileId, "profileId must not be null");
        this.emailAddress = Objects.requireNonNull(emailAddress, "emailAddress must not be null");
        this.role = Objects.requireNonNull(role, "role must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public UserAccount(RegisterUserAccountCommand command) {
        this(
                null,
                command.hospitalWorkspaceId(),
                command.profileId(),
                new EmailAddress(command.email()),
                command.role(),
                UserAccountStatus.ACTIVE
        );
    }

    public void assignRole(UserRole role) {
        this.role = Objects.requireNonNull(role, "role must not be null");
    }

    public void suspend() {
        this.status = UserAccountStatus.SUSPENDED;
    }

    public void deactivate() {
        this.status = UserAccountStatus.DEACTIVATED;
    }

    public void activate() {
        this.status = UserAccountStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == UserAccountStatus.ACTIVE;
    }

    public boolean belongsToWorkspace(Long hospitalWorkspaceId) {
        return this.hospitalWorkspaceId.equals(hospitalWorkspaceId);
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

    public Long getProfileId() {
        return profileId;
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

    public UserAccountStatus getStatus() {
        return status;
    }
}