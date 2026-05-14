package com.todocambioya.repository;

import com.todocambioya.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, UUID> {

    Optional<Orden> findByNumeroOrden(String numeroOrden);
    List<Orden> findByUsuarioIdOrderByCreadoEnDesc(UUID usuarioId);
    List<Orden> findByEstado(String estado);
    List<Orden> findByUsuarioIdAndEstado(UUID usuarioId, String estado);
    List<Orden> findByRegionId(Integer regionId);
}
