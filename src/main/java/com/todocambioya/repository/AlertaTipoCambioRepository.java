package com.todocambioya.repository;

import com.todocambioya.entity.AlertaTipoCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertaTipoCambioRepository extends JpaRepository<AlertaTipoCambio, UUID> {

    List<AlertaTipoCambio> findByUsuarioId(UUID usuarioId);
    List<AlertaTipoCambio> findByDisparadaFalse(); // alertas pendientes de disparar
    List<AlertaTipoCambio> findByUsuarioIdAndDisparadaFalse(UUID usuarioId);
}
