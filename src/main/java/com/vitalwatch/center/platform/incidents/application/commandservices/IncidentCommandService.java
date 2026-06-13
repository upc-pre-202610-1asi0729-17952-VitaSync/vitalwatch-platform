package com.vitalwatch.center.platform.incidents.application.commandservices;

import com.vitalwatch.center.platform.incidents.domain.model.aggregates.Incident;
import com.vitalwatch.center.platform.incidents.domain.model.commands.AcknowledgeIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.model.commands.CancelIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.model.commands.CreateIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.model.commands.EscalateIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.model.commands.ResolveIncidentCommand;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;

/**
 * Application service contract for incident commands.
 */
public interface IncidentCommandService {

    Result<Incident, ApplicationError> handle(CreateIncidentCommand command);

    Result<Incident, ApplicationError> handle(AcknowledgeIncidentCommand command);

    Result<Incident, ApplicationError> handle(EscalateIncidentCommand command);

    Result<Incident, ApplicationError> handle(ResolveIncidentCommand command);

    Result<Incident, ApplicationError> handle(CancelIncidentCommand command);
}