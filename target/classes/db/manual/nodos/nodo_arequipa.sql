-- ============================================================
-- TECAMBIOYA ÔÇö Fase 2: Nodo AREQUIPA (puerto 5443)
-- Base de datos: todocambioya_arequipa
--
-- CONTIENE:
--   [REPLICADAS]   Cat├ílogos globales ÔÇö copia id├®ntica en los 3 nodos
--   [FRAGMENTADAS] Solo filas donde region_id = 2 (Arequipa)
--                  Cubre: Arequipa, Cusco, Puno, Moquegua, Tacna
-- ============================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================================
-- TABLAS REPLICADAS
-- ============================================================

CREATE TABLE regiones (
    id        SERIAL PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    codigo    VARCHAR(10) UNIQUE NOT NULL,
    nodo_db   VARCHAR,
    activo    BOOLEAN DEFAULT TRUE
);

CREATE TABLE bancos (
    id          SERIAL PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    codigo_bcp  VARCHAR(10),
    tipo        VARCHAR(15) CHECK (tipo IN ('banco', 'caja_mun')),
    logo_url    VARCHAR,
    activo      BOOLEAN DEFAULT TRUE
);

CREATE TABLE tipos_cambio (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    moneda_origen      VARCHAR(3) NOT NULL,
    moneda_destino     VARCHAR(3) NOT NULL,
    tasa_compra        DECIMAL(10,4) NOT NULL,
    tasa_venta         DECIMAL(10,4) NOT NULL,
    tasa_preferencial  DECIMAL(10,4),
    registrado_en      TIMESTAMP DEFAULT NOW(),
    vigente_hasta      TIMESTAMP
);

CREATE TABLE cupones (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo         VARCHAR(30) UNIQUE NOT NULL,
    tipo           VARCHAR(15) CHECK (tipo IN ('pips', 'porcentaje')),
    valor          DECIMAL(10,4) NOT NULL,
    usos_maximos   INTEGER NOT NULL,
    usos_actuales  INTEGER DEFAULT 0,
    monto_minimo   DECIMAL(15,2),
    vence_en       TIMESTAMP,
    activo         BOOLEAN DEFAULT TRUE
);

CREATE TABLE empresas (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ruc               VARCHAR(11) UNIQUE NOT NULL,
    razon_social      VARCHAR(200) NOT NULL,
    admin_usuario_id  UUID,
    region_id         INTEGER REFERENCES regiones(id),
    activo            BOOLEAN DEFAULT TRUE,
    creado_en         TIMESTAMP DEFAULT NOW()
);

CREATE TABLE usuarios_empresa (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    empresa_id           UUID NOT NULL REFERENCES empresas(id),
    usuario_id           UUID NOT NULL,
    rol                  VARCHAR(10) CHECK (rol IN ('admin', 'operador', 'lector')),
    puede_operar         BOOLEAN DEFAULT FALSE,
    puede_ver_historial  BOOLEAN DEFAULT FALSE,
    creado_en            TIMESTAMP DEFAULT NOW()
);

CREATE TABLE referidos (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    referidor_id       UUID NOT NULL,
    referido_id        UUID NOT NULL,
    cupon_generado_id  UUID REFERENCES cupones(id),
    completado         BOOLEAN DEFAULT FALSE,
    pips_ganados       DECIMAL(10,4) DEFAULT 0,
    creado_en          TIMESTAMP DEFAULT NOW()
);

-- ============================================================
-- TABLAS FRAGMENTADAS ÔÇö solo filas con region_id = 2 (Arequipa)
-- ============================================================

CREATE TABLE usuarios (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre_completo  VARCHAR(150) NOT NULL,
    email            VARCHAR UNIQUE NOT NULL,
    password_hash    VARCHAR NOT NULL,
    tipo_cuenta      VARCHAR(10) CHECK (tipo_cuenta IN ('personal', 'corp')),
    dni_ruc          VARCHAR(20),
    telefono         VARCHAR(15),
    region_id        INTEGER REFERENCES regiones(id),
    activo           BOOLEAN DEFAULT TRUE,
    creado_en        TIMESTAMP DEFAULT NOW(),
    actualizado_en   TIMESTAMP DEFAULT NOW(),
    CONSTRAINT chk_region_arequipa CHECK (region_id = 2)
);

CREATE TABLE cuentas_bancarias (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id      UUID NOT NULL REFERENCES usuarios(id),
    banco_id        INTEGER NOT NULL REFERENCES bancos(id),
    numero_cuenta   VARCHAR(30) NOT NULL,
    cci             VARCHAR(30),
    moneda          VARCHAR(3) NOT NULL,
    alias           VARCHAR(50),
    verificada      BOOLEAN DEFAULT FALSE,
    creado_en       TIMESTAMP DEFAULT NOW()
);

CREATE TABLE alertas_tipo_cambio (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id     UUID NOT NULL REFERENCES usuarios(id),
    moneda         VARCHAR(3) NOT NULL,
    tasa_objetivo  DECIMAL(10,4) NOT NULL,
    condicion      VARCHAR(10) CHECK (condicion IN ('mayor', 'menor')),
    canal_notif    VARCHAR(10) CHECK (canal_notif IN ('email', 'sms', 'push')),
    disparada      BOOLEAN DEFAULT FALSE,
    creado_en      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE ordenes (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    numero_orden       VARCHAR UNIQUE NOT NULL,
    usuario_id         UUID NOT NULL REFERENCES usuarios(id),
    tipo_cambio_id     UUID NOT NULL REFERENCES tipos_cambio(id),
    cuenta_origen_id   UUID NOT NULL REFERENCES cuentas_bancarias(id),
    cuenta_destino_id  UUID NOT NULL REFERENCES cuentas_bancarias(id),
    monto_enviado      DECIMAL(15,2) NOT NULL,
    monto_recibido     DECIMAL(15,2),
    tasa_aplicada      DECIMAL(10,4) NOT NULL,
    estado             VARCHAR(15) CHECK (estado IN ('pendiente', 'procesando', 'completado', 'cancelado')),
    cupon_id           UUID REFERENCES cupones(id),
    region_id          INTEGER REFERENCES regiones(id),
    creado_en          TIMESTAMP DEFAULT NOW(),
    completado_en      TIMESTAMP,
    CONSTRAINT chk_orden_region_arequipa CHECK (region_id = 2)
);

CREATE TABLE comprobantes (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    orden_id            UUID NOT NULL REFERENCES ordenes(id),
    numero_comprobante  VARCHAR UNIQUE NOT NULL,
    tipo                VARCHAR(10) CHECK (tipo IN ('recibo', 'factura', 'boleta')),
    url_pdf             VARCHAR,
    generado_en         TIMESTAMP DEFAULT NOW()
);

CREATE TABLE notificaciones (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id  UUID NOT NULL REFERENCES usuarios(id),
    tipo        VARCHAR(15) CHECK (tipo IN ('orden', 'alerta', 'promo')),
    titulo      VARCHAR(200) NOT NULL,
    contenido   TEXT,
    leida       BOOLEAN DEFAULT FALSE,
    creado_en   TIMESTAMP DEFAULT NOW()
);

CREATE TABLE auditoria_sesiones (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id  UUID NOT NULL REFERENCES usuarios(id),
    ip_address  INET,
    user_agent  TEXT,
    accion      VARCHAR(15) CHECK (accion IN ('login', 'logout', 'operacion')),
    region_id   INTEGER REFERENCES regiones(id),
    creado_en   TIMESTAMP DEFAULT NOW(),
    CONSTRAINT chk_auditoria_region_arequipa CHECK (region_id = 2)
);

-- ============================================================
-- ├ìNDICES
-- ============================================================
CREATE INDEX idx_usuarios_email       ON usuarios(email);
CREATE INDEX idx_ordenes_usuario      ON ordenes(usuario_id);
CREATE INDEX idx_ordenes_estado       ON ordenes(estado);
CREATE INDEX idx_ordenes_creado       ON ordenes(creado_en DESC);
CREATE INDEX idx_tipos_cambio_vigente ON tipos_cambio(vigente_hasta);
CREATE INDEX idx_cuentas_usuario      ON cuentas_bancarias(usuario_id);
CREATE INDEX idx_notif_usuario_leida  ON notificaciones(usuario_id, leida);

-- ============================================================
-- SEED ÔÇö cat├ílogos globales (igual en los 3 nodos)
-- ============================================================
INSERT INTO regiones (id, nombre, codigo, nodo_db, activo) VALUES
    (1, 'Lima',     'LIM', 'db-lima:5442',     TRUE),
    (2, 'Arequipa', 'AQP', 'db-arequipa:5443', TRUE),
    (3, 'Trujillo', 'TRU', 'db-trujillo:5444', TRUE),
    (4, 'Cusco',    'CUS', 'db-cusco:5445',    FALSE),
    (5, 'Piura',    'PIU', 'db-piura:5446',    FALSE);

INSERT INTO bancos (nombre, codigo_bcp, tipo, activo) VALUES
    ('BCP',           'BCP', 'banco',    TRUE),
    ('Interbank',     'IBK', 'banco',    TRUE),
    ('BBVA',          'BBV', 'banco',    TRUE),
    ('Scotiabank',    'SCO', 'banco',    TRUE),
    ('Caja Arequipa', 'CAR', 'caja_mun', TRUE),
    ('Caja Huancayo', 'CHU', 'caja_mun', TRUE);

INSERT INTO tipos_cambio (moneda_origen, moneda_destino, tasa_compra, tasa_venta, tasa_preferencial)
VALUES ('USD', 'PEN', 3.7100, 3.7500, 3.7300);

-- ============================================================
-- DATOS DE PRUEBA ÔÇö usuarios y operaciones de Arequipa
-- ============================================================
INSERT INTO usuarios (nombre_completo, email, password_hash, tipo_cuenta, dni_ruc, telefono, region_id) VALUES
    ('Maria Flores Ccopa',   'maria.flores@gmail.com',   'hash_maria',   'personal', '45123456', '954321098', 2),
    ('Roberto Zuniga Rios',  'roberto.zuniga@gmail.com', 'hash_roberto', 'personal', '45123457', '954321099', 2);

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 2, '89012345678901', '00289012345678901234', 'USD', 'Interbank Dolares', TRUE
FROM usuarios u WHERE u.email = 'maria.flores@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 2, '89098765432101', '00289098765432101234', 'PEN', 'Interbank Soles', TRUE
FROM usuarios u WHERE u.email = 'maria.flores@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 5, '50011111111101', '00550011111111101234', 'USD', 'Caja Arequipa Dolares', TRUE
FROM usuarios u WHERE u.email = 'roberto.zuniga@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 5, '50022222222201', '00550022222222201234', 'PEN', 'Caja Arequipa Soles', TRUE
FROM usuarios u WHERE u.email = 'roberto.zuniga@gmail.com';

INSERT INTO ordenes (numero_orden, usuario_id, tipo_cambio_id, cuenta_origen_id, cuenta_destino_id, monto_enviado, monto_recibido, tasa_aplicada, estado, region_id)
SELECT 'ORD-AQP-0001', u.id, t.id, co.id, cd.id, 500.00, 1865.00, 3.7300, 'completado', 2
FROM usuarios u
JOIN tipos_cambio t ON t.moneda_origen = 'USD'
JOIN cuentas_bancarias co ON co.usuario_id = u.id AND co.moneda = 'USD'
JOIN cuentas_bancarias cd ON cd.usuario_id = u.id AND cd.moneda = 'PEN'
WHERE u.email = 'maria.flores@gmail.com';

INSERT INTO ordenes (numero_orden, usuario_id, tipo_cambio_id, cuenta_origen_id, cuenta_destino_id, monto_enviado, monto_recibido, tasa_aplicada, estado, region_id)
SELECT 'ORD-AQP-0002', u.id, t.id, co.id, cd.id, 300.00, 1119.00, 3.7300, 'completado', 2
FROM usuarios u
JOIN tipos_cambio t ON t.moneda_origen = 'USD'
JOIN cuentas_bancarias co ON co.usuario_id = u.id AND co.moneda = 'USD'
JOIN cuentas_bancarias cd ON cd.usuario_id = u.id AND cd.moneda = 'PEN'
WHERE u.email = 'roberto.zuniga@gmail.com';

INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
SELECT id, 'COMP-AQP-0001', 'recibo' FROM ordenes WHERE numero_orden = 'ORD-AQP-0001';

INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
SELECT id, 'COMP-AQP-0002', 'recibo' FROM ordenes WHERE numero_orden = 'ORD-AQP-0002';

INSERT INTO notificaciones (usuario_id, tipo, titulo, contenido, leida)
SELECT u.id, 'orden', 'Orden completada', 'Tu cambio de USD 500.00 fue procesado exitosamente.', FALSE
FROM usuarios u WHERE u.email = 'maria.flores@gmail.com';

INSERT INTO auditoria_sesiones (usuario_id, ip_address, user_agent, accion, region_id)
SELECT u.id, '190.235.20.1', 'Mozilla/5.0 Chrome/120', 'login', 2
FROM usuarios u WHERE u.email = 'maria.flores@gmail.com';
