package com.vitalwatch.center.platform.incidents.application.queryservices;

import com.vitalwatch.center.platform.incidents.domain.model.aggregates.Incident;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetIncidentByIdQuery;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetIncidentsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetIncidentsByReportedUserAccountIdQuery;
import com.vitalwatch.center.platform.incidents.domain.model.queries.GetOpenIncidentsByHospitalWorkspaceIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for incident queries.
 */
public interface IncidentQueryService {

    Optional<Incident> handle(GetIncidentByIdQuery query);

    List<Incident> handle(GetIncidentsByHospitalWorkspaceIdQuery query);

    List<Incident> handle(GetOpenIncidentsByHospitalWorkspaceIdQuery query);

    List<Incident> handle(GetIncidentsByReportedUserAccountIdQuery query);
}