-- ============================================================
-- PC3 — Datos de demostración (sobre schema V1)
-- Tabla más detallada: ordenes (50 filas)
-- ============================================================

INSERT INTO tipos_cambio (moneda_origen, moneda_destino, tasa_compra, tasa_venta, tasa_preferencial, vigente_hasta)
VALUES ('USD', 'PEN', 3.7100, 3.7500, 3.7300, NOW() + INTERVAL '30 days');

INSERT INTO cupones (codigo, tipo, valor, usos_maximos, usos_actuales, monto_minimo, vence_en, activo) VALUES
    ('BIENVENIDA10', 'porcentaje', 0.1000, 100, 0, 100.00, NOW() + INTERVAL '90 days', TRUE),
    ('PROMO-CONC',   'pips',       0.0500,   1, 0, 200.00, NOW() + INTERVAL '30 days', TRUE);

INSERT INTO usuarios (nombre_completo, email, password_hash, tipo_cuenta, dni_ruc, telefono, region_id) VALUES
    ('Carlos Quispe Mendoza', 'carlos.quispe@gmail.com',  'hash_carlos',  'personal', '45678901',    '987654321', 1),
    ('Ana Torres Huanca',     'ana.torres@gmail.com',     'hash_ana',     'personal', '45678902',    '987654322', 1),
    ('Luis Mendoza Ríos',     'luis.mendoza@gmail.com',   'hash_luis',    'personal', '45678903',    '987654323', 1),
    ('María Vargas Silva',    'maria.vargas@gmail.com',   'hash_maria',   'personal', '45678904',    '987654324', 1),
    ('Empresa Lima SAC',      'contacto@empresalima.com', 'hash_empresa', 'corp',     '20512345678', '014445566', 1);

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 1, '19012345678901', '00219012345678901234', 'USD', 'BCP Dolares', TRUE
FROM usuarios u WHERE u.email = 'carlos.quispe@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 1, '19098765432101', '00219098765432101234', 'PEN', 'BCP Soles', TRUE
FROM usuarios u WHERE u.email = 'carlos.quispe@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 2, '89011111111101', '00289011111111101234', 'USD', 'Interbank Dolares', TRUE
FROM usuarios u WHERE u.email = 'ana.torres@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 2, '89022222222201', '00289022222222201234', 'PEN', 'Interbank Soles', TRUE
FROM usuarios u WHERE u.email = 'ana.torres@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 3, '01133333333301', '00201133333333301234', 'USD', 'BBVA Dolares', TRUE
FROM usuarios u WHERE u.email = 'luis.mendoza@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 3, '01144444444401', '00201144444444401234', 'PEN', 'BBVA Soles', TRUE
FROM usuarios u WHERE u.email = 'luis.mendoza@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 4, '02155555555501', '00202155555555501234', 'USD', 'Scotiabank Dolares', TRUE
FROM usuarios u WHERE u.email = 'maria.vargas@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 4, '02166666666601', '00202166666666601234', 'PEN', 'Scotiabank Soles', TRUE
FROM usuarios u WHERE u.email = 'maria.vargas@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 1, '19077777777701', '00219077777777701234', 'USD', 'Corp USD', TRUE
FROM usuarios u WHERE u.email = 'contacto@empresalima.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 1, '19088888888801', '00219088888888801234', 'PEN', 'Corp PEN', TRUE
FROM usuarios u WHERE u.email = 'contacto@empresalima.com';

-- 50 filas en ordenes (tabla más detallada del modelo)
INSERT INTO ordenes (
    numero_orden, usuario_id, tipo_cambio_id, cuenta_origen_id, cuenta_destino_id,
    monto_enviado, monto_recibido, tasa_aplicada, estado, region_id, creado_en, completado_en
)
SELECT
    'ORD-LIM-' || LPAD(gs.n::TEXT, 4, '0'),
    u.id,
    tc.id,
    co.id,
    cd.id,
    (100 + (gs.n * 47) % 4900)::DECIMAL(15,2),
    CASE
        WHEN gs.n % 10 IN (0, 1, 2, 3, 4, 5, 6) THEN ROUND(((100 + (gs.n * 47) % 4900) * 3.7300)::NUMERIC, 2)
        ELSE NULL
    END,
    3.7300,
    CASE gs.n % 10
        WHEN 0 THEN 'completado'
        WHEN 1 THEN 'completado'
        WHEN 2 THEN 'completado'
        WHEN 3 THEN 'completado'
        WHEN 4 THEN 'completado'
        WHEN 5 THEN 'completado'
        WHEN 6 THEN 'completado'
        WHEN 7 THEN 'pendiente'
        WHEN 8 THEN 'procesando'
        ELSE 'cancelado'
    END,
    1,
    NOW() - (gs.n || ' days')::INTERVAL,
    CASE WHEN gs.n % 10 <= 6 THEN NOW() - (gs.n || ' days')::INTERVAL + INTERVAL '2 hours' ELSE NULL END
FROM generate_series(1, 50) AS gs(n)
CROSS JOIN LATERAL (
    SELECT id FROM tipos_cambio WHERE moneda_origen = 'USD' ORDER BY registrado_en DESC LIMIT 1
) tc
CROSS JOIN LATERAL (
    SELECT id FROM usuarios WHERE region_id = 1 ORDER BY email OFFSET (gs.n % 5) LIMIT 1
) u
JOIN cuentas_bancarias co ON co.usuario_id = u.id AND co.moneda = 'USD'
JOIN cuentas_bancarias cd ON cd.usuario_id = u.id AND cd.moneda = 'PEN';

INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
SELECT o.id, 'COMP-LIM-' || LPAD(ROW_NUMBER() OVER (ORDER BY o.creado_en)::TEXT, 4, '0'), 'recibo'
FROM ordenes o
WHERE o.estado = 'completado';

INSERT INTO notificaciones (usuario_id, tipo, titulo, contenido, leida)
SELECT u.id, 'orden', 'Orden registrada', 'Se registró la orden ' || o.numero_orden || ' por USD ' || o.monto_enviado, FALSE
FROM ordenes o
JOIN usuarios u ON u.id = o.usuario_id
WHERE o.numero_orden IN ('ORD-LIM-0001', 'ORD-LIM-0002', 'ORD-LIM-0003');

INSERT INTO auditoria_sesiones (usuario_id, ip_address, user_agent, accion, region_id)
SELECT u.id, '190.235.10.1', 'Mozilla/5.0 Chrome/120', 'login', 1
FROM usuarios u WHERE u.email = 'carlos.quispe@gmail.com';


----------------------------------------------
--------------------------------------------

-- ============================================================
-- LIMA — Solo lo que falta (comprobantes, notif, auditoria, V2, V3)
-- Correr en: todocambioya_lima
-- ============================================================

-- Comprobantes de las 2 órdenes que ya existen
INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
SELECT id, 'COMP-LIM-0000', 'recibo' FROM ordenes WHERE numero_orden = 'ORD-LIM-0001'
ON CONFLICT DO NOTHING;

INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
SELECT id, 'COMP-LIM-0002', 'recibo' FROM ordenes WHERE numero_orden = 'ORD-LIM-0002'
ON CONFLICT DO NOTHING;

-- Notificación para Carlos
INSERT INTO notificaciones (usuario_id, tipo, titulo, contenido, leida)
SELECT u.id, 'orden', 'Orden completada', 'Tu cambio de USD 1000.00 fue procesado exitosamente.', FALSE
FROM usuarios u WHERE u.email = 'carlos.quispe@gmail.com'
ON CONFLICT DO NOTHING;

-- Auditoría para Carlos
INSERT INTO auditoria_sesiones (usuario_id, ip_address, user_agent, accion, region_id)
SELECT u.id, '190.235.10.1', 'Mozilla/5.0 Chrome/120', 'login', 1
FROM usuarios u WHERE u.email = 'carlos.quispe@gmail.com';

-- ============================================================
-- V2 — Datos de demostración (PC3): 50 filas en ordenes
-- ============================================================

-- Tipo de cambio con vigencia para los procedimientos
INSERT INTO tipos_cambio (moneda_origen, moneda_destino, tasa_compra, tasa_venta, tasa_preferencial, vigente_hasta)
VALUES ('USD', 'PEN', 3.7100, 3.7500, 3.7300, NOW() + INTERVAL '30 days');

-- Cupones
INSERT INTO cupones (codigo, tipo, valor, usos_maximos, usos_actuales, monto_minimo, vence_en, activo) VALUES
    ('BIENVENIDA10', 'porcentaje', 0.1000, 100, 0, 100.00, NOW() + INTERVAL '90 days', TRUE),
    ('PROMO-CONC',   'pips',       0.0500,   1, 0, 200.00, NOW() + INTERVAL '30 days', TRUE)
ON CONFLICT DO NOTHING;

-- Usuarios adicionales (Luis y María — Carlos, Ana y Empresa ya existen)
INSERT INTO usuarios (nombre_completo, email, password_hash, tipo_cuenta, dni_ruc, telefono, region_id)
VALUES
    ('Luis Mendoza Ríos',  'luis.mendoza@gmail.com',  'hash_luis',  'personal', '45678903', '987654323', 1),
    ('María Vargas Silva', 'maria.vargas@gmail.com',  'hash_maria', 'personal', '45678904', '987654324', 1)
ON CONFLICT (email) DO NOTHING;

-- Cuentas bancarias para Luis Mendoza
INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 3, '01133333333301', '00201133333333301234', 'USD', 'BBVA Dolares', TRUE
FROM usuarios u WHERE u.email = 'luis.mendoza@gmail.com'
ON CONFLICT DO NOTHING;

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 3, '01144444444401', '00201144444444401234', 'PEN', 'BBVA Soles', TRUE
FROM usuarios u WHERE u.email = 'luis.mendoza@gmail.com'
ON CONFLICT DO NOTHING;

-- Cuentas bancarias para María Vargas
INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 4, '02155555555501', '00202155555555501234', 'USD', 'Scotiabank Dolares', TRUE
FROM usuarios u WHERE u.email = 'maria.vargas@gmail.com'
ON CONFLICT DO NOTHING;

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 4, '02166666666601', '00202166666666601234', 'PEN', 'Scotiabank Soles', TRUE
FROM usuarios u WHERE u.email = 'maria.vargas@gmail.com'
ON CONFLICT DO NOTHING;

-- Cuentas para Empresa Lima SAC
INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 1, '19077777777701', '00219077777777701234', 'USD', 'Corp USD', TRUE
FROM usuarios u WHERE u.email = 'contacto@empresalima.com'
ON CONFLICT DO NOTHING;

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 1, '19088888888801', '00219088888888801234', 'PEN', 'Corp PEN', TRUE
FROM usuarios u WHERE u.email = 'contacto@empresalima.com'
ON CONFLICT DO NOTHING;

-- 50 filas en ordenes
INSERT INTO ordenes (
    numero_orden, usuario_id, tipo_cambio_id, cuenta_origen_id, cuenta_destino_id,
    monto_enviado, monto_recibido, tasa_aplicada, estado, region_id, creado_en, completado_en
)
SELECT
    'ORD-LIM-' || LPAD(gs.n::TEXT, 4, '0'),
    u.id,
    tc.id,
    co.id,
    cd.id,
    (100 + (gs.n * 47) % 4900)::DECIMAL(15,2),
    CASE
        WHEN gs.n % 10 IN (0,1,2,3,4,5,6)
        THEN ROUND(((100 + (gs.n * 47) % 4900) * 3.7300)::NUMERIC, 2)
        ELSE NULL
    END,
    3.7300,
    CASE gs.n % 10
        WHEN 0 THEN 'completado'
        WHEN 1 THEN 'completado'
        WHEN 2 THEN 'completado'
        WHEN 3 THEN 'completado'
        WHEN 4 THEN 'completado'
        WHEN 5 THEN 'completado'
        WHEN 6 THEN 'completado'
        WHEN 7 THEN 'pendiente'
        WHEN 8 THEN 'procesando'
        ELSE        'cancelado'
    END,
    1,
    NOW() - (gs.n || ' days')::INTERVAL,
    CASE WHEN gs.n % 10 <= 6
         THEN NOW() - (gs.n || ' days')::INTERVAL + INTERVAL '2 hours'
         ELSE NULL END
FROM generate_series(1, 60) AS gs(n)
CROSS JOIN LATERAL (
    SELECT id FROM tipos_cambio
    WHERE moneda_origen = 'USD' AND (vigente_hasta IS NULL OR vigente_hasta > NOW())
    ORDER BY registrado_en DESC LIMIT 1
) tc
CROSS JOIN LATERAL (
    SELECT id FROM usuarios WHERE region_id = 1 ORDER BY email OFFSET (gs.n % 5) LIMIT 1
) u
JOIN cuentas_bancarias co ON co.usuario_id = u.id AND co.moneda = 'USD'
JOIN cuentas_bancarias cd ON cd.usuario_id = u.id AND cd.moneda = 'PEN'
ON CONFLICT (numero_orden) DO NOTHING;

-- Comprobantes para las órdenes completadas del generate_series
INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
SELECT o.id,
       'COMP-LIM-' || LPAD(ROW_NUMBER() OVER (ORDER BY o.creado_en)::TEXT, 4, '0'),
       'recibo'
FROM ordenes o
WHERE o.estado = 'completado'
  AND o.numero_orden LIKE 'ORD-LIM-%'
  AND NOT EXISTS (SELECT 1 FROM comprobantes c WHERE c.orden_id = o.id);

-- Notificaciones para las primeras 3 órdenes del generate_series
INSERT INTO notificaciones (usuario_id, tipo, titulo, contenido, leida)
SELECT u.id, 'orden', 'Orden registrada',
       'Se registró la orden ' || o.numero_orden || ' por USD ' || o.monto_enviado, FALSE
FROM ordenes o
JOIN usuarios u ON u.id = o.usuario_id
WHERE o.numero_orden IN ('ORD-LIM-0001', 'ORD-LIM-0002', 'ORD-LIM-0003')
ON CONFLICT DO NOTHING;

-- ============================================================
-- V3 — Procedimientos almacenados
-- ============================================================

CREATE SEQUENCE IF NOT EXISTS seq_numero_orden START 1;

CREATE OR REPLACE FUNCTION registrar_orden_cambio(
    p_usuario_id        UUID,
    p_monto_enviado     DECIMAL(15,2),
    p_cuenta_origen_id  UUID,
    p_cuenta_destino_id UUID,
    p_region_id         INTEGER DEFAULT 1
)
RETURNS TABLE(orden_id UUID, numero_orden VARCHAR, estado VARCHAR) AS $$
DECLARE
    v_tipo_cambio_id UUID;
    v_tasa           DECIMAL(10,4);
    v_numero         VARCHAR;
    v_orden_id       UUID;
BEGIN
    SELECT id, COALESCE(tasa_preferencial, tasa_venta)
    INTO v_tipo_cambio_id, v_tasa
    FROM tipos_cambio
    WHERE moneda_origen = 'USD' AND moneda_destino = 'PEN'
      AND (vigente_hasta IS NULL OR vigente_hasta > NOW())
    ORDER BY registrado_en DESC LIMIT 1;

    IF v_tipo_cambio_id IS NULL THEN
        RAISE EXCEPTION 'No hay tipo de cambio vigente';
    END IF;

    v_numero := 'ORD-AUTO-' || TO_CHAR(NOW(), 'YYYYMMDD') || '-'
                || LPAD(NEXTVAL('seq_numero_orden')::TEXT, 4, '0');

    INSERT INTO ordenes (
        numero_orden, usuario_id, tipo_cambio_id,
        cuenta_origen_id, cuenta_destino_id,
        monto_enviado, tasa_aplicada, estado, region_id
    ) VALUES (
        v_numero, p_usuario_id, v_tipo_cambio_id,
        p_cuenta_origen_id, p_cuenta_destino_id,
        p_monto_enviado, v_tasa, 'pendiente', p_region_id
    ) RETURNING id INTO v_orden_id;

    RETURN QUERY SELECT v_orden_id, v_numero, 'pendiente'::VARCHAR;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION procesar_orden(p_numero_orden VARCHAR)
RETURNS TABLE(orden_id UUID, estado_final VARCHAR, monto_recibido DECIMAL) AS $$
DECLARE
    v_orden RECORD;
    v_monto DECIMAL(15,2);
BEGIN
    SELECT o.id, o.estado, o.monto_enviado, o.tasa_aplicada
    INTO v_orden
    FROM ordenes o
    WHERE o.numero_orden = p_numero_orden
    FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Orden % no encontrada', p_numero_orden;
    END IF;

    IF v_orden.estado <> 'pendiente' THEN
        RAISE EXCEPTION 'Orden % no está pendiente (estado: %)', p_numero_orden, v_orden.estado;
    END IF;

    UPDATE ordenes SET estado = 'procesando' WHERE id = v_orden.id;
    PERFORM pg_sleep(0.5);
    v_monto := ROUND((v_orden.monto_enviado * v_orden.tasa_aplicada)::NUMERIC, 2);

    UPDATE ordenes
    SET estado = 'completado', monto_recibido = v_monto, completado_en = NOW()
    WHERE id = v_orden.id;

    INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
    VALUES (v_orden.id, 'COMP-' || p_numero_orden, 'recibo');

    RETURN QUERY SELECT v_orden.id, 'completado'::VARCHAR, v_monto;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION aplicar_cupon(p_codigo_cupon VARCHAR, p_orden_id UUID)
RETURNS TABLE(exito BOOLEAN, mensaje TEXT) AS $$
DECLARE
    v_cupon RECORD;
    v_orden RECORD;
BEGIN
    SELECT * INTO v_cupon FROM cupones
    WHERE codigo = p_codigo_cupon AND activo = TRUE FOR UPDATE;

    IF NOT FOUND THEN
        RETURN QUERY SELECT FALSE, 'Cupón no encontrado o inactivo'::TEXT; RETURN;
    END IF;

    IF v_cupon.vence_en IS NOT NULL AND v_cupon.vence_en < NOW() THEN
        RETURN QUERY SELECT FALSE, 'Cupón vencido'::TEXT; RETURN;
    END IF;

    IF v_cupon.usos_actuales >= v_cupon.usos_maximos THEN
        RETURN QUERY SELECT FALSE, 'Cupón agotado (concurrencia detectada)'::TEXT; RETURN;
    END IF;

    SELECT * INTO v_orden FROM ordenes WHERE id = p_orden_id FOR UPDATE;

    IF NOT FOUND THEN
        RETURN QUERY SELECT FALSE, 'Orden no encontrada'::TEXT; RETURN;
    END IF;

    IF v_orden.monto_enviado < COALESCE(v_cupon.monto_minimo, 0) THEN
        RETURN QUERY SELECT FALSE, 'Monto mínimo no alcanzado'::TEXT; RETURN;
    END IF;

    UPDATE cupones SET usos_actuales = usos_actuales + 1 WHERE id = v_cupon.id;
    UPDATE ordenes SET cupon_id = v_cupon.id WHERE id = p_orden_id;

    RETURN QUERY SELECT TRUE, ('Cupón ' || p_codigo_cupon || ' aplicado correctamente')::TEXT;
END;
$$ LANGUAGE plpgsql;



-----------------------------------------------
-----------------------------------------------

-- ============================================================
-- AREQUIPA — Solo datos faltantes (usuarios y operaciones)
-- Correr en: todocambioya_arequipa
-- ============================================================

INSERT INTO usuarios (nombre_completo, email, password_hash, tipo_cuenta, dni_ruc, telefono, region_id)
VALUES
    ('Maria Flores Ccopa',  'maria.flores@gmail.com',   'hash_maria',   'personal', '45123456', '954321098', 2),
    ('Roberto Zuniga Rios', 'roberto.zuniga@gmail.com', 'hash_roberto', 'personal', '45123457', '954321099', 2)
ON CONFLICT (email) DO NOTHING;

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 2, '89012345678901', '00289012345678901234', 'USD', 'Interbank Dolares', TRUE
FROM usuarios u WHERE u.email = 'maria.flores@gmail.com'
AND NOT EXISTS (SELECT 1 FROM cuentas_bancarias cb WHERE cb.usuario_id = u.id AND cb.moneda = 'USD');

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 2, '89098765432101', '00289098765432101234', 'PEN', 'Interbank Soles', TRUE
FROM usuarios u WHERE u.email = 'maria.flores@gmail.com'
AND NOT EXISTS (SELECT 1 FROM cuentas_bancarias cb WHERE cb.usuario_id = u.id AND cb.moneda = 'PEN');

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 5, '50011111111101', '00550011111111101234', 'USD', 'Caja Arequipa Dolares', TRUE
FROM usuarios u WHERE u.email = 'roberto.zuniga@gmail.com'
AND NOT EXISTS (SELECT 1 FROM cuentas_bancarias cb WHERE cb.usuario_id = u.id AND cb.moneda = 'USD');

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 5, '50022222222201', '00550022222222201234', 'PEN', 'Caja Arequipa Soles', TRUE
FROM usuarios u WHERE u.email = 'roberto.zuniga@gmail.com'
AND NOT EXISTS (SELECT 1 FROM cuentas_bancarias cb WHERE cb.usuario_id = u.id AND cb.moneda = 'PEN');

INSERT INTO ordenes (numero_orden, usuario_id, tipo_cambio_id, cuenta_origen_id, cuenta_destino_id, monto_enviado, monto_recibido, tasa_aplicada, estado, region_id)
SELECT 'ORD-AQP-0001', u.id, t.id, co.id, cd.id, 500.00, 1865.00, 3.7300, 'completado', 2
FROM usuarios u
JOIN tipos_cambio t ON t.moneda_origen = 'USD'
JOIN cuentas_bancarias co ON co.usuario_id = u.id AND co.moneda = 'USD'
JOIN cuentas_bancarias cd ON cd.usuario_id = u.id AND cd.moneda = 'PEN'
WHERE u.email = 'maria.flores@gmail.com'
ON CONFLICT (numero_orden) DO NOTHING;

INSERT INTO ordenes (numero_orden, usuario_id, tipo_cambio_id, cuenta_origen_id, cuenta_destino_id, monto_enviado, monto_recibido, tasa_aplicada, estado, region_id)
SELECT 'ORD-AQP-0002', u.id, t.id, co.id, cd.id, 300.00, 1119.00, 3.7300, 'completado', 2
FROM usuarios u
JOIN tipos_cambio t ON t.moneda_origen = 'USD'
JOIN cuentas_bancarias co ON co.usuario_id = u.id AND co.moneda = 'USD'
JOIN cuentas_bancarias cd ON cd.usuario_id = u.id AND cd.moneda = 'PEN'
WHERE u.email = 'roberto.zuniga@gmail.com'
ON CONFLICT (numero_orden) DO NOTHING;

INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
SELECT id, 'COMP-AQP-0001', 'recibo' FROM ordenes WHERE numero_orden = 'ORD-AQP-0001'
ON CONFLICT DO NOTHING;

INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
SELECT id, 'COMP-AQP-0002', 'recibo' FROM ordenes WHERE numero_orden = 'ORD-AQP-0002'
ON CONFLICT DO NOTHING;

INSERT INTO notificaciones (usuario_id, tipo, titulo, contenido, leida)
SELECT u.id, 'orden', 'Orden completada', 'Tu cambio de USD 500.00 fue procesado exitosamente.', FALSE
FROM usuarios u WHERE u.email = 'maria.flores@gmail.com';

INSERT INTO auditoria_sesiones (usuario_id, ip_address, user_agent, accion, region_id)
SELECT u.id, '190.235.20.1', 'Mozilla/5.0 Chrome/120', 'login', 2
FROM usuarios u WHERE u.email = 'maria.flores@gmail.com';