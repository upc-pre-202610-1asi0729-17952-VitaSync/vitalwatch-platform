package com.vitalwatch.center.platform.iam.domain.model.aggregates;

import com.vitalwatch.center.platform.iam.domain.model.commands.CreateHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.iam.domain.model.enums.HospitalWorkspaceStatus;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.HospitalWorkspaceName;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.Ruc;
import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.util.Objects;

/**
 * Aggregate root that represents a hospital institutional workspace in VitalWatch.
 */
public class HospitalWorkspace extends AbstractDomainAggregateRoot<HospitalWorkspace> {

    private Long id;
    private HospitalWorkspaceName name;
    private Ruc ruc;
    private Long administratorProfileId;
    private EmailAddress administratorEmail;
    private HospitalWorkspaceStatus status;

    public HospitalWorkspace(
            Long id,
            HospitalWorkspaceName name,
            Ruc ruc,
            Long administratorProfileId,
            EmailAddress administratorEmail,
            HospitalWorkspaceStatus status
    ) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.ruc = Objects.requireNonNull(ruc, "ruc must not be null");
        this.administratorProfileId = Objects.requireNonNull(administratorProfileId, "administratorProfileId must not be null");
        this.administratorEmail = Objects.requireNonNull(administratorEmail, "administratorEmail must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public HospitalWorkspace(CreateHospitalWorkspaceCommand command) {
        this(
                null,
                new HospitalWorkspaceName(command.name()),
                new Ruc(command.ruc()),
                command.administratorProfileId(),
                new EmailAddress(command.administratorEmail()),
                HospitalWorkspaceStatus.ACTIVE
        );
    }

    public void activate() {
        this.status = HospitalWorkspaceStatus.ACTIVE;
    }

    public void suspend() {
        this.status = HospitalWorkspaceStatus.SUSPENDED;
    }

    public void deactivate() {
        this.status = HospitalWorkspaceStatus.DEACTIVATED;
    }

    public boolean isActive() {
        return this.status == HospitalWorkspaceStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HospitalWorkspaceName getNameValue() {
        return name;
    }

    public String getName() {
        return name.value();
    }

    public Ruc getRucValue() {
        return ruc;
    }

    public String getRuc() {
        return ruc.value();
    }

    public Long getAdministratorProfileId() {
        return administratorProfileId;
    }

    public EmailAddress getAdministratorEmailValue() {
        return administratorEmail;
    }

    public String getAdministratorEmail() {
        return administratorEmail.value();
    }

    public HospitalWorkspaceStatus getStatus() {
        return status;
    }
}