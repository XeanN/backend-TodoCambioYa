package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "referidos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Referido {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referidor_id", nullable = false)
    private Usuario referidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referido_id", nullable = false)
    private Usuario referido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cupon_generado_id")
    private Cupon cuponGenerado;

    @Builder.Default
    private Boolean completado = false;

    @Column(name = "pips_ganados", precision = 10, scale = 4)
    @Builder.Default
    private BigDecimal pipsGanados = BigDecimal.ZERO;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}
