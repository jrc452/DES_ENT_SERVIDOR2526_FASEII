package com.miapp.compra.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miapp.compra.model.AuditLog;
import com.miapp.compra.model.User;
import com.miapp.compra.service.AuditService;
import com.miapp.compra.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuditService auditService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/profile")
    public String profile(Model model) {
        User currentUser = userService.getCurrentUserOrThrow();

        auditService.logAction(
                currentUser,
                AuditLog.ActionType.READ,
                "VIEW_PROFILE",
                "Usuario visualizó su perfil");

        model.addAttribute("user", currentUser);
        return "user/profile";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/my-activity")
    public String myActivity(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        User currentUser = userService.getCurrentUserOrThrow();

        Page<AuditLog> auditLogs = auditService.getUserAuditLogs(
                currentUser,
                PageRequest.of(page, 10));

        model.addAttribute("auditLogs", auditLogs);
        model.addAttribute("user", currentUser);

        return "user/activity";
    }
}