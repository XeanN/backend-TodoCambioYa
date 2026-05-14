package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tipos_cambio")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TipoCambio {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "moneda_origen", nullable = false, length = 3)
    private String monedaOrigen; // USD

    @Column(name = "moneda_destino", nullable = false, length = 3)
    private String monedaDestino; // PEN

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
}
