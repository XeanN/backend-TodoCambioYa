-- ============================================================
-- TECAMBIOYA — Fase 2: Nodo TRUJILLO (puerto 5444)
-- Base de datos: todocambioya_trujillo
--
-- CONTIENE:
--   [REPLICADAS]   Catálogos globales — copia idéntica en los 3 nodos
--   [FRAGMENTADAS] Solo filas donde region_id = 3 (Trujillo)
--                  Cubre: Trujillo, Piura, Chiclayo, Cajamarca
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
-- TABLAS FRAGMENTADAS — solo filas con region_id = 3 (Trujillo)
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
    CONSTRAINT chk_region_trujillo CHECK (region_id = 3)
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
    CONSTRAINT chk_orden_region_trujillo CHECK (region_id = 3)
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
    CONSTRAINT chk_auditoria_region_trujillo CHECK (region_id = 3)
);

-- ============================================================
-- ÍNDICES
-- ============================================================
CREATE INDEX idx_usuarios_email       ON usuarios(email);
CREATE INDEX idx_ordenes_usuario      ON ordenes(usuario_id);
CREATE INDEX idx_ordenes_estado       ON ordenes(estado);
CREATE INDEX idx_ordenes_creado       ON ordenes(creado_en DESC);
CREATE INDEX idx_tipos_cambio_vigente ON tipos_cambio(vigente_hasta);
CREATE INDEX idx_cuentas_usuario      ON cuentas_bancarias(usuario_id);
CREATE INDEX idx_notif_usuario_leida  ON notificaciones(usuario_id, leida);

-- ============================================================
-- SEED — catálogos globales (igual en los 3 nodos)
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
-- DATOS DE PRUEBA — usuarios y operaciones de Trujillo
-- ============================================================
INSERT INTO usuarios (nombre_completo, email, password_hash, tipo_cuenta, dni_ruc, telefono, region_id) VALUES
    ('Luis Chavez Vega',     'luis.chavez@gmail.com',   'hash_luis',   'personal', '48123456', '944321098', 3),
    ('Carmen Ruiz Castillo', 'carmen.ruiz@gmail.com',   'hash_carmen', 'personal', '48123457', '944321099', 3);

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 3, '01112345678901', '00301112345678901234', 'USD', 'BBVA Dolares', TRUE
FROM usuarios u WHERE u.email = 'luis.chavez@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 3, '01198765432101', '00301198765432101234', 'PEN', 'BBVA Soles', TRUE
FROM usuarios u WHERE u.email = 'luis.chavez@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 4, '40011111111101', '00440011111111101234', 'USD', 'Scotiabank Dolares', TRUE
FROM usuarios u WHERE u.email = 'carmen.ruiz@gmail.com';

INSERT INTO cuentas_bancarias (usuario_id, banco_id, numero_cuenta, cci, moneda, alias, verificada)
SELECT u.id, 4, '40022222222201', '00440022222222201234', 'PEN', 'Scotiabank Soles', TRUE
FROM usuarios u WHERE u.email = 'carmen.ruiz@gmail.com';

INSERT INTO ordenes (numero_orden, usuario_id, tipo_cambio_id, cuenta_origen_id, cuenta_destino_id, monto_enviado, monto_recibido, tasa_aplicada, estado, region_id)
SELECT 'ORD-TRU-0001', u.id, t.id, co.id, cd.id, 750.00, 2797.50, 3.7300, 'completado', 3
FROM usuarios u
JOIN tipos_cambio t ON t.moneda_origen = 'USD'
JOIN cuentas_bancarias co ON co.usuario_id = u.id AND co.moneda = 'USD'
JOIN cuentas_bancarias cd ON cd.usuario_id = u.id AND cd.moneda = 'PEN'
WHERE u.email = 'luis.chavez@gmail.com';

INSERT INTO ordenes (numero_orden, usuario_id, tipo_cambio_id, cuenta_origen_id, cuenta_destino_id, monto_enviado, monto_recibido, tasa_aplicada, estado, region_id)
SELECT 'ORD-TRU-0002', u.id, t.id, co.id, cd.id, 200.00, 746.00, 3.7300, 'pendiente', 3
FROM usuarios u
JOIN tipos_cambio t ON t.moneda_origen = 'USD'
JOIN cuentas_bancarias co ON co.usuario_id = u.id AND co.moneda = 'USD'
JOIN cuentas_bancarias cd ON cd.usuario_id = u.id AND cd.moneda = 'PEN'
WHERE u.email = 'carmen.ruiz@gmail.com';

INSERT INTO comprobantes (orden_id, numero_comprobante, tipo)
SELECT id, 'COMP-TRU-0001', 'recibo' FROM ordenes WHERE numero_orden = 'ORD-TRU-0001';

INSERT INTO notificaciones (usuario_id, tipo, titulo, contenido, leida)
SELECT u.id, 'orden', 'Orden completada', 'Tu cambio de USD 750.00 fue procesado exitosamente.', FALSE
FROM usuarios u WHERE u.email = 'luis.chavez@gmail.com';

INSERT INTO notificaciones (usuario_id, tipo, titulo, contenido, leida)
SELECT u.id, 'orden', 'Orden en proceso', 'Tu cambio de USD 200.00 está siendo procesado.', FALSE
FROM usuarios u WHERE u.email = 'carmen.ruiz@gmail.com';

INSERT INTO auditoria_sesiones (usuario_id, ip_address, user_agent, accion, region_id)
SELECT u.id, '190.235.30.1', 'Mozilla/5.0 Chrome/120', 'login', 3
FROM usuarios u WHERE u.email = 'luis.chavez@gmail.com';