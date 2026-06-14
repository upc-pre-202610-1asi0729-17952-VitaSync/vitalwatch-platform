package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities.RecoveryPlanPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for recovery plans.
 */
public interface RecoveryPlanPersistenceRepository extends JpaRepository<RecoveryPlanPersistenceEntity, Long> {

    List<RecoveryPlanPersistenceEntity> findAllByHospitalWorkspaceIdOrderByPlanCreatedAtDesc(Long hospitalWorkspaceId);

    List<RecoveryPlanPersistenceEntity> findAllByUserAccountIdOrderByPlanCreatedAtDesc(Long userAccountId);
}