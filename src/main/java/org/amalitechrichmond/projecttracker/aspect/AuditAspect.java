package org.amalitechrichmond.projecttracker.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amalitechrichmond.projecttracker.DTO.ProjectDTO;
import org.amalitechrichmond.projecttracker.DTO.TaskDTO;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogService auditLogService;

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.DeveloperServiceImpl.createDeveloper(..))")
    public void createDeveloperMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.DeveloperServiceImpl.updateDeveloper(..))")
    public void updateDeveloperMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.DeveloperServiceImpl.deleteDeveloper(..))")
    public void deleteDeveloperMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.ProjectServiceImpl.createProject(..))")
    public void createProjectMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.ProjectServiceImpl.updateProject(..))")
    public void updateProjectMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.ProjectServiceImpl.deleteProject(..))")
    public void deleteProjectMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.TaskServiceImpl.createTask(..))")
    public void createTaskMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.TaskServiceImpl.updateTask(..))")
    public void updateTaskMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.TaskServiceImpl.deleteTask(..))")
    public void deleteTaskMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.UserServiceImpl.createUser(..))")
    public void createUserMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.UserServiceImpl.updateUser(..))")
    public void updateUserMethod() {}

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.impl.UserServiceImpl.deleteUser(..))")
    public void deleteUserMethod() {}

    @AfterReturning(pointcut = "createDeveloperMethod()", returning = "result")
    public void afterCreateDeveloper(JoinPoint joinPoint, Object result) {
        if (result instanceof org.amalitechrichmond.projecttracker.DTO.DeveloperDTO dto) {
            auditLogService.saveLog("CREATE", "Developer", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "updateDeveloperMethod()", returning = "result")
    public void afterUpdateDeveloper(JoinPoint joinPoint, Object result) {
        if (result instanceof org.amalitechrichmond.projecttracker.DTO.DeveloperDTO dto) {
            auditLogService.saveLog("UPDATE", "Developer", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "deleteDeveloperMethod()", returning = "result")
    public void afterDeleteDeveloper(JoinPoint joinPoint, Object result) {
        if (result instanceof org.amalitechrichmond.projecttracker.DTO.DeveloperDTO dto) {
            auditLogService.saveLog("DELETE", "Developer", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "createProjectMethod()", returning = "result")
    public void afterCreateProject(JoinPoint joinPoint, Object result) {
        if (result instanceof ProjectDTO dto) {
            auditLogService.saveLog("CREATE", "Project", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "updateProjectMethod()", returning = "result")
    public void afterUpdateProject(JoinPoint joinPoint, Object result) {
        if (result instanceof ProjectDTO dto) {
            auditLogService.saveLog("UPDATE", "Project", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "deleteProjectMethod()", returning = "result")
    public void afterDeleteProject(JoinPoint joinPoint, Object result) {
        if (result instanceof ProjectDTO dto) {
            auditLogService.saveLog("DELETE", "Project", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "createTaskMethod()", returning = "result")
    public void afterCreateTask(JoinPoint joinPoint, Object result) {
        if (result instanceof TaskDTO dto) {
            auditLogService.saveLog("CREATE", "Task", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "updateTaskMethod()", returning = "result")
    public void afterUpdateTask(JoinPoint joinPoint, Object result) {
        if (result instanceof TaskDTO dto) {
            auditLogService.saveLog("UPDATE", "Task", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "deleteTaskMethod()", returning = "result")
    public void afterDeleteTask(JoinPoint joinPoint, Object result) {
        if (result instanceof TaskDTO dto) {
            auditLogService.saveLog("DELETE", "Task", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "createUserMethod()", returning = "result")
    public void afterCreateUser(JoinPoint joinPoint, Object result) {
        if (result instanceof UserDTO dto) {
            auditLogService.saveLog("CREATE", "User", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "updateUserMethod()", returning = "result")
    public void afterUpdateUser(JoinPoint joinPoint, Object result) {
        if (result instanceof UserDTO dto) {
            auditLogService.saveLog("UPDATE", "User", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    @AfterReturning(pointcut = "deleteUserMethod()", returning = "result")
    public void afterDeleteUser(JoinPoint joinPoint, Object result) {
        if (result instanceof UserDTO dto) {
            auditLogService.saveLog("DELETE", "User", String.valueOf(dto.getId()), dto, getActor());
        }
    }

    private String getActor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "system";
    }
}
