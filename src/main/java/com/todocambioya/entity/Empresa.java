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
import jakarta.persistence.Table;

@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false, length = 11)
    private String ruc;

    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_usuario_id")
    private Usuario adminUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    private Boolean activo = true;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }

    // ─── Constructores ───────────────────────────
    public Empresa() {}

    public Empresa(UUID id, String ruc, String razonSocial, Usuario adminUsuario,
                   Region region, Boolean activo, LocalDateTime creadoEn) {
        this.id = id;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.adminUsuario = adminUsuario;
        this.region = region;
        this.activo = activo;
        this.creadoEn = creadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public String getRuc() { return ruc; }
    public String getRazonSocial() { return razonSocial; }
    public Usuario getAdminUsuario() { return adminUsuario; }
    public Region getRegion() { return region; }
    public Boolean getActivo() { return activo; }
    public LocalDateTime getCreadoEn() { return creadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public void setAdminUsuario(Usuario adminUsuario) { this.adminUsuario = adminUsuario; }
    public void setRegion(Region region) { this.region = region; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    @Override
    public String toString() {
        return "Empresa{id=" + id + ", ruc='" + ruc + "', razonSocial='" + razonSocial + "'}";
    }
}
