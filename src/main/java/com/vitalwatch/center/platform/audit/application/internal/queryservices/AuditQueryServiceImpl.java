package com.vitalwatch.center.platform.audit.application.internal.queryservices;

import com.vitalwatch.center.platform.audit.application.queryservices.AuditQueryService;
import com.vitalwatch.center.platform.audit.domain.model.aggregates.AuditLog;
import com.vitalwatch.center.platform.audit.domain.model.aggregates.ComplianceRecord;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetAuditLogByIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetAuditLogsByActorUserAccountIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetAuditLogsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetComplianceRecordByIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetComplianceRecordsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.audit.domain.repositories.AuditLogRepository;
import com.vitalwatch.center.platform.audit.domain.repositories.ComplianceRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Audit and compliance query service implementation.
 */
@Service
public class AuditQueryServiceImpl implements AuditQueryService {

    private final AuditLogRepository auditLogRepository;
    private final ComplianceRecordRepository complianceRecordRepository;

    public AuditQueryServiceImpl(
            AuditLogRepository auditLogRepository,
            ComplianceRecordRepository complianceRecordRepository
    ) {
        this.auditLogRepository = auditLogRepository;
        this.complianceRecordRepository = complianceRecordRepository;
    }

    @Override
    public Optional<AuditLog> handle(GetAuditLogByIdQuery query) {
        return auditLogRepository.findById(query.auditLogId());
    }

    @Override
    public List<AuditLog> handle(GetAuditLogsByHospitalWorkspaceIdQuery query) {
        return auditLogRepository.findAllByHospitalWorkspaceId(query.hospitalWorkspaceId());
    }

    @Override
    public List<AuditLog> handle(GetAuditLogsByActorUserAccountIdQuery query) {
        return auditLogRepository.findAllByActorUserAccountId(query.actorUserAccountId());
    }

    @Override
    public Optional<ComplianceRecord> handle(GetComplianceRecordByIdQuery query) {
        return complianceRecordRepository.findById(query.complianceRecordId());
    }

    @Override
    public List<ComplianceRecord> handle(GetComplianceRecordsByHospitalWorkspaceIdQuery query) {
        return complianceRecordRepository.findAllByHospitalWorkspaceId(query.hospitalWorkspaceId());
    }
}