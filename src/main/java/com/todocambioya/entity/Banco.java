package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bancos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo_bcp", length = 10)
    private String codigoBcp;

    @Column(length = 15)
    private String tipo; // 'banco' | 'caja_mun'

    @Column(name = "logo_url")
    private String logoUrl;

    @Builder.Default
    private Boolean activo = true;
}
