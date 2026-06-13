package com.vitalwatch.center.platform.incidents.application.internal.commandservices;

import com.vitalwatch.center.platform.incidents.application.commandservices.IncidentCommandService;
import com.vitalwatch.center.platform.incidents.domain.model.aggregates.Incident;
import com.vitalwatch.center.platform.incidents.domain.model.commands.AcknowledgeIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.model.commands.CancelIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.model.commands.CreateIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.model.commands.EscalateIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.model.commands.ResolveIncidentCommand;
import com.vitalwatch.center.platform.incidents.domain.repositories.IncidentRepository;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Incident command service implementation.
 */
@Service
public class IncidentCommandServiceImpl implements IncidentCommandService {

    private final IncidentRepository incidentRepository;

    public IncidentCommandServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    @Transactional
    public Result<Incident, ApplicationError> handle(CreateIncidentCommand command) {
        try {
            var incident = new Incident(command);
            var savedIncident = incidentRepository.save(incident);

            return Result.success(savedIncident);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("Incident", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Incident creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<Incident, ApplicationError> handle(AcknowledgeIncidentCommand command) {
        try {
            var incident = incidentRepository.findById(command.incidentId());

            if (incident.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Incident", command.incidentId().toString()));
            }

            var incidentToAcknowledge = incident.get();
            incidentToAcknowledge.acknowledge(command.acknowledgedByUserAccountId());

            var savedIncident = incidentRepository.save(incidentToAcknowledge);

            return Result.success(savedIncident);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("Incident", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("Incident", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Incident acknowledgement", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<Incident, ApplicationError> handle(EscalateIncidentCommand command) {
        try {
            var incident = incidentRepository.findById(command.incidentId());

            if (incident.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Incident", command.incidentId().toString()));
            }

            var incidentToEscalate = incident.get();
            incidentToEscalate.escalate(command.escalatedByUserAccountId(), command.escalationLevel());

            var savedIncident = incidentRepository.save(incidentToEscalate);

            return Result.success(savedIncident);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("Incident", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("Incident", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Incident escalation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<Incident, ApplicationError> handle(ResolveIncidentCommand command) {
        try {
            var incident = incidentRepository.findById(command.incidentId());

            if (incident.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Incident", command.incidentId().toString()));
            }

            var incidentToResolve = incident.get();
            incidentToResolve.resolve(command.resolvedByUserAccountId(), command.resolutionNotes());

            var savedIncident = incidentRepository.save(incidentToResolve);

            return Result.success(savedIncident);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("Incident", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("Incident", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Incident resolution", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<Incident, ApplicationError> handle(CancelIncidentCommand command) {
        try {
            var incident = incidentRepository.findById(command.incidentId());

            if (incident.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Incident", command.incidentId().toString()));
            }

            var incidentToCancel = incident.get();
            incidentToCancel.cancel(command.cancelledByUserAccountId(), command.cancellationReason());

            var savedIncident = incidentRepository.save(incidentToCancel);

            return Result.success(savedIncident);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("Incident", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("Incident", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Incident cancellation", exception.getMessage()));
        }
    }
}