package com.vitalwatch.center.platform.staffrecovery.application.internal.queryservices;

import com.vitalwatch.center.platform.staffrecovery.application.queryservices.RecoveryQueryService;
import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryAction;
import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryPlan;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryActionsByRecoveryPlanIdQuery;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryPlanByIdQuery;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryPlansByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryPlansByUserAccountIdQuery;
import com.vitalwatch.center.platform.staffrecovery.domain.repositories.RecoveryActionRepository;
import com.vitalwatch.center.platform.staffrecovery.domain.repositories.RecoveryPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Staff recovery query service implementation.
 */
@Service
public class RecoveryQueryServiceImpl implements RecoveryQueryService {

    private final RecoveryPlanRepository recoveryPlanRepository;
    private final RecoveryActionRepository recoveryActionRepository;

    public RecoveryQueryServiceImpl(
            RecoveryPlanRepository recoveryPlanRepository,
            RecoveryActionRepository recoveryActionRepository
    ) {
        this.recoveryPlanRepository = recoveryPlanRepository;
        this.recoveryActionRepository = recoveryActionRepository;
    }

    @Override
    public Optional<RecoveryPlan> handle(GetRecoveryPlanByIdQuery query) {
        return recoveryPlanRepository.findById(query.recoveryPlanId());
    }

    @Override
    public List<RecoveryPlan> handle(GetRecoveryPlansByHospitalWorkspaceIdQuery query) {
        return recoveryPlanRepository.findAllByHospitalWorkspaceId(query.hospitalWorkspaceId());
    }

    @Override
    public List<RecoveryPlan> handle(GetRecoveryPlansByUserAccountIdQuery query) {
        return recoveryPlanRepository.findAllByUserAccountId(query.userAccountId());
    }

    @Override
    public List<RecoveryAction> handle(GetRecoveryActionsByRecoveryPlanIdQuery query) {
        return recoveryActionRepository.findAllByRecoveryPlanId(query.recoveryPlanId());
    }
}