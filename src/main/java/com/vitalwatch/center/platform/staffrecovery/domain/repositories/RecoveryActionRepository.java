package com.vitalwatch.center.platform.staffrecovery.domain.repositories;

import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryAction;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for recovery actions.
 */
public interface RecoveryActionRepository {

    Optional<RecoveryAction> findById(Long id);

    List<RecoveryAction> findAllByRecoveryPlanId(Long recoveryPlanId);

    RecoveryAction save(RecoveryAction recoveryAction);
}