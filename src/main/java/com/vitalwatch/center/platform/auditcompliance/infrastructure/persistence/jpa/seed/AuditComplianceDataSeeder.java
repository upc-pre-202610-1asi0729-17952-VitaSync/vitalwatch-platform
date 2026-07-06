package com.vitalwatch.center.platform.auditcompliance.infrastructure.persistence.jpa.seed;

import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogSeverity;
import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogType;
import com.vitalwatch.center.platform.auditcompliance.infrastructure.persistence.jpa.entities.AuditLogJpaEntity;
import com.vitalwatch.center.platform.auditcompliance.infrastructure.persistence.jpa.repositories.AuditLogJpaRepository;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Seeds audit compliance data required by the frontend audit view.
 */
@Component
public class AuditComplianceDataSeeder {

    private final AuditLogJpaRepository auditLogRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final UserJpaRepository userRepository;

    public AuditComplianceDataSeeder(
            AuditLogJpaRepository auditLogRepository,
            OrganizationJpaRepository organizationRepository,
            UserJpaRepository userRepository
    ) {
        this.auditLogRepository = auditLogRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        organizationRepository.findAll()
                .forEach(this::seedAuditLogsForOrganization);
    }

    private void seedAuditLogsForOrganization(OrganizationJpaEntity organization) {
        if (auditLogRepository.existsByOrganizationId(organization.getId())) {
            return;
        }

        var users = userRepository.findAll()
                .stream()
                .filter(user -> user.getOrganization().getId().equals(organization.getId()))
                .toList();

        var admin = findUserByRole(users, UserRole.HOSPITAL_ADMIN);
        var supervisor = findUserByRole(users, UserRole.SUPERVISOR);
        var doctor = findUserByRole(users, UserRole.DOCTOR);

        var actorUserId = admin == null ? null : admin.getId();
        var supervisorId = supervisor == null ? actorUserId : supervisor.getId();
        var doctorId = doctor == null ? actorUserId : doctor.getId();

        var now = OffsetDateTime.now();

        var logs = List.of(
                new AuditLogJpaEntity(
                        organization.getId(),
                        actorUserId,
                        AuditLogType.USER_INVITED,
                        AuditLogSeverity.INFO,
                        "INVITATION",
                        null,
                        "Se envió una invitación para incorporar nuevo personal médico.",
                        now.minusDays(5)
                ),
                new AuditLogJpaEntity(
                        organization.getId(),
                        doctorId,
                        AuditLogType.USER_REGISTERED,
                        AuditLogSeverity.INFO,
                        "USER",
                        doctorId,
                        "Un usuario invitado completó su registro en VitalWatch.",
                        now.minusDays(4)
                ),
                new AuditLogJpaEntity(
                        organization.getId(),
                        actorUserId,
                        AuditLogType.TEAM_CREATED,
                        AuditLogSeverity.INFO,
                        "CARE_TEAM",
                        1L,
                        "Se creó un equipo clínico para el seguimiento del personal.",
                        now.minusDays(3)
                ),
                new AuditLogJpaEntity(
                        organization.getId(),
                        supervisorId,
                        AuditLogType.PREVENTIVE_ACTION_CREATED,
                        AuditLogSeverity.WARNING,
                        "PREVENTIVE_ACTION",
                        1L,
                        "Se registró una acción preventiva por fatiga moderada.",
                        now.minusDays(2)
                ),
                new AuditLogJpaEntity(
                        organization.getId(),
                        supervisorId,
                        AuditLogType.ALERT_RESOLVED,
                        AuditLogSeverity.INFO,
                        "CLINICAL_ALERT",
                        1L,
                        "Una alerta clínica fue marcada como resuelta por el supervisor.",
                        now.minusDays(1)
                ),
                new AuditLogJpaEntity(
                        organization.getId(),
                        doctorId,
                        AuditLogType.SHIFT_CHECK_IN,
                        AuditLogSeverity.INFO,
                        "SHIFT_RECORD",
                        1L,
                        "El personal médico inició un turno programado.",
                        now.minusHours(8)
                ),
                new AuditLogJpaEntity(
                        organization.getId(),
                        supervisorId,
                        AuditLogType.ANOMALY_REVIEWED,
                        AuditLogSeverity.WARNING,
                        "VITAL_SIGN_ANOMALY",
                        1L,
                        "Una anomalía biométrica fue revisada durante el monitoreo preventivo.",
                        now.minusHours(4)
                )
        );

        auditLogRepository.saveAll(logs);
    }

    private UserJpaEntity findUserByRole(List<UserJpaEntity> users, UserRole role) {
        return users.stream()
                .filter(user -> user.getRole() == role)
                .findFirst()
                .orElse(null);
    }
}