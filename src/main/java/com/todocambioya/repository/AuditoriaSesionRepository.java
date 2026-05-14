package com.todocambioya.repository;

import com.todocambioya.entity.AuditoriaSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditoriaSesionRepository extends JpaRepository<AuditoriaSesion, UUID> {

    List<AuditoriaSesion> findByUsuarioIdOrderByCreadoEnDesc(UUID usuarioId);
    List<AuditoriaSesion> findByAccion(String accion); // 'login' | 'logout' | 'operacion'
}
