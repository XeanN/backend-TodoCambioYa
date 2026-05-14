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
@Table(name = "ordenes")
public class Orden {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "numero_orden", unique = true, nullable = false)
    private String numeroOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_cambio_id", nullable = false)
    private TipoCambio tipoCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_origen_id", nullable = false)
    private CuentaBancaria cuentaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_destino_id", nullable = false)
    private CuentaBancaria cuentaDestino;

    @Column(name = "monto_enviado", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoEnviado;

    @Column(name = "monto_recibido", precision = 15, scale = 2)
    private BigDecimal montoRecibido;

    @Column(name = "tasa_aplicada", nullable = false, precision = 10, scale = 4)
    private BigDecimal tasaAplicada;

    @Column(length = 15)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cupon_id")
    private Cupon cupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "completado_en")
    private LocalDateTime completadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }

    // ─── Constructores ───────────────────────────
    public Orden() {}

    public Orden(UUID id, String numeroOrden, Usuario usuario, TipoCambio tipoCambio,
                 CuentaBancaria cuentaOrigen, CuentaBancaria cuentaDestino,
                 BigDecimal montoEnviado, BigDecimal montoRecibido, BigDecimal tasaAplicada,
                 String estado, Cupon cupon, Region region,
                 LocalDateTime creadoEn, LocalDateTime completadoEn) {
        this.id = id;
        this.numeroOrden = numeroOrden;
        this.usuario = usuario;
        this.tipoCambio = tipoCambio;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.montoEnviado = montoEnviado;
        this.montoRecibido = montoRecibido;
        this.tasaAplicada = tasaAplicada;
        this.estado = estado;
        this.cupon = cupon;
        this.region = region;
        this.creadoEn = creadoEn;
        this.completadoEn = completadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public String getNumeroOrden() { return numeroOrden; }
    public Usuario getUsuario() { return usuario; }
    public TipoCambio getTipoCambio() { return tipoCambio; }
    public CuentaBancaria getCuentaOrigen() { return cuentaOrigen; }
    public CuentaBancaria getCuentaDestino() { return cuentaDestino; }
    public BigDecimal getMontoEnviado() { return montoEnviado; }
    public BigDecimal getMontoRecibido() { return montoRecibido; }
    public BigDecimal getTasaAplicada() { return tasaAplicada; }
    public String getEstado() { return estado; }
    public Cupon getCupon() { return cupon; }
    public Region getRegion() { return region; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
    public LocalDateTime getCompletadoEn() { return completadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setNumeroOrden(String numeroOrden) { this.numeroOrden = numeroOrden; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setTipoCambio(TipoCambio tipoCambio) { this.tipoCambio = tipoCambio; }
    public void setCuentaOrigen(CuentaBancaria cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }
    public void setCuentaDestino(CuentaBancaria cuentaDestino) { this.cuentaDestino = cuentaDestino; }
    public void setMontoEnviado(BigDecimal montoEnviado) { this.montoEnviado = montoEnviado; }
    public void setMontoRecibido(BigDecimal montoRecibido) { this.montoRecibido = montoRecibido; }
    public void setTasaAplicada(BigDecimal tasaAplicada) { this.tasaAplicada = tasaAplicada; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCupon(Cupon cupon) { this.cupon = cupon; }
    public void setRegion(Region region) { this.region = region; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
    public void setCompletadoEn(LocalDateTime completadoEn) { this.completadoEn = completadoEn; }

    @Override
    public String toString() {
        return "Orden{id=" + id + ", numeroOrden='" + numeroOrden + "', estado='" + estado + "'}";
    }
}
