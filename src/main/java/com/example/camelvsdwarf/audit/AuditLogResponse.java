package com.example.camelvsdwarf.audit;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id, Long userId, String action, String entityType, String entityId,
        LocalDateTime createdAt, String description, String previousValues, String newValues
) {
}