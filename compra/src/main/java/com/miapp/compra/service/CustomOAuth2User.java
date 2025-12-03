package com.miapp.compra.service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.miapp.compra.model.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final User user;

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    public String getDisplayName() {
        return user.getName() != null ? user.getName() : user.getEmail();
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name) {
        return (T) getAttributes().get(name);
    }
}