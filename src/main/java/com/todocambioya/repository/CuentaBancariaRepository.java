package com.todocambioya.repository;

import com.todocambioya.entity.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, UUID> {

    List<CuentaBancaria> findByUsuarioId(UUID usuarioId);
    List<CuentaBancaria> findByUsuarioIdAndMoneda(UUID usuarioId, String moneda);
    boolean existsByNumeroCuentaAndBancoId(String numeroCuenta, Integer bancoId);
}
