package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.model.AuditLog;
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @Operation(summary = "Get all logs")
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        List<AuditLog> logs = auditLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/by-entity/{entity}")
    @Operation(summary = "Get a log by entity")
    public ResponseEntity<List<AuditLog>> getLogsByEntityType(@PathVariable String entity) {
        List<AuditLog> logs = auditLogService.getLogsByEntityType(entity);
        if (logs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/by-actor/{actor}")
    @Operation(summary = "Get a log by actor name")
    public ResponseEntity<List<AuditLog>> getLogsByActor(@PathVariable String actor) {
        List<AuditLog> logs = auditLogService.getLogsByActorName(actor);
        if (logs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(logs);
    }
}
