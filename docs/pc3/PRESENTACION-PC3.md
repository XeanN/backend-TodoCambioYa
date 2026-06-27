# Presentación PC3 — TeCambioYa

## Diapositiva 1 — Portada
- Proyecto: TeCambioYa (Casa de cambio virtual Perú)
- Curso: Base de Datos
- Entrega: 22 Julio — PC3

## Diapositiva 2 — Objetivo
- Plataforma de cambio USD/PEN con trazabilidad completa
- PostgreSQL distribuido + MongoDB para datos no estructurados

## Diapositiva 3 — Diagrama E-R
- 14 tablas relacionales
- Ver: `docs/pc3/DIAGRAMA-ER.md` (exportar Mermaid a imagen)

## Diapositiva 4 — Tabla más detallada: `ordenes`
- 50 filas de demostración
- Campos: montos, tasas, estados, FKs a usuarios/cuentas/cupones
- Query: `SELECT COUNT(*) FROM ordenes;` → 50

## Diapositiva 5 — BD Distribuida
- 3 nodos PostgreSQL: Lima (5442), Arequipa (5443), Trujillo (5444)
- Replicación: catálogos | Fragmentación horizontal: por `region_id`

## Diapositiva 6 — Proceso 1: `registrar_orden_cambio`
- Inserta orden en estado `pendiente`
- Obtiene tasa vigente automáticamente

## Diapositiva 7 — Proceso 2: `procesar_orden`
- Bloqueo `FOR UPDATE` sobre la fila
- Cambia estado: pendiente → procesando → completado
- Genera comprobante

## Diapositiva 8 — Concurrencia: `aplicar_cupon`
- Cupón `PROMO-CONC` con 1 uso máximo
- 2 sesiones simultáneas: solo 1 gana
- Demo: `docs/pc3/demo-concurrencia.sql`

## Diapositiva 9 — MongoDB
- Colección: `notificaciones_push`
- Validaciones JSON Schema
- Índices compuestos
- Proyecciones y agregaciones

## Diapositiva 10 — Conclusión
- Modelo relacional + distribuido + NoSQL
- Procedimientos almacenados con control de concurrencia
