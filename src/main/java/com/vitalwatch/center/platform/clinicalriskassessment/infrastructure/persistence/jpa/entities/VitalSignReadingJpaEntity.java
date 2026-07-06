package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.SensorStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * JPA entity for vital sign readings.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vital_sign_readings")
public class VitalSignReadingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "heart_rate", nullable = false)
    private Integer heartRate;

    @Column(name = "hrv", nullable = false)
    private Integer hrv;

    @Column(name = "fatigue_level", nullable = false)
    private Integer fatigueLevel;

    @Column(name = "cortisol_level", nullable = false)
    private Double cortisolLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "sensor_status", nullable = false, length = 30)
    private SensorStatus sensorStatus = SensorStatus.CONNECTED;

    @Column(name = "recorded_at", nullable = false)
    private OffsetDateTime recordedAt = OffsetDateTime.now();

    public VitalSignReadingJpaEntity(
            Long organizationId,
            Long userId,
            Integer heartRate,
            Integer hrv,
            Integer fatigueLevel,
            Double cortisolLevel,
            SensorStatus sensorStatus,
            OffsetDateTime recordedAt
    ) {
        this.organizationId = organizationId;
        this.userId = userId;
        this.heartRate = heartRate;
        this.hrv = hrv;
        this.fatigueLevel = fatigueLevel;
        this.cortisolLevel = cortisolLevel;
        this.sensorStatus = sensorStatus == null ? SensorStatus.CONNECTED : sensorStatus;
        this.recordedAt = recordedAt == null ? OffsetDateTime.now() : recordedAt;
    }
}