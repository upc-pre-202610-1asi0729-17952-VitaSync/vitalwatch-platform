package com.vitalwatch.center.platform.audit.application.commandservices;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.AuditLog;
import com.vitalwatch.center.platform.audit.domain.model.aggregates.ComplianceRecord;
import com.vitalwatch.center.platform.audit.domain.model.commands.RecordAuditLogCommand;
import com.vitalwatch.center.platform.audit.domain.model.commands.RecordComplianceRecordCommand;
import com.vitalwatch.center.platform.audit.domain.model.commands.ReviewComplianceRecordCommand;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;

/**
 * Application service contract for audit and compliance commands.
 */
public interface AuditCommandService {

    Result<AuditLog, ApplicationError> handle(RecordAuditLogCommand command);

    Result<ComplianceRecord, ApplicationError> handle(RecordComplianceRecordCommand command);

    Result<ComplianceRecord, ApplicationError> handle(ReviewComplianceRecordCommand command);
}