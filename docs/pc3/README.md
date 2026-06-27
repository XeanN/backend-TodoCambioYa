# PC3 — Guía rápida TeCambioYa

## Requisitos implementados

| Requisito | Ubicación |
|-----------|-----------|
| Diagrama E-R | `docs/pc3/DIAGRAMA-ER.md` |
| 50 filas en `ordenes` | `V2__pc3_datos_demostracion.sql` |
| Proceso 1: `registrar_orden_cambio` | `V3__pc3_procedimientos.sql` |
| Proceso 2: `procesar_orden` | `V3__pc3_procedimientos.sql` |
| Concurrencia: `aplicar_cupon` | `V3__pc3_procedimientos.sql` + `demo-concurrencia.sql` |
| BD distribuida | `docker-compose.yml` + `db/manual/nodos/` |
| MongoDB validaciones/índices/agregaciones/proyecciones | `mongodb/init-collections.js` + Java en `mongodb/` |
| Presentación PPT (estructura) | `PRESENTACION-PC3.md` |

## Levantar entorno

```powershell
docker compose up -d
mvn spring-boot:run
```

## Verificar PostgreSQL

```sql
SELECT COUNT(*) FROM ordenes;  -- debe retornar 50
SELECT * FROM procesar_orden('ORD-LIM-0007');
SELECT * FROM aplicar_cupon('PROMO-CONC', (SELECT id FROM ordenes WHERE estado='pendiente' LIMIT 1));
```

## Endpoints demo (PC3)

- `GET /api/pc3/ordenes/count`
- `GET /api/pc3/ordenes/procesar/{numeroOrden}`
- `GET /api/pc3/mongo/proyeccion`
- `GET /api/pc3/mongo/agregacion/tipo-region`
- `GET /api/pc3/mongo/agregacion/pendientes`

## Nodos distribuidos (manual)

```powershell
psql -h localhost -p 5442 -U tcya_user -d todocambioya_lima -f src/main/resources/db/manual/nodos/nodo_lima.sql
```

Los scripts de nodos Arequipa/Trujillo siguen el mismo patrón con `region_id` 2 y 3.
