package com.todocambioya.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CuentaBancariaRequest {

    @NotNull(message = "El banco es obligatorio")
    private Integer bancoId;

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    private String cci;

    @NotBlank
    @Pattern(regexp = "PEN|USD", message = "La moneda debe ser PEN o USD")
    private String moneda;

    private String alias;

    // ─── Constructores ───────────────────────────
    public CuentaBancariaRequest() {}

    public CuentaBancariaRequest(Integer bancoId, String numeroCuenta, String cci,
                                  String moneda, String alias) {
        this.bancoId = bancoId;
        this.numeroCuenta = numeroCuenta;
        this.cci = cci;
        this.moneda = moneda;
        this.alias = alias;
    }

    // ─── Getters ─────────────────────────────────
    public Integer getBancoId() { return bancoId; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getCci() { return cci; }
    public String getMoneda() { return moneda; }
    public String getAlias() { return alias; }

    // ─── Setters ─────────────────────────────────
    public void setBancoId(Integer bancoId) { this.bancoId = bancoId; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public void setCci(String cci) { this.cci = cci; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public void setAlias(String alias) { this.alias = alias; }
}
