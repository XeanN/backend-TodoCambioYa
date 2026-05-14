package com.todocambioya.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipos_cambio")
public class TipoCambio {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "moneda_origen", nullable = false, length = 3)
    private String monedaOrigen;

    @Column(name = "moneda_destino", nullable = false, length = 3)
    private String monedaDestino;

    @Column(name = "tasa_compra", nullable = false, precision = 10, scale = 4)
    private BigDecimal tasaCompra;

    @Column(name = "tasa_venta", nullable = false, precision = 10, scale = 4)
    private BigDecimal tasaVenta;

    @Column(name = "tasa_preferencial", precision = 10, scale = 4)
    private BigDecimal tasaPreferencial;

    @Column(name = "registrado_en", updatable = false)
    private LocalDateTime registradoEn;

    @Column(name = "vigente_hasta")
    private LocalDateTime vigenteHasta;

    @PrePersist
    protected void onCreate() {
        registradoEn = LocalDateTime.now();
    }

    // ─── Constructores ───────────────────────────
    public TipoCambio() {}

    public TipoCambio(UUID id, String monedaOrigen, String monedaDestino, BigDecimal tasaCompra,
                      BigDecimal tasaVenta, BigDecimal tasaPreferencial,
                      LocalDateTime registradoEn, LocalDateTime vigenteHasta) {
        this.id = id;
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.tasaCompra = tasaCompra;
        this.tasaVenta = tasaVenta;
        this.tasaPreferencial = tasaPreferencial;
        this.registradoEn = registradoEn;
        this.vigenteHasta = vigenteHasta;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public String getMonedaOrigen() { return monedaOrigen; }
    public String getMonedaDestino() { return monedaDestino; }
    public BigDecimal getTasaCompra() { return tasaCompra; }
    public BigDecimal getTasaVenta() { return tasaVenta; }
    public BigDecimal getTasaPreferencial() { return tasaPreferencial; }
    public LocalDateTime getRegistradoEn() { return registradoEn; }
    public LocalDateTime getVigenteHasta() { return vigenteHasta; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setMonedaOrigen(String monedaOrigen) { this.monedaOrigen = monedaOrigen; }
    public void setMonedaDestino(String monedaDestino) { this.monedaDestino = monedaDestino; }
    public void setTasaCompra(BigDecimal tasaCompra) { this.tasaCompra = tasaCompra; }
    public void setTasaVenta(BigDecimal tasaVenta) { this.tasaVenta = tasaVenta; }
    public void setTasaPreferencial(BigDecimal tasaPreferencial) { this.tasaPreferencial = tasaPreferencial; }
    public void setRegistradoEn(LocalDateTime registradoEn) { this.registradoEn = registradoEn; }
    public void setVigenteHasta(LocalDateTime vigenteHasta) { this.vigenteHasta = vigenteHasta; }

    @Override
    public String toString() {
        return "TipoCambio{id=" + id + ", " + monedaOrigen + "->" + monedaDestino +
               ", compra=" + tasaCompra + ", venta=" + tasaVenta + "}";
    }
}
