package com.vitalwatch.center.platform.staffrecovery.domain.repositories;

import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryPlan;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for recovery plans.
 */
public interface RecoveryPlanRepository {

    Optional<RecoveryPlan> findById(Long id);

    List<RecoveryPlan> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    List<RecoveryPlan> findAllByUserAccountId(Long userAccountId);

    RecoveryPlan save(RecoveryPlan recoveryPlan);
}