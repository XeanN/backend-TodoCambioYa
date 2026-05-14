package com.todocambioya.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "tipo_cuenta", length = 10)
    private String tipoCuenta;

    @Column(name = "dni_ruc", length = 20)
    private String dniRuc;

    @Column(length = 15)
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    private Boolean activo = true;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    // ─── Hooks ───────────────────────────────────
    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
        actualizadoEn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        actualizadoEn = LocalDateTime.now();
    }

    // ─── Constructores ───────────────────────────
    public Usuario() {}

    public Usuario(UUID id, String nombreCompleto, String email, String passwordHash,
                   String tipoCuenta, String dniRuc, String telefono, Region region,
                   Boolean activo, LocalDateTime creadoEn, LocalDateTime actualizadoEn) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.passwordHash = passwordHash;
        this.tipoCuenta = tipoCuenta;
        this.dniRuc = dniRuc;
        this.telefono = telefono;
        this.region = region;
        this.activo = activo;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getTipoCuenta() { return tipoCuenta; }
    public String getDniRuc() { return dniRuc; }
    public String getTelefono() { return telefono; }
    public Region getRegion() { return region; }
    public Boolean getActivo() { return activo; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
    public LocalDateTime getActualizadoEn() { return actualizadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public void setDniRuc(String dniRuc) { this.dniRuc = dniRuc; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setRegion(Region region) { this.region = region; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }

    // ─── toString ────────────────────────────────
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", email='" + email + "', nombreCompleto='" + nombreCompleto + "'}";
    }
}
