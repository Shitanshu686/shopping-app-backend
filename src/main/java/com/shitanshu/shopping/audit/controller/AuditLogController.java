package com.shitanshu.shopping.audit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shitanshu.shopping.audit.model.AuditLog;
import com.shitanshu.shopping.audit.service.AuditLogService;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/{entityName}/{entityId}")
    public ResponseEntity<List<AuditLog>> getAuditHistory(
            @PathVariable String entityName,
            @PathVariable String entityId) {
        List<AuditLog> logs = auditLogService.getLogsForEntity(entityName.toUpperCase(), entityId);
        return ResponseEntity.ok(logs);
    }
}
