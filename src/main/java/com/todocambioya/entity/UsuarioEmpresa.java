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
@Table(name = "usuarios_empresa")
public class UsuarioEmpresa {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 10)
    private String rol;

    @Column(name = "puede_operar")
    private Boolean puedeOperar = false;

    @Column(name = "puede_ver_historial")
    private Boolean puedeVerHistorial = false;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }

    // ─── Constructores ───────────────────────────
    public UsuarioEmpresa() {}

    public UsuarioEmpresa(UUID id, Empresa empresa, Usuario usuario, String rol,
                          Boolean puedeOperar, Boolean puedeVerHistorial, LocalDateTime creadoEn) {
        this.id = id;
        this.empresa = empresa;
        this.usuario = usuario;
        this.rol = rol;
        this.puedeOperar = puedeOperar;
        this.puedeVerHistorial = puedeVerHistorial;
        this.creadoEn = creadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public Empresa getEmpresa() { return empresa; }
    public Usuario getUsuario() { return usuario; }
    public String getRol() { return rol; }
    public Boolean getPuedeOperar() { return puedeOperar; }
    public Boolean getPuedeVerHistorial() { return puedeVerHistorial; }
    public LocalDateTime getCreadoEn() { return creadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setRol(String rol) { this.rol = rol; }
    public void setPuedeOperar(Boolean puedeOperar) { this.puedeOperar = puedeOperar; }
    public void setPuedeVerHistorial(Boolean puedeVerHistorial) { this.puedeVerHistorial = puedeVerHistorial; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    @Override
    public String toString() {
        return "UsuarioEmpresa{id=" + id + ", rol='" + rol + "'}";
    }
}
