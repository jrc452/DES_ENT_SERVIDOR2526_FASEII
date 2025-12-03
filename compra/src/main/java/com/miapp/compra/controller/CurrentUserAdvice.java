package com.miapp.compra.controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.miapp.compra.model.User;
import com.miapp.compra.service.UserService;

@ControllerAdvice
public class CurrentUserAdvice {

    private final UserService userService;

    public CurrentUserAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User addCurrentUserToModel(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated())
            return null;

        Optional<User> currentUserOpt = userService.getCurrentUser();
        return currentUserOpt.orElse(null);
    }
}