package com.todocambioya.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todocambioya.mongodb.document.NotificacionPush;
import com.todocambioya.mongodb.repository.NotificacionPushRepository;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;
import java.util.Map;

@Component
public class PgNotificationListener {

    private final DataSource dataSource;
    private final NotificacionPushRepository mongoRepo;
    private final ObjectMapper objectMapper;
    private Connection listenerConn;

    public PgNotificationListener(DataSource dataSource,
                                  NotificacionPushRepository mongoRepo,
                                  ObjectMapper objectMapper) {
        this.dataSource   = dataSource;
        this.mongoRepo    = mongoRepo;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 3000)
    public void escuchar() throws Exception {
        if (listenerConn == null || listenerConn.isClosed()) {
            listenerConn = dataSource.getConnection();
            try (Statement st = listenerConn.createStatement()) {
                st.execute("LISTEN nueva_orden");
            }
        }

        PGConnection pgConn = listenerConn.unwrap(PGConnection.class);
        PGNotification[] notifs = pgConn.getNotifications(500);

        if (notifs != null) {
            for (PGNotification n : notifs) {
                procesarNotificacion(n.getParameter());
            }
        }
    }

    private void procesarNotificacion(String payload) {
        try {
            JsonNode json = objectMapper.readTree(payload);

            NotificacionPush doc = new NotificacionPush();
            doc.setUsuarioId(json.get("usuario_id").asText());
            doc.setTipo("orden");
            doc.setTitulo("Nueva orden registrada");
            doc.setContenido("Orden " + json.get("numero_orden").asText()
                           + " por USD " + json.get("monto").asText()
                           + " — estado: " + json.get("estado").asText());
            doc.setRegionCodigo(resolverRegion(json.get("region_id").asInt()));
            doc.setLeida(false);
            doc.setCreadoEn(Instant.now());
            doc.setMetadata(Map.of(
                "ordenNumero", json.get("numero_orden").asText(),
                "monto",       json.get("monto").asDouble(),
                "canal",       "push"
            ));

            mongoRepo.save(doc);
            System.out.println("✅ Mongo actualizado: " + json.get("numero_orden").asText());

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private String resolverRegion(int regionId) {
        return switch (regionId) {
            case 1 -> "LIM";
            case 2 -> "AQP";
            case 3 -> "TRU";
            default -> "LIM";
        };
    }
}