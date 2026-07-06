package com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities.TeamMemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for team member persistence.
 */
public interface TeamMemberJpaRepository extends JpaRepository<TeamMemberJpaEntity, Long> {

    List<TeamMemberJpaEntity> findByTeamId(Long teamId);

    List<TeamMemberJpaEntity> findByUserId(Long userId);

    boolean existsByTeamIdAndUserId(Long teamId, Long userId);

    boolean existsByUserId(Long userId);
}