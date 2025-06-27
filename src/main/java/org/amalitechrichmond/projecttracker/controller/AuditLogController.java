package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.ResponseHelper;
import org.amalitechrichmond.projecttracker.model.AuditLog;
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @Operation(summary = "Get all logs")
    public ResponseEntity<?> getAllLogs() {
        List<AuditLog> logs = auditLogService.getAllLogs();
        return ResponseHelper.success("All audit logs retrieved", logs);
    }

    @GetMapping("/by-entity/{entity}")
    @Operation(summary = "Get logs by entity type")
    public ResponseEntity<?> getLogsByEntityType(@PathVariable String entity) {
        List<AuditLog> logs = auditLogService.getLogsByEntityType(entity);
        if (logs.isEmpty()) {
            return ResponseHelper.success("No logs found for entity: " + entity, logs);
        }
        return ResponseHelper.success("Audit logs for entity retrieved", logs);
    }

    @GetMapping("/by-actor/{actor}")
    @Operation(summary = "Get logs by actor name")
    public ResponseEntity<?> getLogsByActor(@PathVariable String actor) {
        List<AuditLog> logs = auditLogService.getLogsByActorName(actor);
        if (logs.isEmpty()) {
            return ResponseHelper.success("No logs found for actor: " + actor, logs);
        }
        return ResponseHelper.success("Audit logs for actor retrieved", logs);
    }
}
