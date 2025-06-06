package org.amalitechrichmond.projecttracker.service;

import org.amalitechrichmond.projecttracker.model.AuditLog;

import java.util.List;

public interface AuditLogService {

     <T> void saveLog(String actionType, String entityType, String entityId, T payload, String actorName);

     List<AuditLog> getAllLogs();
     List<AuditLog> getLogsByEntityType(String entityType);
     List<AuditLog> getLogsByActorName(String actorName);
}

