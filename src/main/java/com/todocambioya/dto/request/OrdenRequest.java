package com.todocambioya.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class OrdenRequest {

    @NotNull(message = "La cuenta de origen es obligatoria")
    private UUID cuentaOrigenId;

    @NotNull(message = "La cuenta de destino es obligatoria")
    private UUID cuentaDestinoId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "1.00", message = "El monto mínimo es 1.00")
    private BigDecimal montoEnviado;

    @NotNull(message = "El tipo de cambio es obligatorio")
    private UUID tipoCambioId;

    private String codigoCupon;

    // ─── Constructores ───────────────────────────
    public OrdenRequest() {}

    public OrdenRequest(UUID cuentaOrigenId, UUID cuentaDestinoId, BigDecimal montoEnviado,
                        UUID tipoCambioId, String codigoCupon) {
        this.cuentaOrigenId = cuentaOrigenId;
        this.cuentaDestinoId = cuentaDestinoId;
        this.montoEnviado = montoEnviado;
        this.tipoCambioId = tipoCambioId;
        this.codigoCupon = codigoCupon;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getCuentaOrigenId() { return cuentaOrigenId; }
    public UUID getCuentaDestinoId() { return cuentaDestinoId; }
    public BigDecimal getMontoEnviado() { return montoEnviado; }
    public UUID getTipoCambioId() { return tipoCambioId; }
    public String getCodigoCupon() { return codigoCupon; }

    // ─── Setters ─────────────────────────────────
    public void setCuentaOrigenId(UUID cuentaOrigenId) { this.cuentaOrigenId = cuentaOrigenId; }
    public void setCuentaDestinoId(UUID cuentaDestinoId) { this.cuentaDestinoId = cuentaDestinoId; }
    public void setMontoEnviado(BigDecimal montoEnviado) { this.montoEnviado = montoEnviado; }
    public void setTipoCambioId(UUID tipoCambioId) { this.tipoCambioId = tipoCambioId; }
    public void setCodigoCupon(String codigoCupon) { this.codigoCupon = codigoCupon; }
}
