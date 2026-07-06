package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.PlanJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for plan persistence.
 */
public interface PlanJpaRepository extends JpaRepository<PlanJpaEntity, Long> {

    Optional<PlanJpaEntity> findByCode(String code);

    boolean existsByCode(String code);
}