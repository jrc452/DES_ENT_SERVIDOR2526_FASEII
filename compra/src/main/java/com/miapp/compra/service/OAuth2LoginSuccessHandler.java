package com.miapp.compra.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.miapp.compra.model.User;
import com.miapp.compra.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        log.info("=== INICIO DE LOGIN EXITOSO ===");

        Object principal = authentication.getPrincipal();
        log.info("Tipo de principal: {}", principal.getClass().getName());

        User user = null;

        if (principal instanceof CustomOAuth2User customOAuth2User) {
            user = customOAuth2User.getUser();
            log.info("Usuario CustomOAuth2User - Email: {}", user.getEmail());
        } else if (principal instanceof OAuth2User oauth2User) {
            log.warn("OAuth2User genérico detectado (no CustomOAuth2User)");
            String email = extractEmailFromOAuth2User(oauth2User);

            if (email != null) {
                user = userRepository.findByEmail(email).orElse(null);
                log.info("Usuario encontrado en BD por email: {}", email);
            }
        }

        if (user == null) {
            log.error("❌ ERROR: No se pudo obtener el usuario de la autenticación");
            response.sendRedirect("/login?error=user_not_found");
            return;
        }

        log.info("✅ Usuario autenticado correctamente:");
        log.info("  - ID: {}", user.getId());
        log.info("  - Email: {}", user.getEmail());
        log.info("  - Nombre: {}", user.getName());
        log.info("  - Provider: {}", user.getProvider());
        log.info("  - Roles: {}", user.getRoles());

        String targetUrl;
        if (user.isAdmin()) {
            targetUrl = "/admin/dashboard";
            log.info("Redirigiendo a dashboard de ADMIN");
        } else {
            targetUrl = "/user/profile";
            log.info("Redirigiendo a perfil de USER");
        }

        setDefaultTargetUrl(targetUrl);
        log.info("=== FIN DE LOGIN EXITOSO ===");

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private String extractEmailFromOAuth2User(OAuth2User oauth2User) {

        String email = oauth2User.getAttribute("email");

        if (email == null || email.isEmpty()) {
            String login = oauth2User.getAttribute("login");
            Object idObj = oauth2User.getAttribute("id");

            if (login != null && idObj != null) {
                String id = String.valueOf(idObj);

                // Formato consistente con extractGitHubUserInfo
                email = login + "@github-id-" + id + ".com";
                log.info("Email generado para GitHub: {}", email);
            }
        }

        return email;
    }
}
