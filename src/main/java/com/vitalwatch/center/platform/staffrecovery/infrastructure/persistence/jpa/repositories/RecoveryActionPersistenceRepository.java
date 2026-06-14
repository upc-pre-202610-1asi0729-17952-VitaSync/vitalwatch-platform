package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities.RecoveryActionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for recovery actions.
 */
public interface RecoveryActionPersistenceRepository extends JpaRepository<RecoveryActionPersistenceEntity, Long> {

    List<RecoveryActionPersistenceEntity> findAllByRecoveryPlanIdOrderByActionCreatedAtDesc(Long recoveryPlanId);
}