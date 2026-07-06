package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for user persistence.
 */
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    @Override
    @EntityGraph(attributePaths = "organization")
    List<UserJpaEntity> findAll();

    @Override
    @EntityGraph(attributePaths = "organization")
    Optional<UserJpaEntity> findById(Long id);

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findByUsername(String username);

    @EntityGraph(attributePaths = "organization")
    List<UserJpaEntity> findByOrganization_Id(Long organizationId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}