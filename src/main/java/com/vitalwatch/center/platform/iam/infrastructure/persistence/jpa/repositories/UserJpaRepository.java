package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for user persistence.
 */
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findByUsername(String username);

    List<UserJpaEntity> findByOrganizationId(Long organizationId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}