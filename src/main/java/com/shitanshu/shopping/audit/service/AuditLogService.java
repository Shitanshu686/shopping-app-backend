package com.shitanshu.shopping.audit.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shitanshu.shopping.audit.model.AuditLog;
import com.shitanshu.shopping.audit.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(transactionManager = "postgresTransactionManager")
    public void recordLog(String entityName, String entityId, String action, String detailsJson, Long userId) {
        AuditLog entry = AuditLog.builder()
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .detailsJson(detailsJson)
                .performedByUserId(userId)
                .build();

        auditLogRepository.save(entry);
        log.info("[AUDIT-TRAIL] Recorded {} action on {}:{}", action, entityName, entityId);
    }

    @Transactional(transactionManager = "postgresTransactionManager", readOnly = true)
    public List<AuditLog> getLogsForEntity(String entityName, String entityId) {
        return auditLogRepository.findByEntityNameAndEntityIdOrderByTimestampDesc(entityName, entityId);
    }
}
