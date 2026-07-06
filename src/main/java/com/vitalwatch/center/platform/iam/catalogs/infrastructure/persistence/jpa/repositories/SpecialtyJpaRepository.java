package com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.entities.SpecialtyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for specialty persistence.
 */
public interface SpecialtyJpaRepository extends JpaRepository<SpecialtyJpaEntity, Long> {

    List<SpecialtyJpaEntity> findAllByOrderByNameAsc();

    boolean existsByNameIgnoreCase(String name);
}