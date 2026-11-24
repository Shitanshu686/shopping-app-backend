package com.shitanshu.shopping.audit.service;

import com.shitanshu.shopping.audit.model.AuditLog;
import com.shitanshu.shopping.audit.repository.AuditLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    @Test
    void recordLog_SavesAuditEntryWithExpectedValues() {
        auditLogService.recordLog(
                "ORDER", "101", "PLACED", "{\"status\":\"PLACED\"}", 5L
        );

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());

        AuditLog savedLog = captor.getValue();

        assertEquals("ORDER", savedLog.getEntityName());
        assertEquals("101", savedLog.getEntityId());
        assertEquals("PLACED", savedLog.getAction());
        assertEquals("{\"status\":\"PLACED\"}", savedLog.getDetailsJson());
        assertEquals(5L, savedLog.getPerformedByUserId());
    }

    @Test
    void recordLog_WhenRepositoryFails_PropagatesException() {
        RuntimeException databaseError = new RuntimeException("Database unavailable");
        doThrow(databaseError).when(auditLogRepository).save(any(AuditLog.class));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> auditLogService.recordLog(
                        "ORDER", "101", "PLACED", "{}", 5L
                )
        );

        assertSame(databaseError, thrown);
        verify(auditLogRepository).save(any(AuditLog.class));
    }
}
