package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.seed;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.PreventiveActionStatus;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.PreventiveActionType;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities.PreventiveActionJpaEntity;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.repositories.PreventiveActionJpaRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * Seeds preventive actions required by the frontend recovery views.
 */
@Component
public class StaffRecoveryDataSeeder {

    private final UserJpaRepository userRepository;
    private final PreventiveActionJpaRepository preventiveActionRepository;

    public StaffRecoveryDataSeeder(
            UserJpaRepository userRepository,
            PreventiveActionJpaRepository preventiveActionRepository
    ) {
        this.userRepository = userRepository;
        this.preventiveActionRepository = preventiveActionRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        var users = userRepository.findAll();

        var doctors = users.stream()
                .filter(user -> user.getRole() == UserRole.DOCTOR)
                .toList();

        doctors.forEach(doctor -> {
            var organizationId = doctor.getOrganization().getId();

            if (preventiveActionRepository.existsByOrganizationIdAndUserId(
                    organizationId,
                    doctor.getId()
            )) {
                return;
            }

            var supervisor = findSupervisorForOrganization(organizationId);
            var supervisorId = supervisor == null ? doctor.getId() : supervisor.getId();

            seedActionsForDoctor(organizationId, supervisorId, doctor.getId());
        });
    }

    private UserJpaEntity findSupervisorForOrganization(Long organizationId) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getOrganization().getId().equals(organizationId))
                .filter(user -> user.getRole() == UserRole.SUPERVISOR)
                .findFirst()
                .orElse(null);
    }

    private void seedActionsForDoctor(
            Long organizationId,
            Long supervisorId,
            Long userId
    ) {
        var now = OffsetDateTime.now();

        preventiveActionRepository.save(new PreventiveActionJpaEntity(
                organizationId,
                supervisorId,
                userId,
                PreventiveActionType.RECOVERY_BREAK,
                PreventiveActionStatus.PENDING,
                "Tomar una pausa preventiva de 15 minutos y registrar evolución del estado de fatiga.",
                now.minusHours(2),
                null
        ));

        preventiveActionRepository.save(new PreventiveActionJpaEntity(
                organizationId,
                supervisorId,
                userId,
                PreventiveActionType.SUPERVISOR_CHECK_IN,
                PreventiveActionStatus.COMPLETED,
                "Seguimiento realizado por el supervisor clínico durante el turno.",
                now.minusDays(1),
                now.minusHours(20)
        ));
    }
}