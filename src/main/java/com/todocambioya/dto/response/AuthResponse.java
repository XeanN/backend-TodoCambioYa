package com.todocambioya.dto.response;

import java.util.UUID;

public class AuthResponse {

    private String token;
    private String tipo;
    private UUID usuarioId;
    private String email;
    private String nombreCompleto;
    private String tipoCuenta;

    // ─── Constructores ───────────────────────────
    public AuthResponse() {}

    public AuthResponse(String token, String tipo, UUID usuarioId,
                        String email, String nombreCompleto, String tipoCuenta) {
        this.token = token;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.tipoCuenta = tipoCuenta;
    }

    // ─── Getters ─────────────────────────────────
    public String getToken() { return token; }
    public String getTipo() { return tipo; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getEmail() { return email; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTipoCuenta() { return tipoCuenta; }

    // ─── Setters ─────────────────────────────────
    public void setToken(String token) { this.token = token; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
    public void setEmail(String email) { this.email = email; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
}
