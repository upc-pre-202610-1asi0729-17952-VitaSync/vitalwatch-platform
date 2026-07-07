package com.vitalwatch.center.platform.clinicalriskassessment.application.internal.services;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.*;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities.*;
import com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.repositories.*;
import com.vitalwatch.center.platform.clinicalriskassessment.interfaces.rest.resources.SimulationTickResource;
import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Random;

/**
 * Service that simulates IoT wearable readings from medical staff smartwatches.
 */
@Service
public class ClinicalSimulationService {

    private static final int MAX_READINGS_PER_USER = 60;
    private static final int MAX_ALERTS_PER_USER = 10;
    private static final int MAX_ANOMALIES_PER_USER = 10;

    private final UserJpaRepository userRepository;
    private final OrganizationJpaRepository organizationRepository;
    private final VitalSignReadingJpaRepository vitalSignReadingRepository;
    private final RiskAssessmentJpaRepository riskAssessmentRepository;
    private final ClinicalAlertJpaRepository clinicalAlertRepository;
    private final VitalSignAnomalyJpaRepository vitalSignAnomalyRepository;

    private final Random random = new Random();

    public ClinicalSimulationService(
            UserJpaRepository userRepository,
            OrganizationJpaRepository organizationRepository,
            VitalSignReadingJpaRepository vitalSignReadingRepository,
            RiskAssessmentJpaRepository riskAssessmentRepository,
            ClinicalAlertJpaRepository clinicalAlertRepository,
            VitalSignAnomalyJpaRepository vitalSignAnomalyRepository
    ) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.vitalSignReadingRepository = vitalSignReadingRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.clinicalAlertRepository = clinicalAlertRepository;
        this.vitalSignAnomalyRepository = vitalSignAnomalyRepository;
    }

    @Transactional
    public SimulationTickResource simulateOrganization(Long organizationId) {
        if (!organizationRepository.existsById(organizationId)) {
            return new SimulationTickResource(
                    organizationId,
                    0,
                    0,
                    0,
                    0,
                    0,
                    OffsetDateTime.now()
            );
        }

        var counters = new SimulationCounters();

        var doctors = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == UserRole.DOCTOR)
                .filter(user -> user.getOrganization() != null)
                .filter(user -> user.getOrganization().getId().equals(organizationId))
                .toList();

        doctors.forEach(doctor -> simulateDoctor(doctor, counters));

        return new SimulationTickResource(
                organizationId,
                counters.doctorsProcessed,
                counters.readingsCreated,
                counters.riskAssessmentsUpdated,
                counters.alertsCreated,
                counters.anomaliesCreated,
                OffsetDateTime.now()
        );
    }

    @Transactional
    public SimulationTickResource simulateAllOrganizations() {
        var counters = new SimulationCounters();

        var doctors = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == UserRole.DOCTOR)
                .filter(user -> user.getOrganization() != null)
                .toList();

        doctors.forEach(doctor -> simulateDoctor(doctor, counters));

        return new SimulationTickResource(
                null,
                counters.doctorsProcessed,
                counters.readingsCreated,
                counters.riskAssessmentsUpdated,
                counters.alertsCreated,
                counters.anomaliesCreated,
                OffsetDateTime.now()
        );
    }

    private void simulateDoctor(
            UserJpaEntity doctor,
            SimulationCounters counters
    ) {
        var organizationId = doctor.getOrganization().getId();
        var userId = doctor.getId();
        var now = OffsetDateTime.now();

        var latestAssessment = riskAssessmentRepository
                .findFirstByOrganizationIdAndUserIdOrderByLastUpdatedAtDesc(organizationId, userId);

        var previousFatigue = latestAssessment
                .map(RiskAssessmentJpaEntity::getFatigueLevel)
                .orElse(35 + random.nextInt(35));

        var fatigueLevel = clamp(previousFatigue + random.nextInt(17) - 8, 5, 95);
        var heartRate = clamp(62 + fatigueLevel / 2 + random.nextInt(19) - 9, 55, 130);
        var hrv = clamp(85 - fatigueLevel / 2 + random.nextInt(11) - 5, 15, 90);
        var cortisolLevel = roundOneDecimal(280.0 + fatigueLevel * 3.8 + random.nextDouble() * 35.0);
        var sensorStatus = generateSensorStatus();
        var riskLevel = resolveRiskLevel(fatigueLevel);

        vitalSignReadingRepository.save(new VitalSignReadingJpaEntity(
                organizationId,
                userId,
                heartRate,
                hrv,
                fatigueLevel,
                cortisolLevel,
                sensorStatus,
                now
        ));

        counters.readingsCreated++;

        if (latestAssessment.isPresent()) {
            var assessment = latestAssessment.get();
            assessment.setFatigueLevel(fatigueLevel);
            assessment.setRiskLevel(riskLevel);
            assessment.setHeartRate(heartRate);
            assessment.setHrv(hrv);
            assessment.setLastUpdatedAt(now);
            riskAssessmentRepository.save(assessment);
        } else {
            riskAssessmentRepository.save(new RiskAssessmentJpaEntity(
                    organizationId,
                    userId,
                    fatigueLevel,
                    riskLevel,
                    heartRate,
                    hrv,
                    now
            ));
        }

        counters.riskAssessmentsUpdated++;
        counters.doctorsProcessed++;

        if (shouldGenerateAlert(riskLevel)) {
            clinicalAlertRepository.save(new ClinicalAlertJpaEntity(
                    organizationId,
                    userId,
                    riskLevel,
                    ClinicalAlertStatus.ACTIVE,
                    buildAlertMessage(riskLevel, fatigueLevel),
                    now
            ));

            counters.alertsCreated++;
        }

        var anomaly = buildAnomalyIfNeeded(
                organizationId,
                userId,
                fatigueLevel,
                heartRate,
                hrv,
                sensorStatus,
                riskLevel,
                now
        );

        if (anomaly != null) {
            vitalSignAnomalyRepository.save(anomaly);
            counters.anomaliesCreated++;
        }

        deleteOldReadingsForDoctor(organizationId, userId);
        deleteOldAlertsForDoctor(organizationId, userId);
        deleteOldAnomaliesForDoctor(organizationId, userId);
    }

    private void deleteOldReadingsForDoctor(Long organizationId, Long userId) {
        vitalSignReadingRepository.deleteOlderThanLatestByOrganizationAndUser(
                organizationId,
                userId,
                MAX_READINGS_PER_USER
        );
    }

    private void deleteOldAlertsForDoctor(Long organizationId, Long userId) {
        clinicalAlertRepository.deleteOlderThanLatestByOrganizationAndUser(
                organizationId,
                userId,
                MAX_ALERTS_PER_USER
        );
    }

    private void deleteOldAnomaliesForDoctor(Long organizationId, Long userId) {
        vitalSignAnomalyRepository.deleteOlderThanLatestByOrganizationAndUser(
                organizationId,
                userId,
                MAX_ANOMALIES_PER_USER
        );
    }

    private SensorStatus generateSensorStatus() {
        var value = random.nextInt(100);

        if (value < 3) {
            return SensorStatus.DISCONNECTED;
        }

        if (value < 11) {
            return SensorStatus.LOW_BATTERY;
        }

        return SensorStatus.CONNECTED;
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

    private boolean shouldGenerateAlert(RiskLevel riskLevel) {
        if (riskLevel == RiskLevel.CRITICAL) {
            return true;
        }

        if (riskLevel == RiskLevel.HIGH) {
            return random.nextInt(100) < 45;
        }

        return false;
    }

    private String buildAlertMessage(RiskLevel riskLevel, Integer fatigueLevel) {
        return switch (riskLevel) {
            case CRITICAL -> "Nivel crítico de fatiga detectado. Se recomienda intervención inmediata.";
            case HIGH -> "Nivel alto de fatiga detectado. Se recomienda seguimiento preventivo.";
            case MODERATE -> "Fatiga moderada detectada durante el monitoreo.";
            case LOW -> "Lectura biométrica dentro de parámetros normales.";
        };
    }

    private VitalSignAnomalyJpaEntity buildAnomalyIfNeeded(
            Long organizationId,
            Long userId,
            Integer fatigueLevel,
            Integer heartRate,
            Integer hrv,
            SensorStatus sensorStatus,
            RiskLevel riskLevel,
            OffsetDateTime now
    ) {
        if (sensorStatus == SensorStatus.DISCONNECTED) {
            return new VitalSignAnomalyJpaEntity(
                    organizationId,
                    userId,
                    VitalSignAnomalyType.SENSOR_SIGNAL_LOSS,
                    RiskLevel.MODERATE,
                    VitalSignAnomalyStatus.OPEN,
                    0.0,
                    1.0,
                    "Pérdida de señal del wearable durante el monitoreo.",
                    now
            );
        }

        if (fatigueLevel >= 75) {
            return new VitalSignAnomalyJpaEntity(
                    organizationId,
                    userId,
                    VitalSignAnomalyType.FATIGUE_SPIKE,
                    riskLevel,
                    VitalSignAnomalyStatus.OPEN,
                    fatigueLevel.doubleValue(),
                    70.0,
                    "Incremento de fatiga detectado por el smartwatch.",
                    now
            );
        }

        if (heartRate >= 105) {
            return new VitalSignAnomalyJpaEntity(
                    organizationId,
                    userId,
                    VitalSignAnomalyType.HEART_RATE_SPIKE,
                    RiskLevel.HIGH,
                    VitalSignAnomalyStatus.OPEN,
                    heartRate.doubleValue(),
                    100.0,
                    "Ritmo cardíaco elevado detectado durante el turno.",
                    now
            );
        }

        if (hrv <= 28) {
            return new VitalSignAnomalyJpaEntity(
                    organizationId,
                    userId,
                    VitalSignAnomalyType.LOW_HRV,
                    RiskLevel.MODERATE,
                    VitalSignAnomalyStatus.OPEN,
                    hrv.doubleValue(),
                    30.0,
                    "Variabilidad cardíaca baja detectada.",
                    now
            );
        }

        return null;
    }

    private Integer clamp(Integer value, Integer min, Integer max) {
        return Math.max(min, Math.min(max, value));
    }

    private Double roundOneDecimal(Double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private static class SimulationCounters {
        private Integer doctorsProcessed = 0;
        private Integer readingsCreated = 0;
        private Integer riskAssessmentsUpdated = 0;
        private Integer alertsCreated = 0;
        private Integer anomaliesCreated = 0;
    }
}