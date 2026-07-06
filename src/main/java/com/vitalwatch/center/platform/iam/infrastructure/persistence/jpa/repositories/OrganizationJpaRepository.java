package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for organization persistence.
 */
public interface OrganizationJpaRepository extends JpaRepository<OrganizationJpaEntity, Long> {

    Optional<OrganizationJpaEntity> findByEmail(String email);

    Optional<OrganizationJpaEntity> findByRuc(String ruc);

    boolean existsByEmail(String email);

    boolean existsByRuc(String ruc);
}