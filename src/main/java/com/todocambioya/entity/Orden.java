package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ordenes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
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
    private String estado; // 'pendiente' | 'procesando' | 'completado' | 'cancelado'

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
}
