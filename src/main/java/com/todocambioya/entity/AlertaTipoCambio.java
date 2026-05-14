package com.todocambioya.entity;

import java.math.BigDecimal;
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
@Table(name = "alertas_tipo_cambio")
public class AlertaTipoCambio {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 3)
    private String moneda;

    @Column(name = "tasa_objetivo", nullable = false, precision = 10, scale = 4)
    private BigDecimal tasaObjetivo;

    @Column(length = 10)
    private String condicion;

    @Column(name = "canal_notif", length = 10)
    private String canalNotif;

    private Boolean disparada = false;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }

    // ─── Constructores ───────────────────────────
    public AlertaTipoCambio() {}

    public AlertaTipoCambio(UUID id, Usuario usuario, String moneda, BigDecimal tasaObjetivo,
                            String condicion, String canalNotif, Boolean disparada, LocalDateTime creadoEn) {
        this.id = id;
        this.usuario = usuario;
        this.moneda = moneda;
        this.tasaObjetivo = tasaObjetivo;
        this.condicion = condicion;
        this.canalNotif = canalNotif;
        this.disparada = disparada;
        this.creadoEn = creadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getMoneda() { return moneda; }
    public BigDecimal getTasaObjetivo() { return tasaObjetivo; }
    public String getCondicion() { return condicion; }
    public String getCanalNotif() { return canalNotif; }
    public Boolean getDisparada() { return disparada; }
    public LocalDateTime getCreadoEn() { return creadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public void setTasaObjetivo(BigDecimal tasaObjetivo) { this.tasaObjetivo = tasaObjetivo; }
    public void setCondicion(String condicion) { this.condicion = condicion; }
    public void setCanalNotif(String canalNotif) { this.canalNotif = canalNotif; }
    public void setDisparada(Boolean disparada) { this.disparada = disparada; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    @Override
    public String toString() {
        return "AlertaTipoCambio{id=" + id + ", moneda='" + moneda + "', tasaObjetivo=" + tasaObjetivo + "}";
    }
}
