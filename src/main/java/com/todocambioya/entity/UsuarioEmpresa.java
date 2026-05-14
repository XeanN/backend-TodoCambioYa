package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios_empresa")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UsuarioEmpresa {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 10)
    private String rol; // 'admin' | 'operador' | 'lector'

    @Column(name = "puede_operar")
    @Builder.Default
    private Boolean puedeOperar = false;

    @Column(name = "puede_ver_historial")
    @Builder.Default
    private Boolean puedeVerHistorial = false;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}
