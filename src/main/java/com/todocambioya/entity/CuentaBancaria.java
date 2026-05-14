package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cuentas_bancarias")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
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
    private String moneda; // PEN | USD

    @Column(length = 50)
    private String alias;

    @Builder.Default
    private Boolean verificada = false;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}
