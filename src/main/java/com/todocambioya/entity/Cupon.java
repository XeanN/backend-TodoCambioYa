package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cupones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Cupon {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false, length = 30)
    private String codigo;

    @Column(length = 15)
    private String tipo; // 'pips' | 'porcentaje'

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal valor;

    @Column(name = "usos_maximos", nullable = false)
    private Integer usosMaximos;

    @Column(name = "usos_actuales")
    @Builder.Default
    private Integer usosActuales = 0;

    @Column(name = "monto_minimo", precision = 15, scale = 2)
    private BigDecimal montoMinimo;

    @Column(name = "vence_en")
    private LocalDateTime venceEn;

    @Builder.Default
    private Boolean activo = true;
}
