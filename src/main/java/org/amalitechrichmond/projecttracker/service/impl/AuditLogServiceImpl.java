package org.amalitechrichmond.projecttracker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.model.AuditLog;
import org.amalitechrichmond.projecttracker.repository.AuditLogRepository;
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Override
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
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    // Get logs filtered by entity type
    @Override
    public  List<AuditLog> getLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType);
    }

    // Get logs filtered by actor name
    @Override
    public  List<AuditLog> getLogsByActorName(String actorName) {
        return auditLogRepository.findByActorName(actorName);
    }
}

