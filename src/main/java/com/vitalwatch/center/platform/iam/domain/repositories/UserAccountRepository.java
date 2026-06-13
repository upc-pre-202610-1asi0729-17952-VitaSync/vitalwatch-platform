package com.vitalwatch.center.platform.iam.domain.repositories;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for user accounts.
 */
public interface UserAccountRepository {

    Optional<UserAccount> findById(Long id);

    Optional<UserAccount> findByEmailAddress(EmailAddress emailAddress);

    List<UserAccount> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    UserAccount save(UserAccount userAccount);

    boolean existsByEmailAddress(EmailAddress emailAddress);
}