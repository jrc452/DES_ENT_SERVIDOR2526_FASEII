package com.miapp.compra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.miapp.compra.model.AuditLog;
import com.miapp.compra.model.User;
import com.miapp.compra.repository.AuditLogRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void logAction(
            User user,
            AuditLog.ActionType actionType,
            String action,
            String description) {

        HttpServletRequest request = getCurrentRequest();

        AuditLog auditLog = AuditLog.builder()
                .user(user)
                .actionType(actionType)
                .action(action)
                .description(description)
                .ipAddress(getClientIp(request))
                .userAgent(request != null ? request.getHeader("User-Agent") : null)
                .build();

        auditLogRepository.save(auditLog);

        log.info("Acción auditada: {} - {} - {}",
                user.getEmail(), actionType, action);
    }

    public Page<AuditLog> getUserAuditLogs(User user, Pageable pageable) {
        return auditLogRepository.findByUser(user, pageable);
    }

    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null)
            return "Unknown";

        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty())
            return xForwardedFor.split(",")[0];

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty())
            return xRealIp;

        return request.getRemoteAddr();
    }
}
