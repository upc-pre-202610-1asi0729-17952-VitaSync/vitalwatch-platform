package com.vitalwatch.center.platform.iam.invitations.infrastructure.email.resend;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.invitations.application.internal.outboundservices.InvitationEmailService;
import com.vitalwatch.center.platform.iam.invitations.infrastructure.persistence.jpa.entities.InvitationJpaEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Resend implementation for invitation email delivery.
 */
@Service
@ConditionalOnProperty(
        name = "application.email.resend.enabled",
        havingValue = "true"
)
public class ResendInvitationEmailService implements InvitationEmailService {

    private final String apiUrl;
    private final String apiKey;
    private final String from;
    private final String appPublicUrl;
    private final String logoUrl;
    private final HttpClient httpClient;

    public ResendInvitationEmailService(
            @Value("${application.email.resend.api-url}") String apiUrl,
            @Value("${application.email.resend.api-key}") String apiKey,
            @Value("${application.email.resend.from}") String from,
            @Value("${application.frontend.public-url}") String appPublicUrl,
            @Value("${application.email.logo-url}") String logoUrl
    ) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.from = from;
        this.appPublicUrl = appPublicUrl;
        this.logoUrl = logoUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public void sendInvitation(InvitationJpaEntity invitation) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("RESEND_API_KEY is required when Resend is enabled.");
        }

        try {
            var jsonBody = buildResendJsonBody(invitation);

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "/emails"))
                    .timeout(Duration.ofSeconds(20))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "vitalwatch-platform/1.0")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            var response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException(
                        "Resend email delivery failed. Status: "
                                + response.statusCode()
                                + ". Body: "
                                + response.body()
                );
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Resend email delivery was interrupted.", exception);
        } catch (Exception exception) {
            throw new IllegalStateException("Resend email delivery failed.", exception);
        }
    }

    private String buildResendJsonBody(InvitationJpaEntity invitation) {
        return """
                {
                  "from": "%s",
                  "to": ["%s"],
                  "subject": "%s",
                  "html": "%s"
                }
                """.formatted(
                escapeJson(from),
                escapeJson(invitation.getEmail()),
                escapeJson("Invitación a VitalWatch"),
                escapeJson(buildInvitationHtml(invitation))
        );
    }

    private String buildInvitationHtml(InvitationJpaEntity invitation) {
        var roleLabel = resolveRoleLabel(invitation.getRole());
        var medicalCenterName = invitation.getOrganization() == null
                ? "tu centro médico"
                : invitation.getOrganization().getCommercialName();

        var invitationLink = buildInvitationUrl(invitation.getToken());

        var safeEmail = escapeHtml(invitation.getEmail());
        var safeRoleLabel = escapeHtml(roleLabel);
        var safeMedicalCenterName = escapeHtml(medicalCenterName);
        var safeInvitationLink = escapeHtml(invitationLink);
        var safeLogoUrl = escapeHtml(logoUrl);

        return """
                <!doctype html>
                <html lang="es">
                  <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Invitación a VitalWatch</title>
                  </head>

                  <body style="margin:0; padding:0; background:#eef3ff; font-family:Arial, Helvetica, sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" role="presentation" style="background:#eef3ff; padding:48px 16px;">
                      <tr>
                        <td align="center">
                          <table width="100%%" cellpadding="0" cellspacing="0" role="presentation"
                                 style="max-width:820px; background:#ffffff; border-radius:28px; padding:48px 44px 42px; box-shadow:0 18px 45px rgba(15,23,42,0.08);">

                            <tr>
                              <td align="center" style="padding-bottom:28px;">
                                <table cellpadding="0" cellspacing="0" role="presentation" align="center" style="margin:0 auto;">
                                  <tr>
                                    <td align="center" valign="middle" style="padding-right:14px;">
                                      <img
                                        src="%s"
                                        alt="VitalWatch"
                                        width="58"
                                        height="58"
                                        style="display:block; width:58px; height:58px; border-radius:14px;"
                                      />
                                    </td>

                                    <td align="left" valign="middle">
                                      <h1 style="margin:0; color:#4f74ff; font-size:48px; line-height:1.1; font-weight:800; font-style:italic;">
                                        VitalWatch
                                      </h1>
                                    </td>
                                  </tr>
                                </table>

                                <p style="margin:12px 0 0; color:#64748b; font-size:16px; line-height:1.5;">
                                  Monitoreo inteligente para equipos de salud
                                </p>
                              </td>
                            </tr>

                            <tr>
                              <td align="center" style="padding:0 20px;">
                                <p style="margin:0 0 24px; color:#050b18; font-size:25px; line-height:1.45;">
                                  Hola, fuiste invitado a formar parte como
                                  <strong style="color:#2563eb; font-style:italic;">%s</strong>
                                  en el centro médico
                                  <strong style="color:#2563eb; font-style:italic;">%s</strong>.
                                </p>

                                <p style="margin:0 0 24px; color:#050b18; font-size:24px; line-height:1.45;">
                                  Se utilizará el correo
                                  <strong style="color:#2563eb; font-style:italic;">%s</strong>
                                  para poder registrarte.
                                </p>

                                <p style="margin:0 0 32px; color:#050b18; font-size:24px; line-height:1.45;">
                                  ¡Dale click al siguiente botón para poder iniciar tu registro!
                                </p>

                                <a href="%s"
                                   style="display:inline-block; padding:18px 32px; border-radius:12px; background:#4f74ff; color:#ffffff; text-decoration:none; font-size:24px; font-weight:800; box-shadow:0 14px 28px rgba(79,116,255,0.24);">
                                  Crear mi cuenta
                                </a>

                                <p style="margin:42px 0 16px; color:#050b18; font-size:24px; line-height:1.45;">
                                  Si el botón no funciona, copia y pega este enlace en tu navegador:
                                </p>

                                <a href="%s"
                                   style="display:block; color:#2563eb; font-size:22px; line-height:1.5; font-weight:800; font-style:italic; text-decoration:none; word-break:break-all;">
                                  %s
                                </a>
                              </td>
                            </tr>

                          </table>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """.formatted(
                safeLogoUrl,
                safeRoleLabel,
                safeMedicalCenterName,
                safeEmail,
                safeInvitationLink,
                safeInvitationLink,
                safeInvitationLink
        );
    }

    private String resolveRoleLabel(UserRole role) {
        if (role == UserRole.SUPERVISOR) {
            return "Supervisor clínico";
        }

        if (role == UserRole.HOSPITAL_ADMIN) {
            return "Administrador hospitalario";
        }

        return "Personal médico";
    }

    private String buildInvitationUrl(String token) {
        var encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        return appPublicUrl + "/accept-invitation?token=" + encodedToken;
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#039;");
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}