package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.PreventiveActionStatus;
import com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities.PreventiveActionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for preventive action persistence.
 */
public interface PreventiveActionJpaRepository extends JpaRepository<PreventiveActionJpaEntity, Long> {

    List<PreventiveActionJpaEntity> findByOrganizationIdOrderByCreatedAtDesc(Long organizationId);

    List<PreventiveActionJpaEntity> findByOrganizationIdAndSupervisorIdOrderByCreatedAtDesc(
            Long organizationId,
            Long supervisorId
    );

    List<PreventiveActionJpaEntity> findByOrganizationIdAndUserIdOrderByCreatedAtDesc(
            Long organizationId,
            Long userId
    );

    List<PreventiveActionJpaEntity> findByOrganizationIdAndStatusOrderByCreatedAtDesc(
            Long organizationId,
            PreventiveActionStatus status
    );

    boolean existsByOrganizationIdAndUserId(Long organizationId, Long userId);
}