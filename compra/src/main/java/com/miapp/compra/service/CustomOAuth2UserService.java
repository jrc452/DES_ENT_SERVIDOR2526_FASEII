package com.miapp.compra.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miapp.compra.model.AuthProvider;
import com.miapp.compra.model.Role;
import com.miapp.compra.model.User;
import com.miapp.compra.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            log.error("Error procesando usuario OAuth2", ex);
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(
            OAuth2UserRequest userRequest,
            OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        UserInfo userInfo = extractUserInfo(registrationId, attributes);

        User user = userRepository
                .findByProviderAndProviderId(userInfo.provider, userInfo.providerId)
                .map(existingUser -> updateExistingUser(existingUser, userInfo))
                .orElseGet(() -> createNewUser(userInfo));

        return new CustomOAuth2User(oAuth2User, user);
    }

    private UserInfo extractUserInfo(
            String registrationId,
            Map<String, Object> attributes) {

        AuthProvider provider = AuthProvider.valueOf(
                registrationId.toUpperCase());

        return switch (provider) {
            case GOOGLE -> extractGoogleUserInfo(attributes);
            default -> throw new IllegalArgumentException(
                    "Proveedor no soportado: " + registrationId);
        };
    }

    private UserInfo extractGoogleUserInfo(Map<String, Object> attributes) {
        return UserInfo.builder()
                .provider(AuthProvider.GOOGLE)
                .providerId((String) attributes.get("sub"))
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("picture"))
                .emailVerified((Boolean) attributes.get("email_verified"))
                .build();
    }

    private User updateExistingUser(User user, UserInfo userInfo) {
        user.setName(userInfo.name);
        user.setImageUrl(userInfo.imageUrl);
        user.setEmailVerified(userInfo.emailVerified);
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    private User createNewUser(UserInfo userInfo) {
        User user = User.builder()
                .email(userInfo.email)
                .name(userInfo.name)
                .imageUrl(userInfo.imageUrl)
                .provider(userInfo.provider)
                .providerId(userInfo.providerId)
                .emailVerified(userInfo.emailVerified)
                .enabled(true)
                .roles(Set.of(Role.ROLE_USER)) // Rol por defecto
                .lastLogin(LocalDateTime.now())
                .build();

        log.info("Nuevo usuario creado: {}", user.getEmail());
        User usuarioGuardado = userRepository.save(user);
        return usuarioGuardado;
    }

    @lombok.Data
    @lombok.Builder
    private static class UserInfo {
        private AuthProvider provider;
        private String providerId;
        private String email;
        private String name;
        private String imageUrl;
        private Boolean emailVerified;
    }
}
