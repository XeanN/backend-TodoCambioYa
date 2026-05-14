package com.todocambioya.repository;

import com.todocambioya.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, UUID> {

    List<Notificacion> findByUsuarioIdOrderByCreadoEnDesc(UUID usuarioId);
    List<Notificacion> findByUsuarioIdAndLeidaFalse(UUID usuarioId);
    long countByUsuarioIdAndLeidaFalse(UUID usuarioId); // cantidad de no leídas
}
