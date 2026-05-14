package com.todocambioya.repository;

import com.todocambioya.entity.TipoCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoCambioRepository extends JpaRepository<TipoCambio, UUID> {

    // Trae el tipo de cambio vigente más reciente para USD→PEN
    @Query("SELECT t FROM TipoCambio t WHERE t.monedaOrigen = :origen " +
           "AND t.monedaDestino = :destino " +
           "AND (t.vigenteHasta IS NULL OR t.vigenteHasta > :ahora) " +
           "ORDER BY t.registradoEn DESC")
    Optional<TipoCambio> findVigente(String origen, String destino, LocalDateTime ahora);
}
