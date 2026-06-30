package com.todocambioya.mongodb.projection;

import java.time.Instant;

/**
 * Proyección MongoDB — solo campos necesarios para listado.
 */
public interface NotificacionResumen {

    String getTitulo();
    String getTipo();
    boolean isLeida();
    Instant getCreadoEn();
}
