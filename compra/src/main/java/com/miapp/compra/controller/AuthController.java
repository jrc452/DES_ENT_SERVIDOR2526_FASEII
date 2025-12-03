package com.miapp.compra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miapp.compra.model.User;
import com.miapp.compra.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registrarUsuario(@RequestBody User user) {
        try {
            userService.registrarUsuario(user);

            return ResponseEntity.ok("""
                    {
                        "message": "Usuario registrado exitosamente",
                        "email": "%s"
                    }
                    """.formatted(user.getEmail()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("""
                    {
                        "error": "Error registrando usuario",
                        "details": "%s"
                    }
                    """.formatted(e.getMessage()));
        }
    }

    @PostMapping("/register-user")
    public ResponseEntity<String> registrarUser(@RequestBody User user) {
        try {
            User usuarioRegistrado = userService.registrarUsuario(user);

            return ResponseEntity.ok("""
                    {
                        "message": "Usuario registrado exitosamente",
                        "email": "%s",
                        "id": "%d"
                    }
                    """.formatted(user.getEmail(), usuarioRegistrado.getId()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
