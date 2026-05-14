package com.todocambioya.dto.response;

import java.util.UUID;

public class CuentaBancariaResponse {

    private UUID id;
    private String numeroCuenta;
    private String cci;
    private String moneda;
    private String alias;
    private Boolean verificada;
    private String banco;
    private String logoBanco;

    // ─── Constructores ───────────────────────────
    public CuentaBancariaResponse() {}

    public CuentaBancariaResponse(UUID id, String numeroCuenta, String cci, String moneda,
                                   String alias, Boolean verificada, String banco, String logoBanco) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.cci = cci;
        this.moneda = moneda;
        this.alias = alias;
        this.verificada = verificada;
        this.banco = banco;
        this.logoBanco = logoBanco;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getCci() { return cci; }
    public String getMoneda() { return moneda; }
    public String getAlias() { return alias; }
    public Boolean getVerificada() { return verificada; }
    public String getBanco() { return banco; }
    public String getLogoBanco() { return logoBanco; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public void setCci(String cci) { this.cci = cci; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public void setAlias(String alias) { this.alias = alias; }
    public void setVerificada(Boolean verificada) { this.verificada = verificada; }
    public void setBanco(String banco) { this.banco = banco; }
    public void setLogoBanco(String logoBanco) { this.logoBanco = logoBanco; }
}
