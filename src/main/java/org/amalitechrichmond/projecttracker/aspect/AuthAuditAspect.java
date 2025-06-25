package org.amalitechrichmond.projecttracker.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amalitechrichmond.projecttracker.DTO.AuthLoginRequestDTO;
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthAuditAspect {

    private final AuditLogService auditLogService;

    @Pointcut("execution(* org.amalitechrichmond.projecttracker.service.AuthService.login(..))")
    public void loginPointcut() {}

    @AfterReturning(pointcut = "loginPointcut()", returning = "result")
    public void afterSuccessfulLogin(JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof AuthLoginRequestDTO request) {
            String email = request.getEmail();
            auditLogService.saveLog("LOGIN_SUCCESS", "User", email, null, email);
        }
    }

    @AfterThrowing(pointcut = "loginPointcut()", throwing = "ex")
    public void afterFailedLogin(JoinPoint joinPoint, Throwable ex) {
        if (ex instanceof BadCredentialsException && joinPoint.getArgs()[0] instanceof AuthLoginRequestDTO request) {
            String email = request.getEmail();
            auditLogService.saveLog("LOGIN_FAILURE", "User", email, null, email);
        }
    }
}
