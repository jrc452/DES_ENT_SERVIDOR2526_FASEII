package com.miapp.compra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.miapp.compra.service.CustomOAuth2UserService;
import com.miapp.compra.service.OAuth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/login",
                        "/error",
                        "/public/**",
                        "/css/**",
                        "/js/**",
                        "/images/**")
                .permitAll()

                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/manage/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/libros/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/libros/editar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/libros/**").hasRole("ADMIN")
                .requestMatchers("/usuario/**").hasAnyRole("ADMIN")
                .requestMatchers("/libros/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/categorias/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/autores/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureUrl("/login?error=true"))

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))

                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}