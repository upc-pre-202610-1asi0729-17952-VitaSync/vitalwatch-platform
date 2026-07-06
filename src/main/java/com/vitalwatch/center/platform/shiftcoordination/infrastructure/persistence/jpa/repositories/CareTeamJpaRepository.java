package com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities.CareTeamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for care team persistence.
 */
public interface CareTeamJpaRepository extends JpaRepository<CareTeamJpaEntity, Long> {

    List<CareTeamJpaEntity> findAllByOrderByNameAsc();

    List<CareTeamJpaEntity> findByOrganizationIdOrderByNameAsc(Long organizationId);

    List<CareTeamJpaEntity> findBySupervisorId(Long supervisorId);

    List<CareTeamJpaEntity> findByWorkAreaId(Long workAreaId);

    boolean existsByOrganizationIdAndNameIgnoreCase(Long organizationId, String name);
}