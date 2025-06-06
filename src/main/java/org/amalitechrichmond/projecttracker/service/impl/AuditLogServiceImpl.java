package org.amalitechrichmond.projecttracker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.model.AuditLog;
import org.amalitechrichmond.projecttracker.repository.AuditLogRepository;
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @CacheEvict(value = {"allAuditLogs", "auditLogsByEntityType", "auditLogsByActorName"}, allEntries = true)
    public <T> void saveLog(String actionType, String entityType, String entityId, T payload, String actorName) {
        AuditLog log = new AuditLog();
        log.setActionType(actionType);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setActorName(actorName);
        log.setTimestamp(LocalDateTime.now());

        try {
            String json = objectMapper.writeValueAsString(payload);
            log.setPayload(json);
        } catch (JsonProcessingException e) {
            log.setPayload("Error serializing payload: " + e.getMessage());
        }
        auditLogRepository.save(log);
    }

    // Get all audit logs
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "allAuditLogs")
    public List<AuditLog> getAllLogs() {
        List<AuditLog> logs = auditLogRepository.findAll();
        System.out.println(logs);
        return logs;
    }

    // Get logs filtered by entity type
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "auditLogsByEntityType", key = "#entityType")
    public  List<AuditLog> getLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType);
    }

    // Get logs filtered by actor name
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "auditLogsByActorName", key = "#actorName")
    public  List<AuditLog> getLogsByActorName(String actorName) {
        return auditLogRepository.findByActorName(actorName);
    }
}

