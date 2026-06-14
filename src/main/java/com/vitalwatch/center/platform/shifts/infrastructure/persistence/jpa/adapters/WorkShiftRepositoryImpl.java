package com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;
import com.vitalwatch.center.platform.shifts.domain.repositories.WorkShiftRepository;
import com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.assemblers.WorkShiftPersistenceAssembler;
import com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.repositories.WorkShiftPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for WorkShift.
 */
@Repository
public class WorkShiftRepositoryImpl implements WorkShiftRepository {

    private final WorkShiftPersistenceRepository repository;

    public WorkShiftRepositoryImpl(WorkShiftPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<WorkShift> findById(Long id) {
        return repository.findById(id)
                .map(WorkShiftPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<WorkShift> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findAllByHospitalWorkspaceIdOrderByStartsAtDesc(hospitalWorkspaceId)
                .stream()
                .map(WorkShiftPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public WorkShift save(WorkShift workShift) {
        var savedEntity = repository.save(
                WorkShiftPersistenceAssembler.toPersistenceFromDomain(workShift)
        );

        return WorkShiftPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }
}