package com.todocambioya.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todocambioya.dto.response.ApiResponse;
import com.todocambioya.mongodb.projection.NotificacionResumen;
import com.todocambioya.service.MongoNotificacionService;

@RestController
@RequestMapping("/pc3")
public class Pc3DemoController {

    private final JdbcTemplate jdbcTemplate;
    private final MongoNotificacionService mongoService;

    public Pc3DemoController(JdbcTemplate jdbcTemplate, MongoNotificacionService mongoService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mongoService = mongoService;
    }

    @GetMapping("/ordenes/count")
    public ApiResponse<Long> contarOrdenes() {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ordenes", Long.class);
        return ApiResponse.ok(total, "Total de filas en tabla ordenes");
    }

    @GetMapping("/ordenes/procesar/{numeroOrden}")
    public ApiResponse<List<Map<String, Object>>> procesarOrden(@PathVariable String numeroOrden) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
            "SELECT * FROM procesar_orden(?)", numeroOrden
        );
        return ApiResponse.ok(result, "Proceso 2: procesar_orden ejecutado");
    }

    @GetMapping("/mongo/proyeccion")
    public ApiResponse<List<NotificacionResumen>> mongoProyeccion() {
        return ApiResponse.ok(mongoService.listarNoLeidasProyectadas(), "Proyección MongoDB");
    }

    @GetMapping("/mongo/agregacion/tipo-region")
    public ApiResponse<List<Map>> mongoAgregacionTipoRegion() {
        return ApiResponse.ok(mongoService.contarPorTipoYRegion(), "Agregación por tipo y región");
    }

    @GetMapping("/mongo/agregacion/pendientes")
    public ApiResponse<List<Map>> mongoAgregacionPendientes() {
        return ApiResponse.ok(mongoService.pendientesPorUsuario(), "Agregación pendientes por usuario");
    }
}
