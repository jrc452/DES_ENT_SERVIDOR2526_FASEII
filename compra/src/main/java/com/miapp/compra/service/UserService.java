package com.miapp.compra.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miapp.compra.model.AuditLog;
import com.miapp.compra.model.Role;
import com.miapp.compra.model.User;
import com.miapp.compra.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AuditService auditService;

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("No hay autenticación válida");
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2User customUser)
            return Optional.of(customUser.getUser());

        if (principal instanceof OAuth2User oauth2User) {
            String email = extractEmailFromOAuth2User(oauth2User);
            if (email != null)
                return userRepository.findByEmail(email);
        }

        if (principal instanceof String username)
            return userRepository.findByEmail(username);

        return Optional.empty();
    }

    private String extractEmailFromOAuth2User(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");

        if (email == null || email.isEmpty()) {
            String login = oauth2User.getAttribute("login");
            Object idObj = oauth2User.getAttribute("id");

            if (login != null && idObj != null) {
                String id = String.valueOf(idObj);
                email = login + "@github-id-" + id + ".com";
            }
        }
        return email;
    }

    public User getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("Usuario no autenticado"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUserRoles(Long userId, Role role) {
        User currentUser = getCurrentUserOrThrow();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.getRoles().clear();
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);

        auditService.logAction(currentUser, AuditLog.ActionType.UPDATE, "ROLE_UPDATE",
                String.format("Rol actualizado para %s a %s", user.getEmail(), role.name()));

        return savedUser;
    }

    @Transactional
    public void deleteUser(Long userId) {
        User currentUser = getCurrentUserOrThrow();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        userRepository.delete(user);

        auditService.logAction(currentUser, AuditLog.ActionType.DELETE, "USER_DELETE",
                "Usuario eliminado: " + user.getEmail());
    }

    @Transactional
    public User registrarUsuario(User user) {
        User usuarioGuardado = userRepository.save(user);
        return usuarioGuardado;
    }

    @Transactional
    public boolean confirmarUsuario(String email) {
        Optional<User> usuarioOpt = userRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            User user = usuarioOpt.get();
            user.setEmailVerified(true);
            userRepository.save(user);
            log.info("Usuario confirmado: {}", email);
            return true;
        }
        return false;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public static void cambiarRol(Long id, String role) {
        throw new UnsupportedOperationException("Unimplemented method 'cambiarRol'");
    }

    public void actualizarRol(Long id, String role) {
        throw new UnsupportedOperationException("Unimplemented method 'actualizarRol'");
    }

    public Page<User> getAllUsersPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}