package com.todocambioya.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cupones")
public class Cupon {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false, length = 30)
    private String codigo;

    @Column(length = 15)
    private String tipo;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal valor;

    @Column(name = "usos_maximos", nullable = false)
    private Integer usosMaximos;

    @Column(name = "usos_actuales")
    private Integer usosActuales = 0;

    @Column(name = "monto_minimo", precision = 15, scale = 2)
    private BigDecimal montoMinimo;

    @Column(name = "vence_en")
    private LocalDateTime venceEn;

    private Boolean activo = true;

    // ─── Constructores ───────────────────────────
    public Cupon() {}

    public Cupon(UUID id, String codigo, String tipo, BigDecimal valor, Integer usosMaximos,
                 Integer usosActuales, BigDecimal montoMinimo, LocalDateTime venceEn, Boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.tipo = tipo;
        this.valor = valor;
        this.usosMaximos = usosMaximos;
        this.usosActuales = usosActuales;
        this.montoMinimo = montoMinimo;
        this.venceEn = venceEn;
        this.activo = activo;
    }

    // ─── Getters ─────────────────────────────────
    public UUID getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getTipo() { return tipo; }
    public BigDecimal getValor() { return valor; }
    public Integer getUsosMaximos() { return usosMaximos; }
    public Integer getUsosActuales() { return usosActuales; }
    public BigDecimal getMontoMinimo() { return montoMinimo; }
    public LocalDateTime getVenceEn() { return venceEn; }
    public Boolean getActivo() { return activo; }

    // ─── Setters ─────────────────────────────────
    public void setId(UUID id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setUsosMaximos(Integer usosMaximos) { this.usosMaximos = usosMaximos; }
    public void setUsosActuales(Integer usosActuales) { this.usosActuales = usosActuales; }
    public void setMontoMinimo(BigDecimal montoMinimo) { this.montoMinimo = montoMinimo; }
    public void setVenceEn(LocalDateTime venceEn) { this.venceEn = venceEn; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "Cupon{id=" + id + ", codigo='" + codigo + "', tipo='" + tipo + "'}";
    }
}
