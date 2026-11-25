package com.shitanshu.shopping.audit.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shitanshu.shopping.audit.service.AuditLogService;
import com.shitanshu.shopping.event.OrderPlacedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditEventListenerTest {

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private AuditEventListener auditEventListener;

    @Test
    void handleOrderPlacedAudit_WhenSaveSucceeds_AcknowledgesMessage() throws Exception {
        OrderPlacedEvent event = new OrderPlacedEvent(
                9999, 101, "testuser@shopease.com", 4999.0, "PLACED"
        );

        when(objectMapper.writeValueAsString(event)).thenReturn("{}");

        auditEventListener.handleOrderPlacedAudit(event, acknowledgment);

        verify(auditLogService).recordLog(
                "ORDER", "9999", "PLACED", "{}", 101L
        );
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void handleOrderPlacedAudit_WhenSaveFails_DoesNotAcknowledgeAndPropagatesException() throws Exception {
        OrderPlacedEvent event = new OrderPlacedEvent(
                9999, 101, "testuser@shopease.com", 4999.0, "PLACED"
        );

        when(objectMapper.writeValueAsString(event)).thenReturn("{}");

        RuntimeException databaseError = new RuntimeException("Database unavailable");

        doThrow(databaseError).when(auditLogService).recordLog(
                "ORDER", "9999", "PLACED", "{}", 101L
        );

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> auditEventListener.handleOrderPlacedAudit(event, acknowledgment)
        );

        assertSame(databaseError, thrown.getCause());
        verify(acknowledgment, never()).acknowledge();
    }
}
