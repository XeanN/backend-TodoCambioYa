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
