package com.miapp.compra.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miapp.compra.model.AuthProvider;
import com.miapp.compra.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    List<User> findAllByNameContaining(String name);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);
}
