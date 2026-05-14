package com.todocambioya.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // ─── Constructores ───────────────────────────
    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // ─── Getters ─────────────────────────────────
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    // ─── Setters ─────────────────────────────────
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
