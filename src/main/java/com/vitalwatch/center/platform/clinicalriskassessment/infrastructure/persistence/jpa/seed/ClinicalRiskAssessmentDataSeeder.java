package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.seed;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.*;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.*;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories.*;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Seeds clinical risk assessment data required by the frontend dashboards.
 */
@Component
public class ClinicalRiskAssessmentDataSeeder {

    private final UserJpaRepository userRepository;
    private final RiskAssessmentJpaRepository riskAssessmentRepository;
    private final ClinicalAlertJpaRepository clinicalAlertRepository;
    private final VitalSignReadingJpaRepository vitalSignReadingRepository;
    private final VitalSignAnomalyJpaRepository vitalSignAnomalyRepository;

    public ClinicalRiskAssessmentDataSeeder(
            UserJpaRepository userRepository,
            RiskAssessmentJpaRepository riskAssessmentRepository,
            ClinicalAlertJpaRepository clinicalAlertRepository,
            VitalSignReadingJpaRepository vitalSignReadingRepository,
            VitalSignAnomalyJpaRepository vitalSignAnomalyRepository
    ) {
        this.userRepository = userRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.clinicalAlertRepository = clinicalAlertRepository;
        this.vitalSignReadingRepository = vitalSignReadingRepository;
        this.vitalSignAnomalyRepository = vitalSignAnomalyRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        var doctors = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == UserRole.DOCTOR)
                .toList();

        doctors.forEach(this::seedClinicalDataForDoctor);
    }

    private void seedClinicalDataForDoctor(UserJpaEntity doctor) {
        var organizationId = doctor.getOrganization().getId();
        var userId = doctor.getId();

        var hasRiskAssessment = riskAssessmentRepository
                .findFirstByOrganizationIdAndUserIdOrderByLastUpdatedAtDesc(organizationId, userId)
                .isPresent();

        if (hasRiskAssessment) {
            return;
        }

        var baseFatigue = Math.toIntExact((userId * 17) % 75) + 15;
        var riskLevel = resolveRiskLevel(baseFatigue);
        var heartRate = 68 + Math.toIntExact((userId * 7) % 32);
        var hrv = Math.max(18, 65 - baseFatigue / 2);
        var now = OffsetDateTime.now();

        riskAssessmentRepository.save(new RiskAssessmentJpaEntity(
                organizationId,
                userId,
                baseFatigue,
                riskLevel,
                heartRate,
                hrv,
                now
        ));

        seedReadings(organizationId, userId, baseFatigue, heartRate, hrv, now);

        if (riskLevel == RiskLevel.HIGH || riskLevel == RiskLevel.CRITICAL) {
            clinicalAlertRepository.save(new ClinicalAlertJpaEntity(
                    organizationId,
                    userId,
                    riskLevel,
                    ClinicalAlertStatus.ACTIVE,
                    "Se detectó un nivel elevado de fatiga que requiere seguimiento preventivo.",
                    now.minusMinutes(20)
            ));

            vitalSignAnomalyRepository.save(new VitalSignAnomalyJpaEntity(
                    organizationId,
                    userId,
                    VitalSignAnomalyType.FATIGUE_SPIKE,
                    riskLevel,
                    VitalSignAnomalyStatus.OPEN,
                    (double) baseFatigue,
                    70.0,
                    "Incremento de fatiga detectado durante el turno.",
                    now.minusMinutes(15)
            ));
        }
    }

    private void seedReadings(
            Long organizationId,
            Long userId,
            Integer baseFatigue,
            Integer heartRate,
            Integer hrv,
            OffsetDateTime now
    ) {
        List<Integer> variations = List.of(-8, -5, -2, 0, 3, 5, 7);

        for (int index = 0; index < variations.size(); index++) {
            var fatigue = clamp(baseFatigue + variations.get(index), 5, 95);
            var cortisol = 320.0 + fatigue * 3.2;
            var recordedAt = now.minusDays(6L - index);

            vitalSignReadingRepository.save(new VitalSignReadingJpaEntity(
                    organizationId,
                    userId,
                    heartRate + index,
                    Math.max(18, hrv - index),
                    fatigue,
                    cortisol,
                    SensorStatus.CONNECTED,
                    recordedAt
            ));
        }
    }

    private RiskLevel resolveRiskLevel(Integer fatigueLevel) {
        if (fatigueLevel >= 85) {
            return RiskLevel.CRITICAL;
        }

        if (fatigueLevel >= 70) {
            return RiskLevel.HIGH;
        }

        if (fatigueLevel >= 45) {
            return RiskLevel.MODERATE;
        }

        return RiskLevel.LOW;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}