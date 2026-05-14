package com.todocambioya.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrdenResponse {

    private UUID id;
    private String numeroOrden;
    private String estado;
    private BigDecimal montoEnviado;
    private BigDecimal montoRecibido;
    private BigDecimal tasaAplicada;
    private String monedaOrigen;
    private String monedaDestino;
    private String cuentaOrigen;
    private String cuentaDestino;
    private String bancoOrigen;
    private String bancoDestino;
    private LocalDateTime creadoEn;
    private LocalDateTime completadoEn;

    // ─── Constructores ───────────────────────────
    public OrdenResponse() {}

    public OrdenResponse(UUID id, String numeroOrden, String estado, BigDecimal montoEnviado,
                         BigDecimal montoRecibido, BigDecimal tasaAplicada, String monedaOrigen,
                         String monedaDestino, String cuentaOrigen, String cuentaDestino,
                         String bancoOrigen, String bancoDestino,
                         LocalDateTime creadoEn, LocalDateTime completadoEn) {
        this.id = id;
        this.numeroOrden = numeroOrden;
        this.estado = estado;
        this.montoEnviado = montoEnviado;
        this.montoRecibido = montoRecibido;
        this.tasaAplicada = tasaAplicada;
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.bancoOrigen = bancoOrigen;
        this.bancoDestino = bancoDestino;
        this.creadoEn = creadoEn;
        this.completadoEn = completadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public String getNumeroOrden() { return numeroOrden; }
    public String getEstado() { return estado; }
    public BigDecimal getMontoEnviado() { return montoEnviado; }
    public BigDecimal getMontoRecibido() { return montoRecibido; }
    public BigDecimal getTasaAplicada() { return tasaAplicada; }
    public String getMonedaOrigen() { return monedaOrigen; }
    public String getMonedaDestino() { return monedaDestino; }
    public String getCuentaOrigen() { return cuentaOrigen; }
    public String getCuentaDestino() { return cuentaDestino; }
    public String getBancoOrigen() { return bancoOrigen; }
    public String getBancoDestino() { return bancoDestino; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
    public LocalDateTime getCompletadoEn() { return completadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setNumeroOrden(String numeroOrden) { this.numeroOrden = numeroOrden; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setMontoEnviado(BigDecimal montoEnviado) { this.montoEnviado = montoEnviado; }
    public void setMontoRecibido(BigDecimal montoRecibido) { this.montoRecibido = montoRecibido; }
    public void setTasaAplicada(BigDecimal tasaAplicada) { this.tasaAplicada = tasaAplicada; }
    public void setMonedaOrigen(String monedaOrigen) { this.monedaOrigen = monedaOrigen; }
    public void setMonedaDestino(String monedaDestino) { this.monedaDestino = monedaDestino; }
    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }
    public void setBancoOrigen(String bancoOrigen) { this.bancoOrigen = bancoOrigen; }
    public void setBancoDestino(String bancoDestino) { this.bancoDestino = bancoDestino; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
    public void setCompletadoEn(LocalDateTime completadoEn) { this.completadoEn = completadoEn; }
}
