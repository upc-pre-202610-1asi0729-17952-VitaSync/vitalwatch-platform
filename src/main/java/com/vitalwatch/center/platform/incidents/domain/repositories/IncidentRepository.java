package com.vitalwatch.center.platform.incidents.domain.repositories;

import com.vitalwatch.center.platform.incidents.domain.model.aggregates.Incident;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for incidents.
 */
public interface IncidentRepository {

    Optional<Incident> findById(Long id);

    List<Incident> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    List<Incident> findAllByHospitalWorkspaceIdAndStatus(Long hospitalWorkspaceId, IncidentStatus status);

    List<Incident> findAllByReportedUserAccountId(Long reportedUserAccountId);

    Incident save(Incident incident);
}