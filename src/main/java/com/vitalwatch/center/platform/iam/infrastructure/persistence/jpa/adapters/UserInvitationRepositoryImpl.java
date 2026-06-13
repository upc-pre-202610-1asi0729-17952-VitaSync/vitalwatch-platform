package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;
import com.vitalwatch.center.platform.iam.domain.model.enums.InvitationStatus;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.repositories.UserInvitationRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.assemblers.UserInvitationPersistenceAssembler;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserInvitationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for UserInvitation.
 */
@Repository
public class UserInvitationRepositoryImpl implements UserInvitationRepository {

    private final UserInvitationPersistenceRepository repository;

    public UserInvitationRepositoryImpl(UserInvitationPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserInvitation> findById(Long id) {
        return repository.findById(id)
                .map(UserInvitationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<UserInvitation> findByToken(String token) {
        return repository.findByToken(token)
                .map(UserInvitationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<UserInvitation> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findAllByHospitalWorkspaceId(hospitalWorkspaceId)
                .stream()
                .map(UserInvitationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public UserInvitation save(UserInvitation userInvitation) {
        var savedEntity = repository.save(
                UserInvitationPersistenceAssembler.toPersistenceFromDomain(userInvitation)
        );
        return UserInvitationPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }

    @Override
    public boolean existsByHospitalWorkspaceIdAndEmailAddressAndPendingStatus(
            Long hospitalWorkspaceId,
            EmailAddress emailAddress
    ) {
        return repository.countByHospitalWorkspaceIdAndEmailAddressAndStatus(
                hospitalWorkspaceId,
                emailAddress.value(),
                InvitationStatus.PENDING
        ) > 0;
    }
}