package com.vitalwatch.center.platform.iam.application.internal.commandservices;

import com.vitalwatch.center.platform.iam.application.commandservices.IamCommandService;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;
import com.vitalwatch.center.platform.iam.domain.model.commands.AssignUserRoleCommand;
import com.vitalwatch.center.platform.iam.domain.model.commands.CreateHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.iam.domain.model.commands.InviteUserCommand;
import com.vitalwatch.center.platform.iam.domain.model.commands.RegisterUserAccountCommand;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.Ruc;
import com.vitalwatch.center.platform.iam.domain.repositories.HospitalWorkspaceRepository;
import com.vitalwatch.center.platform.iam.domain.repositories.UserAccountRepository;
import com.vitalwatch.center.platform.iam.domain.repositories.UserInvitationRepository;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * IAM command service implementation.
 */
@Service
public class IamCommandServiceImpl implements IamCommandService {

    private final HospitalWorkspaceRepository hospitalWorkspaceRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserInvitationRepository userInvitationRepository;

    public IamCommandServiceImpl(
            HospitalWorkspaceRepository hospitalWorkspaceRepository,
            UserAccountRepository userAccountRepository,
            UserInvitationRepository userInvitationRepository
    ) {
        this.hospitalWorkspaceRepository = hospitalWorkspaceRepository;
        this.userAccountRepository = userAccountRepository;
        this.userInvitationRepository = userInvitationRepository;
    }

    @Override
    @Transactional
    public Result<HospitalWorkspace, ApplicationError> handle(CreateHospitalWorkspaceCommand command) {
        try {
            var ruc = new Ruc(command.ruc());
            var administratorEmail = new EmailAddress(command.administratorEmail());

            if (command.administratorProfileId() == null || command.administratorProfileId() <= 0) {
                return Result.failure(ApplicationError.validationError(
                        "administratorProfileId",
                        "Administrator profile id must be a positive number"
                ));
            }

            if (hospitalWorkspaceRepository.existsByRuc(ruc)) {
                return Result.failure(ApplicationError.conflict(
                        "HospitalWorkspace",
                        "A hospital workspace with RUC '%s' already exists".formatted(command.ruc())
                ));
            }

            if (userAccountRepository.existsByEmailAddress(administratorEmail)) {
                return Result.failure(ApplicationError.conflict(
                        "UserAccount",
                        "A user account with email '%s' already exists".formatted(command.administratorEmail())
                ));
            }

            var workspace = new HospitalWorkspace(command);
            var savedWorkspace = hospitalWorkspaceRepository.save(workspace);

            var administratorAccount = new UserAccount(new RegisterUserAccountCommand(
                    savedWorkspace.getId(),
                    command.administratorProfileId(),
                    command.administratorEmail(),
                    UserRole.HOSPITAL_ADMIN
            ));

            userAccountRepository.save(administratorAccount);

            return Result.success(savedWorkspace);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("HospitalWorkspace", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Hospital workspace creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<UserAccount, ApplicationError> handle(RegisterUserAccountCommand command) {
        try {
            var workspace = hospitalWorkspaceRepository.findById(command.hospitalWorkspaceId());

            if (workspace.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "HospitalWorkspace",
                        command.hospitalWorkspaceId().toString()
                ));
            }

            if (!workspace.get().isActive()) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "Inactive hospital workspace",
                        "Users cannot be registered in an inactive hospital workspace"
                ));
            }

            if (command.profileId() == null || command.profileId() <= 0) {
                return Result.failure(ApplicationError.validationError(
                        "profileId",
                        "Profile id must be a positive number"
                ));
            }

            var emailAddress = new EmailAddress(command.email());

            if (userAccountRepository.existsByEmailAddress(emailAddress)) {
                return Result.failure(ApplicationError.conflict(
                        "UserAccount",
                        "A user account with email '%s' already exists".formatted(command.email())
                ));
            }

            var userAccount = new UserAccount(command);
            var savedUserAccount = userAccountRepository.save(userAccount);

            return Result.success(savedUserAccount);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("UserAccount", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("User account registration", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<UserInvitation, ApplicationError> handle(InviteUserCommand command) {
        try {
            var workspace = hospitalWorkspaceRepository.findById(command.hospitalWorkspaceId());

            if (workspace.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "HospitalWorkspace",
                        command.hospitalWorkspaceId().toString()
                ));
            }

            if (!workspace.get().isActive()) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "Inactive hospital workspace",
                        "Users cannot be invited to an inactive hospital workspace"
                ));
            }

            if (command.role() == UserRole.HOSPITAL_ADMIN) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "Invalid invitation role",
                        "Hospital administrator role cannot be assigned through invitation"
                ));
            }

            var emailAddress = new EmailAddress(command.email());

            if (userAccountRepository.existsByEmailAddress(emailAddress)) {
                return Result.failure(ApplicationError.conflict(
                        "UserAccount",
                        "A user account with email '%s' already exists".formatted(command.email())
                ));
            }

            if (userInvitationRepository.existsByHospitalWorkspaceIdAndEmailAddressAndPendingStatus(
                    command.hospitalWorkspaceId(),
                    emailAddress
            )) {
                return Result.failure(ApplicationError.conflict(
                        "UserInvitation",
                        "There is already a pending invitation for '%s'".formatted(command.email())
                ));
            }

            var invitation = new UserInvitation(command);
            var savedInvitation = userInvitationRepository.save(invitation);

            return Result.success(savedInvitation);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("UserInvitation", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("User invitation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<UserAccount, ApplicationError> handle(AssignUserRoleCommand command) {
        try {
            var userAccount = userAccountRepository.findById(command.userAccountId());

            if (userAccount.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "UserAccount",
                        command.userAccountId().toString()
                ));
            }

            var account = userAccount.get();
            account.assignRole(command.role());

            var savedAccount = userAccountRepository.save(account);

            return Result.success(savedAccount);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("UserRole", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Role assignment", exception.getMessage()));
        }
    }
}