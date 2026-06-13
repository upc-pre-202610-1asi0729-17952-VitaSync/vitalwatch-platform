package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.iam.domain.model.enums.InvitationStatus;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserInvitationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for institutional user invitations.
 */
public interface UserInvitationPersistenceRepository extends JpaRepository<UserInvitationPersistenceEntity, Long> {

    Optional<UserInvitationPersistenceEntity> findByToken(String token);

    List<UserInvitationPersistenceEntity> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    long countByHospitalWorkspaceIdAndEmailAddressAndStatus(
            Long hospitalWorkspaceId,
            String emailAddress,
            InvitationStatus status
    );
}