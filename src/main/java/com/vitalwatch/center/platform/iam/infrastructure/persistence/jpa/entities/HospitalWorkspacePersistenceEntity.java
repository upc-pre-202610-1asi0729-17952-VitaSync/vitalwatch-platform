package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.iam.domain.model.enums.HospitalWorkspaceStatus;
import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;

/**
 * JPA persistence entity for hospital workspaces.
 */
@Entity
@Table(name = "hospital_workspaces")
public class HospitalWorkspacePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 11)
    private String ruc;

    @Column(name = "administrator_profile_id", nullable = false)
    private Long administratorProfileId;

    @Column(name = "administrator_email", nullable = false)
    private String administratorEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HospitalWorkspaceStatus status;

    public HospitalWorkspacePersistenceEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public Long getAdministratorProfileId() {
        return administratorProfileId;
    }

    public void setAdministratorProfileId(Long administratorProfileId) {
        this.administratorProfileId = administratorProfileId;
    }

    public String getAdministratorEmail() {
        return administratorEmail;
    }

    public void setAdministratorEmail(String administratorEmail) {
        this.administratorEmail = administratorEmail;
    }

    public HospitalWorkspaceStatus getStatus() {
        return status;
    }

    public void setStatus(HospitalWorkspaceStatus status) {
        this.status = status;
    }
}