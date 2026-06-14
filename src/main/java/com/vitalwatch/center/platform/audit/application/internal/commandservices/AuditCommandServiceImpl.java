package com.vitalwatch.center.platform.audit.application.internal.commandservices;

import com.vitalwatch.center.platform.audit.application.commandservices.AuditCommandService;
import com.vitalwatch.center.platform.audit.domain.model.aggregates.AuditLog;
import com.vitalwatch.center.platform.audit.domain.model.aggregates.ComplianceRecord;
import com.vitalwatch.center.platform.audit.domain.model.commands.RecordAuditLogCommand;
import com.vitalwatch.center.platform.audit.domain.model.commands.RecordComplianceRecordCommand;
import com.vitalwatch.center.platform.audit.domain.model.commands.ReviewComplianceRecordCommand;
import com.vitalwatch.center.platform.audit.domain.repositories.AuditLogRepository;
import com.vitalwatch.center.platform.audit.domain.repositories.ComplianceRecordRepository;
import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Audit and compliance command service implementation.
 */
@Service
public class AuditCommandServiceImpl implements AuditCommandService {

    private final AuditLogRepository auditLogRepository;
    private final ComplianceRecordRepository complianceRecordRepository;

    public AuditCommandServiceImpl(
            AuditLogRepository auditLogRepository,
            ComplianceRecordRepository complianceRecordRepository
    ) {
        this.auditLogRepository = auditLogRepository;
        this.complianceRecordRepository = complianceRecordRepository;
    }

    @Override
    @Transactional
    public Result<AuditLog, ApplicationError> handle(RecordAuditLogCommand command) {
        try {
            var auditLog = new AuditLog(command);
            var savedAuditLog = auditLogRepository.save(auditLog);

            return Result.success(savedAuditLog);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("AuditLog", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Audit log recording", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ComplianceRecord, ApplicationError> handle(RecordComplianceRecordCommand command) {
        try {
            var complianceRecord = new ComplianceRecord(command);
            var savedComplianceRecord = complianceRecordRepository.save(complianceRecord);

            return Result.success(savedComplianceRecord);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("ComplianceRecord", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Compliance record creation", exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ComplianceRecord, ApplicationError> handle(ReviewComplianceRecordCommand command) {
        try {
            var complianceRecord = complianceRecordRepository.findById(command.complianceRecordId());

            if (complianceRecord.isEmpty()) {
                return Result.failure(ApplicationError.notFound(
                        "ComplianceRecord",
                        command.complianceRecordId().toString()
                ));
            }

            var recordToReview = complianceRecord.get();
            recordToReview.review(command);

            var savedComplianceRecord = complianceRecordRepository.save(recordToReview);

            return Result.success(savedComplianceRecord);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("ComplianceRecord", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return Result.failure(ApplicationError.businessRuleViolation("ComplianceRecord", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("Compliance record review", exception.getMessage()));
        }
    }
}