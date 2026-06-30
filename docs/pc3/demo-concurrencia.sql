-- ============================================================
-- PC3 — Demo de concurrencia (ejecutar en 2 sesiones pgAdmin)
-- Cupón PROMO-CONC tiene usos_maximos = 1
-- Solo una transacción debe tener éxito
-- ============================================================

-- Sesión 1:
-- BEGIN;
-- SELECT * FROM aplicar_cupon('PROMO-CONC', (SELECT id FROM ordenes WHERE estado = 'pendiente' LIMIT 1 OFFSET 0));
-- -- Esperar 5 segundos antes de COMMIT
-- COMMIT;

-- Sesión 2 (mientras Sesión 1 espera):
-- BEGIN;
-- SELECT * FROM aplicar_cupon('PROMO-CONC', (SELECT id FROM ordenes WHERE estado = 'pendiente' LIMIT 1 OFFSET 1));
-- COMMIT;

-- Resultado esperado:
--   Sesión 1: exito = true
--   Sesión 2: exito = false, mensaje = 'Cupón agotado (concurrencia detectada)'

-- Demo concurrencia en procesar_orden (misma orden, 2 sesiones):
-- Sesión 1: BEGIN; SELECT * FROM procesar_orden('ORD-LIM-0007'); -- usar orden pendiente real
-- Sesión 2: BEGIN; SELECT * FROM procesar_orden('ORD-LIM-0008'); -- misma orden
-- La segunda sesión espera el bloqueo FOR UPDATE y luego falla porque ya no está pendiente

-- Verificar 50 filas en ordenes:
-- SELECT COUNT(*) AS total_ordenes FROM ordenes;
-- SELECT numero_orden, monto_enviado, monto_recibido, tasa_aplicada, estado, creado_en FROM ordenes ORDER BY creado_en DESC;
