package com.todocambioya.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 150)
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Pattern(regexp = "personal|corp", message = "Tipo de cuenta debe ser 'personal' o 'corp'")
    private String tipoCuenta;

    private String dniRuc;
    private String telefono;
    private Integer regionId;

    // ─── Constructores ───────────────────────────
    public RegisterRequest() {}

    public RegisterRequest(String nombreCompleto, String email, String password,
                           String tipoCuenta, String dniRuc, String telefono, Integer regionId) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.password = password;
        this.tipoCuenta = tipoCuenta;
        this.dniRuc = dniRuc;
        this.telefono = telefono;
        this.regionId = regionId;
    }

    // ─── Getters ─────────────────────────────────
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getTipoCuenta() { return tipoCuenta; }
    public String getDniRuc() { return dniRuc; }
    public String getTelefono() { return telefono; }
    public Integer getRegionId() { return regionId; }

    // ─── Setters ─────────────────────────────────
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public void setDniRuc(String dniRuc) { this.dniRuc = dniRuc; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
}
