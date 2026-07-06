package com.vitalwatch.center.platform.iam.invitations.application.internal.outboundservices;

import com.vitalwatch.center.platform.iam.invitations.infrastructure.persistence.jpa.entities.InvitationJpaEntity;

/**
 * Service contract for sending invitation emails.
 */
public interface InvitationEmailService {

    void sendInvitation(InvitationJpaEntity invitation);
}