package com.todocambioya.mongodb.document;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notificaciones_push")
@CompoundIndex(name = "idx_region_tipo", def = "{'regionCodigo': 1, 'tipo': 1}")
public class NotificacionPush {

    @Id
    private String id;

    @Indexed
    private String usuarioId;

    private String tipo;
    private String titulo;
    private String contenido;

    @Indexed
    private String regionCodigo;

    private boolean leida;
    private Map<String, Object> metadata;

    @Indexed
    private Instant creadoEn;

    public NotificacionPush() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public String getRegionCodigo() { return regionCodigo; }
    public void setRegionCodigo(String regionCodigo) { this.regionCodigo = regionCodigo; }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public Instant getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
}
