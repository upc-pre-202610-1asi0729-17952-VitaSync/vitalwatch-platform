package com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.VitalSignReadingRepository;
import com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.assemblers.VitalSignReadingPersistenceAssembler;
import com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.repositories.VitalSignReadingPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for VitalSignReading.
 */
@Repository
public class VitalSignReadingRepositoryImpl implements VitalSignReadingRepository {

    private final VitalSignReadingPersistenceRepository repository;

    public VitalSignReadingRepositoryImpl(VitalSignReadingPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<VitalSignReading> findById(Long id) {
        return repository.findById(id)
                .map(VitalSignReadingPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<VitalSignReading> findAllByUserAccountId(Long userAccountId) {
        return repository.findAllByUserAccountIdOrderByRecordedAtDesc(userAccountId)
                .stream()
                .map(VitalSignReadingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<VitalSignReading> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findAllByHospitalWorkspaceIdOrderByRecordedAtDesc(hospitalWorkspaceId)
                .stream()
                .map(VitalSignReadingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public VitalSignReading save(VitalSignReading vitalSignReading) {
        var savedEntity = repository.save(
                VitalSignReadingPersistenceAssembler.toPersistenceFromDomain(vitalSignReading)
        );
        return VitalSignReadingPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }
}