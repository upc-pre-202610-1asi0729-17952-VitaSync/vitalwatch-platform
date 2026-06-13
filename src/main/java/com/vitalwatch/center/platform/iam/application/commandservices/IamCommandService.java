package com.vitalwatch.center.platform.iam.application.commandservices;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;
import com.vitalwatch.center.platform.iam.domain.model.commands.AssignUserRoleCommand;
import com.vitalwatch.center.platform.iam.domain.model.commands.CreateHospitalWorkspaceCommand;
import com.vitalwatch.center.platform.iam.domain.model.commands.InviteUserCommand;
import com.vitalwatch.center.platform.iam.domain.model.commands.RegisterUserAccountCommand;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;

/**
 * Application service contract for IAM commands.
 */
public interface IamCommandService {

    Result<HospitalWorkspace, ApplicationError> handle(CreateHospitalWorkspaceCommand command);

    Result<UserAccount, ApplicationError> handle(RegisterUserAccountCommand command);

    Result<UserInvitation, ApplicationError> handle(InviteUserCommand command);

    Result<UserAccount, ApplicationError> handle(AssignUserRoleCommand command);
}