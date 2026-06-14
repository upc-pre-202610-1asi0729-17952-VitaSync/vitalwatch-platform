package com.vitalwatch.center.platform.staffrecovery.application.queryservices;

import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryAction;
import com.vitalwatch.center.platform.staffrecovery.domain.model.aggregates.RecoveryPlan;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryActionsByRecoveryPlanIdQuery;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryPlanByIdQuery;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryPlansByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.staffrecovery.domain.model.queries.GetRecoveryPlansByUserAccountIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for staff recovery queries.
 */
public interface RecoveryQueryService {

    Optional<RecoveryPlan> handle(GetRecoveryPlanByIdQuery query);

    List<RecoveryPlan> handle(GetRecoveryPlansByHospitalWorkspaceIdQuery query);

    List<RecoveryPlan> handle(GetRecoveryPlansByUserAccountIdQuery query);

    List<RecoveryAction> handle(GetRecoveryActionsByRecoveryPlanIdQuery query);
}