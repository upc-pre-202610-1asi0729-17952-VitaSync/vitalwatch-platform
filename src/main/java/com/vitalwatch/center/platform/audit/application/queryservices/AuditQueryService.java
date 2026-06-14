package com.vitalwatch.center.platform.audit.application.queryservices;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.AuditLog;
import com.vitalwatch.center.platform.audit.domain.model.aggregates.ComplianceRecord;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetAuditLogByIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetAuditLogsByActorUserAccountIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetAuditLogsByHospitalWorkspaceIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetComplianceRecordByIdQuery;
import com.vitalwatch.center.platform.audit.domain.model.queries.GetComplianceRecordsByHospitalWorkspaceIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for audit and compliance queries.
 */
public interface AuditQueryService {

    Optional<AuditLog> handle(GetAuditLogByIdQuery query);

    List<AuditLog> handle(GetAuditLogsByHospitalWorkspaceIdQuery query);

    List<AuditLog> handle(GetAuditLogsByActorUserAccountIdQuery query);

    Optional<ComplianceRecord> handle(GetComplianceRecordByIdQuery query);

    List<ComplianceRecord> handle(GetComplianceRecordsByHospitalWorkspaceIdQuery query);
}