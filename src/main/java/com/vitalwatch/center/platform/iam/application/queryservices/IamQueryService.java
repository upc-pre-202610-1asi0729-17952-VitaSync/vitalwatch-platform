package com.vitalwatch.center.platform.iam.application.queryservices;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetHospitalWorkspaceByIdQuery;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetInvitationsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetUsersByHospitalWorkspaceIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for IAM queries.
 */
public interface IamQueryService {

    Optional<HospitalWorkspace> handle(GetHospitalWorkspaceByIdQuery query);

    List<UserAccount> handle(GetUsersByHospitalWorkspaceIdQuery query);

    List<UserInvitation> handle(GetInvitationsByHospitalWorkspaceIdQuery query);
}