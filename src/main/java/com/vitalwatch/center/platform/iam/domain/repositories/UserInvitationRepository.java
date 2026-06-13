package com.vitalwatch.center.platform.iam.domain.repositories;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for user invitations.
 */
public interface UserInvitationRepository {

    Optional<UserInvitation> findById(Long id);

    Optional<UserInvitation> findByToken(String token);

    List<UserInvitation> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    UserInvitation save(UserInvitation userInvitation);

    boolean existsByHospitalWorkspaceIdAndEmailAddressAndPendingStatus(Long hospitalWorkspaceId, EmailAddress emailAddress);
}