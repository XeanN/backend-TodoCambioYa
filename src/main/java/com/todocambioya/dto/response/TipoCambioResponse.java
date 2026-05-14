package com.todocambioya.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TipoCambioResponse {

    private UUID id;
    private String monedaOrigen;
    private String monedaDestino;
    private BigDecimal tasaCompra;
    private BigDecimal tasaVenta;
    private BigDecimal tasaPreferencial;
    private LocalDateTime registradoEn;
    private LocalDateTime vigenteHasta;

    // ─── Constructores ───────────────────────────
    public TipoCambioResponse() {}

    public TipoCambioResponse(UUID id, String monedaOrigen, String monedaDestino,
                               BigDecimal tasaCompra, BigDecimal tasaVenta,
                               BigDecimal tasaPreferencial, LocalDateTime registradoEn,
                               LocalDateTime vigenteHasta) {
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
}
