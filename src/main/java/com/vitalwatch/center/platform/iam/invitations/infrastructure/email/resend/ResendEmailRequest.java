package com.vitalwatch.center.platform.iam.invitations.infrastructure.email.resend;

import java.util.List;

/**
 * Request body sent to Resend Email API.
 */
public record ResendEmailRequest(
        String from,
        List<String> to,
        String subject,
        String html
) {
}