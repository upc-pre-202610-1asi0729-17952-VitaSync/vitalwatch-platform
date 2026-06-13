package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.HospitalWorkspacePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for hospital workspaces.
 */
public interface HospitalWorkspacePersistenceRepository extends JpaRepository<HospitalWorkspacePersistenceEntity, Long> {

    Optional<HospitalWorkspacePersistenceEntity> findByRuc(String ruc);

    long countByRuc(String ruc);
}