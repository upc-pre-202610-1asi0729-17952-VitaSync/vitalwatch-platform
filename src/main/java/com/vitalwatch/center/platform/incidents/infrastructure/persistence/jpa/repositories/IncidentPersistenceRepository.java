package com.vitalwatch.center.platform.incidents.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentStatus;
import com.vitalwatch.center.platform.incidents.infrastructure.persistence.jpa.entities.IncidentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for incidents.
 */
public interface IncidentPersistenceRepository extends JpaRepository<IncidentPersistenceEntity, Long> {

    List<IncidentPersistenceEntity> findAllByHospitalWorkspaceIdOrderByIncidentCreatedAtDesc(Long hospitalWorkspaceId);

    List<IncidentPersistenceEntity> findAllByHospitalWorkspaceIdAndStatusOrderByIncidentCreatedAtDesc(
            Long hospitalWorkspaceId,
            IncidentStatus status
    );

    List<IncidentPersistenceEntity> findAllByReportedUserAccountIdOrderByIncidentCreatedAtDesc(Long reportedUserAccountId);
}
