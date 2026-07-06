package com.vitalwatch.center.platform.iam.invitations.infrastructure.email.noop;

import com.vitalwatch.center.platform.iam.invitations.application.internal.outboundservices.InvitationEmailService;
import com.vitalwatch.center.platform.iam.invitations.infrastructure.persistence.jpa.entities.InvitationJpaEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * No-operation email service used when real email delivery is disabled.
 */
@Service
@ConditionalOnProperty(
        name = "application.email.resend.enabled",
        havingValue = "false",
        matchIfMissing = true
)
public class NoOpInvitationEmailService implements InvitationEmailService {

    @Override
    public void sendInvitation(InvitationJpaEntity invitation) {
        // Email delivery is disabled in this environment.
    }
}