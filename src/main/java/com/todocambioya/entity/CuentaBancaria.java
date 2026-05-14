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
@Table(name = "cuentas_bancarias")
public class CuentaBancaria {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banco_id", nullable = false)
    private Banco banco;

    @Column(name = "numero_cuenta", nullable = false, length = 30)
    private String numeroCuenta;

    @Column(length = 30)
    private String cci;

    @Column(nullable = false, length = 3)
    private String moneda;

    @Column(length = 50)
    private String alias;

    private Boolean verificada = false;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }

    // ─── Constructores ───────────────────────────
    public CuentaBancaria() {}

    public CuentaBancaria(UUID id, Usuario usuario, Banco banco, String numeroCuenta,
                          String cci, String moneda, String alias, Boolean verificada,
                          LocalDateTime creadoEn) {
        this.id = id;
        this.usuario = usuario;
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.cci = cci;
        this.moneda = moneda;
        this.alias = alias;
        this.verificada = verificada;
        this.creadoEn = creadoEn;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Banco getBanco() { return banco; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getCci() { return cci; }
    public String getMoneda() { return moneda; }
    public String getAlias() { return alias; }
    public Boolean getVerificada() { return verificada; }
    public LocalDateTime getCreadoEn() { return creadoEn; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setBanco(Banco banco) { this.banco = banco; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public void setCci(String cci) { this.cci = cci; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public void setAlias(String alias) { this.alias = alias; }
    public void setVerificada(Boolean verificada) { this.verificada = verificada; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    @Override
    public String toString() {
        return "CuentaBancaria{id=" + id + ", numeroCuenta='" + numeroCuenta + "', moneda='" + moneda + "'}";
    }
}
