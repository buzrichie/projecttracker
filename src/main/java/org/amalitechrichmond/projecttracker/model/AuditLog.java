package org.amalitechrichmond.projecttracker.model;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "audit_logs")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    private String id;

    private String actionType;   // CREATE, UPDATE, DELETE
    private String entityType;   // "Project", "Task", etc.
    private String entityId;
    private LocalDateTime timestamp;
    private String actorName;

    @Lob
    private String payload; // JSON of the changed entity
}

