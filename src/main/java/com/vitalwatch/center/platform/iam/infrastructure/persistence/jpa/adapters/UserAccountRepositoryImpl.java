package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.repositories.UserAccountRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.assemblers.UserAccountPersistenceAssembler;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for UserAccount.
 */
@Repository
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountPersistenceRepository repository;

    public UserAccountRepositoryImpl(UserAccountPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserAccount> findById(Long id) {
        return repository.findById(id)
                .map(UserAccountPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<UserAccount> findByEmailAddress(EmailAddress emailAddress) {
        return repository.findByEmailAddress(emailAddress.value())
                .map(UserAccountPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<UserAccount> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findAllByHospitalWorkspaceId(hospitalWorkspaceId)
                .stream()
                .map(UserAccountPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        var savedEntity = repository.save(
                UserAccountPersistenceAssembler.toPersistenceFromDomain(userAccount)
        );
        return UserAccountPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }

    @Override
    public boolean existsByEmailAddress(EmailAddress emailAddress) {
        return repository.countByEmailAddress(emailAddress.value()) > 0;
    }
}