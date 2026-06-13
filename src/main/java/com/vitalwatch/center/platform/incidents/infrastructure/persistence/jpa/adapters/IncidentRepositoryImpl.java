package com.vitalwatch.center.platform.incidents.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.incidents.domain.model.aggregates.Incident;
import com.vitalwatch.center.platform.incidents.domain.model.enums.IncidentStatus;
import com.vitalwatch.center.platform.incidents.domain.repositories.IncidentRepository;
import com.vitalwatch.center.platform.incidents.infrastructure.persistence.jpa.assemblers.IncidentPersistenceAssembler;
import com.vitalwatch.center.platform.incidents.infrastructure.persistence.jpa.repositories.IncidentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for Incident.
 */
@Repository
public class IncidentRepositoryImpl implements IncidentRepository {

    private final IncidentPersistenceRepository repository;

    public IncidentRepositoryImpl(IncidentPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Incident> findById(Long id) {
        return repository.findById(id)
                .map(IncidentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Incident> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findAllByHospitalWorkspaceIdOrderByIncidentCreatedAtDesc(hospitalWorkspaceId)
                .stream()
                .map(IncidentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Incident> findAllByHospitalWorkspaceIdAndStatus(Long hospitalWorkspaceId, IncidentStatus status) {
        return repository.findAllByHospitalWorkspaceIdAndStatusOrderByIncidentCreatedAtDesc(hospitalWorkspaceId, status)
                .stream()
                .map(IncidentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Incident> findAllByReportedUserAccountId(Long reportedUserAccountId) {
        return repository.findAllByReportedUserAccountIdOrderByIncidentCreatedAtDesc(reportedUserAccountId)
                .stream()
                .map(IncidentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Incident save(Incident incident) {
        var savedEntity = repository.save(
                IncidentPersistenceAssembler.toPersistenceFromDomain(incident)
        );

        return IncidentPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }
}
