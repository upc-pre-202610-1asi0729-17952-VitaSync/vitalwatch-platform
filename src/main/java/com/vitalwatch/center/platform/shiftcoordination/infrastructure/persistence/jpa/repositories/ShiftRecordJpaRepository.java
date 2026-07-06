package com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities.ShiftRecordJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for shift record persistence.
 */
public interface ShiftRecordJpaRepository extends JpaRepository<ShiftRecordJpaEntity, Long> {

    List<ShiftRecordJpaEntity> findByOrganizationIdOrderByScheduledStartDesc(Long organizationId);

    List<ShiftRecordJpaEntity> findByOrganizationIdAndUserIdOrderByScheduledStartDesc(
            Long organizationId,
            Long userId
    );
}