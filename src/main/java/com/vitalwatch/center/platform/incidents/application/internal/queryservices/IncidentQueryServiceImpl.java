package com.vitalwatch.center.platform.incidents.application.internal.queryservices;

import com.vitalwatch.center.platform.incidents.application.queryservices.IncidentQueryService;
import com.vitalwatch.center.platform.incidents.domain.model.aggregates.Incident;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentStatus;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetIncidentByIdQuery;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetIncidentsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetIncidentsByReportedUserAccountIdQuery;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetOpenIncidentsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.incidents.domain.repositories.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Incident query service implementation.
 */
@Service
public class IncidentQueryServiceImpl implements IncidentQueryService {

    private final IncidentRepository incidentRepository;

    public IncidentQueryServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    public Optional<Incident> handle(GetIncidentByIdQuery query) {
        return incidentRepository.findById(query.incidentId());
    }

    @Override
    public List<Incident> handle(GetIncidentsByHospitalWorkspaceIdQuery query) {
        return incidentRepository.findAllByHospitalWorkspaceId(query.hospitalWorkspaceId());
    }

    @Override
    public List<Incident> handle(GetOpenIncidentsByHospitalWorkspaceIdQuery query) {
        return incidentRepository.findAllByHospitalWorkspaceIdAndStatus(
                query.hospitalWorkspaceId(),
                IncidentStatus.OPEN
        );
    }

    @Override
    public List<Incident> handle(GetIncidentsByReportedUserAccountIdQuery query) {
        return incidentRepository.findAllByReportedUserAccountId(query.reportedUserAccountId());
    }
}