package com.todocambioya.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class UsuarioResponse {

    private UUID id;
    private String nombreCompleto;
    private String email;
    private String tipoCuenta;
    private String dniRuc;
    private String telefono;
    private String region;
    private Boolean activo;
    private LocalDateTime creadoEn;

    // ─── Constructores ───────────────────────────
    public UsuarioResponse() {}

    public UsuarioResponse(UUID id, String nombreCompleto, String email, String tipoCuenta,
                           String dniRuc, String telefono, String region,
                           Boolean activo, LocalDateTime creadoEn) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.tipoCuenta = tipoCuenta;
        this.dniRuc = dniRuc;
        this.telefono = telefono;
        this.region = region;
        this.activo = activo;
        this.creadoEn = creadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return email; }
    public String getTipoCuenta() { return tipoCuenta; }
    public String getDniRuc() { return dniRuc; }
    public String getTelefono() { return telefono; }
    public String getRegion() { return region; }
    public Boolean getActivo() { return activo; }
    public LocalDateTime getCreadoEn() { return creadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setEmail(String email) { this.email = email; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public void setDniRuc(String dniRuc) { this.dniRuc = dniRuc; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setRegion(String region) { this.region = region; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
