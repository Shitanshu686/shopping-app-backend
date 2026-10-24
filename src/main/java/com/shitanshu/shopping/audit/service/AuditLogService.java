package com.shitanshu.shopping.audit.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.audit.model.AuditLog;
import com.shitanshu.shopping.audit.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Async
    public void recordLogAsync(String entityName, String entityId, String action, String detailsJson, Long userId) {
        try {
            AuditLog entry = AuditLog.builder()
                    .entityName(entityName)
                    .entityId(entityId)
                    .action(action)
                    .detailsJson(detailsJson)
                    .performedByUserId(userId)
                    .build();
            auditLogRepository.save(entry);
            log.info("[AUDIT-TRAIL] Recorded {} action on {}:{}", action, entityName, entityId);
        } catch (Exception e) {
            log.error("[AUDIT-TRAIL] Failed to record audit log for {}:{}", entityName, entityId, e);
        }
    }

    public List<AuditLog> getLogsForEntity(String entityName, String entityId) {
        return auditLogRepository.findByEntityNameAndEntityIdOrderByTimestampDesc(entityName, entityId);
    }
}
