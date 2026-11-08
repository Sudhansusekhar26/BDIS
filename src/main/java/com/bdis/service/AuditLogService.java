package com.bdis.service;

import com.bdis.entity.AuditLog;

import java.util.List;

public interface AuditLogService {

    void log(String action, String performedBy, String entityName, String entityId, String details);

    List<AuditLog> getRecentLogs();

    List<AuditLog> getAllLogs();
}
