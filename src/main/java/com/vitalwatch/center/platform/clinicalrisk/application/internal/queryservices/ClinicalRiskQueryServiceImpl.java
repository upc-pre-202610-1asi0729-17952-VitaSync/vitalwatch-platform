package com.vitalwatch.center.platform.clinicalrisk.application.internal.queryservices;

import com.vitalwatch.center.platform.clinicalrisk.application.queryservices.ClinicalRiskQueryService;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.VitalSignReading;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetClinicalRiskAssessmentByIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetClinicalRiskAssessmentsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetLatestClinicalRiskAssessmentByUserAccountIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.queries.GetVitalSignReadingsByUserAccountIdQuery;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.ClinicalRiskAssessmentRepository;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.VitalSignReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Clinical risk query service implementation.
 */
@Service
public class ClinicalRiskQueryServiceImpl implements ClinicalRiskQueryService {

    private final VitalSignReadingRepository vitalSignReadingRepository;
    private final ClinicalRiskAssessmentRepository clinicalRiskAssessmentRepository;

    public ClinicalRiskQueryServiceImpl(
            VitalSignReadingRepository vitalSignReadingRepository,
            ClinicalRiskAssessmentRepository clinicalRiskAssessmentRepository
    ) {
        this.vitalSignReadingRepository = vitalSignReadingRepository;
        this.clinicalRiskAssessmentRepository = clinicalRiskAssessmentRepository;
    }

    @Override
    public Optional<ClinicalRiskAssessment> handle(GetClinicalRiskAssessmentByIdQuery query) {
        return clinicalRiskAssessmentRepository.findById(query.clinicalRiskAssessmentId());
    }

    @Override
    public Optional<ClinicalRiskAssessment> handle(GetLatestClinicalRiskAssessmentByUserAccountIdQuery query) {
        return clinicalRiskAssessmentRepository.findLatestByUserAccountId(query.userAccountId());
    }

    @Override
    public List<ClinicalRiskAssessment> handle(GetClinicalRiskAssessmentsByHospitalWorkspaceIdQuery query) {
        return clinicalRiskAssessmentRepository.findAllByHospitalWorkspaceId(query.hospitalWorkspaceId());
    }

    @Override
    public List<VitalSignReading> handle(GetVitalSignReadingsByUserAccountIdQuery query) {
        return vitalSignReadingRepository.findAllByUserAccountId(query.userAccountId());
    }
}