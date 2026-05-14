package com.todocambioya.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "regiones")
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
    private Boolean activo = true;

    // ─── Constructores ───────────────────────────
    public Region() {}

    public Region(Integer id, String nombre, String codigo, String nodoDB, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.nodoDB = nodoDB;
        this.activo = activo;
    }

    // ─── Getters ─────────────────────────────────
    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public String getNodoDB() { return nodoDB; }
    public Boolean getActivo() { return activo; }

    // ─── Setters ─────────────────────────────────
    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNodoDB(String nodoDB) { this.nodoDB = nodoDB; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    // ─── toString ────────────────────────────────
    @Override
    public String toString() {
        return "Region{id=" + id + ", nombre='" + nombre + "', codigo='" + codigo + "'}";
    }
}
