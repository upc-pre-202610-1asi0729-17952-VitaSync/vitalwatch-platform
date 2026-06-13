package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.Ruc;
import com.vitalwatch.center.platform.iam.domain.repositories.HospitalWorkspaceRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.assemblers.HospitalWorkspacePersistenceAssembler;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.HospitalWorkspacePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for HospitalWorkspace.
 */
@Repository
public class HospitalWorkspaceRepositoryImpl implements HospitalWorkspaceRepository {

    private final HospitalWorkspacePersistenceRepository repository;

    public HospitalWorkspaceRepositoryImpl(HospitalWorkspacePersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<HospitalWorkspace> findById(Long id) {
        return repository.findById(id)
                .map(HospitalWorkspacePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<HospitalWorkspace> findByRuc(Ruc ruc) {
        return repository.findByRuc(ruc.value())
                .map(HospitalWorkspacePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<HospitalWorkspace> findAll() {
        return repository.findAll()
                .stream()
                .map(HospitalWorkspacePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public HospitalWorkspace save(HospitalWorkspace hospitalWorkspace) {
        var savedEntity = repository.save(
                HospitalWorkspacePersistenceAssembler.toPersistenceFromDomain(hospitalWorkspace)
        );
        return HospitalWorkspacePersistenceAssembler.toDomainFromPersistence(savedEntity);
    }

    @Override
    public boolean existsByRuc(Ruc ruc) {
        return repository.countByRuc(ruc.value()) > 0;
    }
}