package com.todocambioya.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bancos")
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo_bcp", length = 10)
    private String codigoBcp;

    @Column(length = 15)
    private String tipo;

    @Column(name = "logo_url")
    private String logoUrl;

    private Boolean activo = true;

    // ─── Constructores ───────────────────────────
    public Banco() {}

    public Banco(Integer id, String nombre, String codigoBcp, String tipo, String logoUrl, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.codigoBcp = codigoBcp;
        this.tipo = tipo;
        this.logoUrl = logoUrl;
        this.activo = activo;
    }

    // ─── Getters ─────────────────────────────────
    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCodigoBcp() { return codigoBcp; }
    public String getTipo() { return tipo; }
    public String getLogoUrl() { return logoUrl; }
    public Boolean getActivo() { return activo; }

    // ─── Setters ─────────────────────────────────
    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCodigoBcp(String codigoBcp) { this.codigoBcp = codigoBcp; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "Banco{id=" + id + ", nombre='" + nombre + "'}";
    }
}
