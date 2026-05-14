package com.todocambioya.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regiones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, nullable = false, length = 10)
    private String codigo;

    @Column(name = "nodo_db")
    private String nodoDB;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
