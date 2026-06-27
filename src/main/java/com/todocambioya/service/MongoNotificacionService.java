package com.todocambioya.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.todocambioya.mongodb.document.NotificacionPush;
import com.todocambioya.mongodb.projection.NotificacionResumen;
import com.todocambioya.mongodb.repository.NotificacionPushRepository;

@Service
public class MongoNotificacionService {

    private final NotificacionPushRepository repository;
    private final MongoTemplate mongoTemplate;

    public MongoNotificacionService(NotificacionPushRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    /** Proyección: solo titulo, tipo, leida, creadoEn */
    public List<NotificacionResumen> listarNoLeidasProyectadas() {
        return repository.findByLeidaFalse();
    }

    /** Agregación: conteo por tipo y región */
    public List<Map> contarPorTipoYRegion() {
        Aggregation agg = Aggregation.newAggregation(
            Aggregation.group("tipo", "regionCodigo").count().as("total"),
            Aggregation.sort(org.springframework.data.domain.Sort.Direction.DESC, "total")
        );
        AggregationResults<Map> results = mongoTemplate.aggregate(agg, "notificaciones_push", Map.class);
        return results.getMappedResults();
    }

    /** Agregación: pendientes por usuario */
    public List<Map> pendientesPorUsuario() {
        Aggregation agg = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("leida").is(false)),
            Aggregation.group("usuarioId").count().as("pendientes")
        );
        AggregationResults<Map> results = mongoTemplate.aggregate(agg, "notificaciones_push", Map.class);
        return results.getMappedResults();
    }

    public List<NotificacionPush> listarTodas() {
        return repository.findAll();
    }
}
