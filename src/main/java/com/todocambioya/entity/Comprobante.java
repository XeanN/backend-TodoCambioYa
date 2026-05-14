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
@Table(name = "comprobantes")
public class Comprobante {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @Column(name = "numero_comprobante", unique = true, nullable = false)
    private String numeroComprobante;

    @Column(length = 10)
    private String tipo;

    @Column(name = "url_pdf")
    private String urlPdf;

    @Column(name = "generado_en", updatable = false)
    private LocalDateTime generadoEn;

    @PrePersist
    protected void onCreate() {
        generadoEn = LocalDateTime.now();
    }

    // ─── Constructores ───────────────────────────
    public Comprobante() {}

    public Comprobante(UUID id, Orden orden, String numeroComprobante,
                       String tipo, String urlPdf, LocalDateTime generadoEn) {
        this.id = id;
        this.orden = orden;
        this.numeroComprobante = numeroComprobante;
        this.tipo = tipo;
        this.urlPdf = urlPdf;
        this.generadoEn = generadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public Orden getOrden() { return orden; }
    public String getNumeroComprobante() { return numeroComprobante; }
    public String getTipo() { return tipo; }
    public String getUrlPdf() { return urlPdf; }
    public LocalDateTime getGeneradoEn() { return generadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setOrden(Orden orden) { this.orden = orden; }
    public void setNumeroComprobante(String numeroComprobante) { this.numeroComprobante = numeroComprobante; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setUrlPdf(String urlPdf) { this.urlPdf = urlPdf; }
    public void setGeneradoEn(LocalDateTime generadoEn) { this.generadoEn = generadoEn; }

    @Override
    public String toString() {
        return "Comprobante{id=" + id + ", numeroComprobante='" + numeroComprobante + "'}";
    }
}
