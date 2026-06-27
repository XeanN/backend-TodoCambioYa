package com.todocambioya.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.todocambioya.mongodb.document.NotificacionPush;
import com.todocambioya.mongodb.projection.NotificacionResumen;

public interface NotificacionPushRepository extends MongoRepository<NotificacionPush, String> {

    List<NotificacionResumen> findByLeidaFalse();

    List<NotificacionPush> findByUsuarioIdAndLeidaFalse(String usuarioId);
}
