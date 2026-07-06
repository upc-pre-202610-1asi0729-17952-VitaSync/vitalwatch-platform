package com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.entities.WorkAreaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for work area persistence.
 */
public interface WorkAreaJpaRepository extends JpaRepository<WorkAreaJpaEntity, Long> {

    List<WorkAreaJpaEntity> findAllByOrderByNameAsc();

    List<WorkAreaJpaEntity> findByOrganizationIdOrderByNameAsc(Long organizationId);

    boolean existsByOrganizationIdAndNameIgnoreCase(Long organizationId, String name);
}