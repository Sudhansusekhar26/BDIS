package com.bdis.service.impl;

import com.bdis.entity.AuditLog;
import com.bdis.repository.AuditLogRepository;
import com.bdis.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public void log(String action, String performedBy, String entityName, String entityId, String details) {
        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .performedBy(performedBy)
                .entityName(entityName)
                .entityId(entityId)
                .details(details)
                .build();
        auditLogRepository.save(auditLog);
        log.info("[AUDIT] Action: {}, PerformedBy: {}, Target: {}:{}", action, performedBy, entityName, entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentLogs() {
        return auditLogRepository.findTop50ByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc();
    }
}
