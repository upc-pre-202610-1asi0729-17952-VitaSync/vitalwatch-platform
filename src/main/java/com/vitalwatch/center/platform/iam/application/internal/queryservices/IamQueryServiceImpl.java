package com.vitalwatch.center.platform.iam.application.internal.queryservices;

import com.vitalwatch.center.platform.iam.application.queryservices.IamQueryService;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetHospitalWorkspaceByIdQuery;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetInvitationsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.iam.domain.model.queries.GetUsersByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.iam.domain.repositories.HospitalWorkspaceRepository;
import com.vitalwatch.center.platform.iam.domain.repositories.UserAccountRepository;
import com.vitalwatch.center.platform.iam.domain.repositories.UserInvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * IAM query service implementation.
 */
@Service
public class IamQueryServiceImpl implements IamQueryService {

    private final HospitalWorkspaceRepository hospitalWorkspaceRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserInvitationRepository userInvitationRepository;

    public IamQueryServiceImpl(
            HospitalWorkspaceRepository hospitalWorkspaceRepository,
            UserAccountRepository userAccountRepository,
            UserInvitationRepository userInvitationRepository
    ) {
        this.hospitalWorkspaceRepository = hospitalWorkspaceRepository;
        this.userAccountRepository = userAccountRepository;
        this.userInvitationRepository = userInvitationRepository;
    }

    @Override
    public Optional<HospitalWorkspace> handle(GetHospitalWorkspaceByIdQuery query) {
        return hospitalWorkspaceRepository.findById(query.hospitalWorkspaceId());
    }

    @Override
    public List<UserAccount> handle(GetUsersByHospitalWorkspaceIdQuery query) {
        return userAccountRepository.findAllByHospitalWorkspaceId(query.hospitalWorkspaceId());
    }

    @Override
    public List<UserInvitation> handle(GetInvitationsByHospitalWorkspaceIdQuery query) {
        return userInvitationRepository.findAllByHospitalWorkspaceId(query.hospitalWorkspaceId());
    }
}