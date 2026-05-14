package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alertas_tipo_cambio")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AlertaTipoCambio {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 3)
    private String moneda;

    @Column(name = "tasa_objetivo", nullable = false, precision = 10, scale = 4)
    private BigDecimal tasaObjetivo;

    @Column(length = 10)
    private String condicion; // 'mayor' | 'menor'

    @Column(name = "canal_notif", length = 10)
    private String canalNotif; // 'email' | 'sms' | 'push'

    @Builder.Default
    private Boolean disparada = false;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}
