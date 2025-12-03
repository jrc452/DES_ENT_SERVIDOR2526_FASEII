package com.miapp.compra.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miapp.compra.model.User;
import com.miapp.compra.service.AuditService;
import com.miapp.compra.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AuditService auditService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "0") int page, Model model) { // 1. Recibir la página
        model.addAttribute("user", userService.getCurrentUserOrThrow());
        Page<User> usersPage = userService.getAllUsersPaginated(PageRequest.of(page, 10));
        model.addAttribute("users", usersPage);
        return "admin/dashboard";
    }

    @GetMapping("/audit-logs")
    public String auditLogs(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("auditLogs", auditService.getAllAuditLogs(PageRequest.of(page, 20)));
        return "admin/audit-logs";
    }
}