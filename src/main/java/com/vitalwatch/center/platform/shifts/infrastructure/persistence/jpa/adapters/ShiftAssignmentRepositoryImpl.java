package com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.shifts.domain.model.aggregates.ShiftAssignment;
import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftAssignmentStatus;
import com.vitalwatch.center.platform.shifts.domain.repositories.ShiftAssignmentRepository;
import com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.assemblers.ShiftAssignmentPersistenceAssembler;
import com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.repositories.ShiftAssignmentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for ShiftAssignment.
 */
@Repository
public class ShiftAssignmentRepositoryImpl implements ShiftAssignmentRepository {

    private final ShiftAssignmentPersistenceRepository repository;

    public ShiftAssignmentRepositoryImpl(ShiftAssignmentPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ShiftAssignment> findById(Long id) {
        return repository.findById(id)
                .map(ShiftAssignmentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<ShiftAssignment> findAllByWorkShiftId(Long workShiftId) {
        return repository.findAllByWorkShiftIdOrderByAssignedAtDesc(workShiftId)
                .stream()
                .map(ShiftAssignmentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<ShiftAssignment> findAllByUserAccountId(Long userAccountId) {
        return repository.findAllByUserAccountIdOrderByAssignedAtDesc(userAccountId)
                .stream()
                .map(ShiftAssignmentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsActiveAssignmentByUserAccountId(Long userAccountId) {
        return repository.existsByUserAccountIdAndStatusIn(
                userAccountId,
                List.of(ShiftAssignmentStatus.ASSIGNED, ShiftAssignmentStatus.CONFIRMED)
        );
    }

    @Override
    public ShiftAssignment save(ShiftAssignment shiftAssignment) {
        var savedEntity = repository.save(
                ShiftAssignmentPersistenceAssembler.toPersistenceFromDomain(shiftAssignment)
        );

        return ShiftAssignmentPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }
}