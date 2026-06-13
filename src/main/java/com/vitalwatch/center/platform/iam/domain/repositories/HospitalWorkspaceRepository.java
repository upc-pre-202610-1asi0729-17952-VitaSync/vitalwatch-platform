package com.vitalwatch.center.platform.iam.domain.repositories;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.Ruc;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for hospital workspaces.
 */
public interface HospitalWorkspaceRepository {

    Optional<HospitalWorkspace> findById(Long id);

    Optional<HospitalWorkspace> findByRuc(Ruc ruc);

    List<HospitalWorkspace> findAll();

    HospitalWorkspace save(HospitalWorkspace hospitalWorkspace);

    boolean existsByRuc(Ruc ruc);
}