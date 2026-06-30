# Diagrama Entidad-Relación — TeCambioYa (PC3)

## Modelo relacional (PostgreSQL — 14 tablas)

```mermaid
erDiagram
    regiones ||--o{ usuarios : "region_id"
    regiones ||--o{ empresas : "region_id"
    regiones ||--o{ ordenes : "region_id"
    regiones ||--o{ auditoria_sesiones : "region_id"

    usuarios ||--o{ cuentas_bancarias : "usuario_id"
    usuarios ||--o{ ordenes : "usuario_id"
    usuarios ||--o{ alertas_tipo_cambio : "usuario_id"
    usuarios ||--o{ notificaciones : "usuario_id"
    usuarios ||--o{ auditoria_sesiones : "usuario_id"
    usuarios ||--o{ usuarios_empresa : "usuario_id"
    usuarios ||--o{ referidos : "referidor_id"
    usuarios ||--o{ referidos : "referido_id"

    bancos ||--o{ cuentas_bancarias : "banco_id"

    tipos_cambio ||--o{ ordenes : "tipo_cambio_id"

    cuentas_bancarias ||--o{ ordenes : "cuenta_origen_id"
    cuentas_bancarias ||--o{ ordenes : "cuenta_destino_id"

    cupones ||--o{ ordenes : "cupon_id"
    cupones ||--o{ referidos : "cupon_generado_id"

    empresas ||--o{ usuarios_empresa : "empresa_id"

    ordenes ||--o{ comprobantes : "orden_id"

    regiones {
        int id PK
        varchar nombre
        varchar codigo UK
        varchar nodo_db
        boolean activo
    }

    usuarios {
        uuid id PK
        varchar nombre_completo
        varchar email UK
        varchar password_hash
        varchar tipo_cuenta
        int region_id FK
    }

    ordenes {
        uuid id PK
        varchar numero_orden UK
        uuid usuario_id FK
        uuid tipo_cambio_id FK
        uuid cuenta_origen_id FK
        uuid cuenta_destino_id FK
        decimal monto_enviado
        decimal monto_recibido
        decimal tasa_aplicada
        varchar estado
        uuid cupon_id FK
        int region_id FK
        timestamp creado_en
        timestamp completado_en
    }

    cupones {
        uuid id PK
        varchar codigo UK
        varchar tipo
        decimal valor
        int usos_maximos
        int usos_actuales
    }

    comprobantes {
        uuid id PK
        uuid orden_id FK
        varchar numero_comprobante UK
        varchar tipo
    }
```

## Tabla más detallada: `ordenes`

Contiene **50 filas** de demostración (migración `V2__pc3_datos_demostracion.sql`).

Campos clave: montos, tasas, estados, cuentas origen/destino, cupón, región, fechas.

## Base de datos distribuida (Fase 2)

| Tipo | Tablas | Estrategia |
|------|--------|------------|
| Replicadas | regiones, bancos, tipos_cambio, cupones, empresas | Misma copia en 3 nodos |
| Fragmentadas | usuarios, cuentas_bancarias, ordenes, comprobantes, notificaciones | Por `region_id` (Lima/Arequipa/Trujillo) |

Scripts manuales por nodo: `src/main/resources/db/manual/nodos/`

## MongoDB (Fase 3 — datos no estructurados)

Colección `notificaciones_push`: chat, alertas push, metadata flexible.

Ver: `src/main/resources/mongodb/init-collections.js`
