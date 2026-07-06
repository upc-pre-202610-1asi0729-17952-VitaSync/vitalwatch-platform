package com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity for medical work areas.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "work_areas",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_work_areas_organization_name",
                        columnNames = {"organization_id", "name"}
                )
        }
)
public class WorkAreaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public WorkAreaJpaEntity(Long organizationId, String name) {
        this.organizationId = organizationId;
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}