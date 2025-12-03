package com.miapp.compra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.miapp.compra.model.AuditLog;
import com.miapp.compra.model.User;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByUser(User user, Pageable pageable);

    Page<AuditLog> findByActionType(
            AuditLog.ActionType actionType,
            Pageable pageable);
}
