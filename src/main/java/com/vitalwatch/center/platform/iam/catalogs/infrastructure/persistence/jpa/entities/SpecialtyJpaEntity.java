package com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity for medical specialties.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "specialties",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_specialties_name", columnNames = "name")
        }
)
public class SpecialtyJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public SpecialtyJpaEntity(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}