package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserAccountPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for institutional user accounts.
 */
public interface UserAccountPersistenceRepository extends JpaRepository<UserAccountPersistenceEntity, Long> {

    Optional<UserAccountPersistenceEntity> findByEmailAddress(String emailAddress);

    List<UserAccountPersistenceEntity> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    long countByEmailAddress(String emailAddress);
}