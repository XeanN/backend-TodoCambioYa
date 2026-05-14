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
@Table(name = "referidos")
public class Referido {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referidor_id", nullable = false)
    private Usuario referidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referido_id", nullable = false)
    private Usuario referido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cupon_generado_id")
    private Cupon cuponGenerado;

    private Boolean completado = false;

    @Column(name = "pips_ganados", precision = 10, scale = 4)
    private BigDecimal pipsGanados = BigDecimal.ZERO;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }

    // ─── Constructores ───────────────────────────
    public Referido() {}

    public Referido(UUID id, Usuario referidor, Usuario referido, Cupon cuponGenerado,
                    Boolean completado, BigDecimal pipsGanados, LocalDateTime creadoEn) {
        this.id = id;
        this.referidor = referidor;
        this.referido = referido;
        this.cuponGenerado = cuponGenerado;
        this.completado = completado;
        this.pipsGanados = pipsGanados;
        this.creadoEn = creadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public Usuario getReferidor() { return referidor; }
    public Usuario getReferido() { return referido; }
    public Cupon getCuponGenerado() { return cuponGenerado; }
    public Boolean getCompletado() { return completado; }
    public BigDecimal getPipsGanados() { return pipsGanados; }
    public LocalDateTime getCreadoEn() { return creadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setReferidor(Usuario referidor) { this.referidor = referidor; }
    public void setReferido(Usuario referido) { this.referido = referido; }
    public void setCuponGenerado(Cupon cuponGenerado) { this.cuponGenerado = cuponGenerado; }
    public void setCompletado(Boolean completado) { this.completado = completado; }
    public void setPipsGanados(BigDecimal pipsGanados) { this.pipsGanados = pipsGanados; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    @Override
    public String toString() {
        return "Referido{id=" + id + ", completado=" + completado + "}";
    }
}
