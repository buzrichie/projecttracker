package org.amalitechrichmond.projecttracker.repository;

import org.amalitechrichmond.projecttracker.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findByEntityType(String entityType);
    List<AuditLog> findByActorName(String actorName);

}
