package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comprobantes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Comprobante {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @Column(name = "numero_comprobante", unique = true, nullable = false)
    private String numeroComprobante;

    @Column(length = 10)
    private String tipo; // 'recibo' | 'factura' | 'boleta'

    @Column(name = "url_pdf")
    private String urlPdf;

    @Column(name = "generado_en", updatable = false)
    private LocalDateTime generadoEn;

    @PrePersist
    protected void onCreate() {
        generadoEn = LocalDateTime.now();
    }
}
