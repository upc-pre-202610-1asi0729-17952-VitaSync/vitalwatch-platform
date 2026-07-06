package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.iam.domain.model.enums.OrganizationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity for hospital organizations.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "organizations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_organizations_ruc", columnNames = "ruc"),
                @UniqueConstraint(name = "uk_organizations_email", columnNames = "email")
        }
)
public class OrganizationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "legal_name", nullable = false, length = 150)
    private String legalName;

    @Column(name = "commercial_name", nullable = false, length = 150)
    private String commercialName;

    @Column(name = "ruc", nullable = false, length = 11)
    private String ruc;

    @Column(name = "email", nullable = false, length = 120)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "address", length = 200)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrganizationStatus status = OrganizationStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public OrganizationJpaEntity(
            String legalName,
            String commercialName,
            String ruc,
            String email,
            String phone,
            String address
    ) {
        this.legalName = legalName;
        this.commercialName = commercialName;
        this.ruc = ruc;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = OrganizationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = OrganizationStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = OrganizationStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
}