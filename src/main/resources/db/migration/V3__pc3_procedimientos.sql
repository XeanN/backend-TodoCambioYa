-- ============================================================
-- PC3 — Procedimientos almacenados PostgreSQL
-- Proceso 1: registrar_orden_cambio
-- Proceso 2: procesar_orden (con concurrencia via FOR UPDATE)
-- Proceso 3: aplicar_cupon (concurrencia — SELECT FOR UPDATE)
-- ============================================================

CREATE SEQUENCE IF NOT EXISTS seq_numero_orden START 1;

-- PROCESO 1: Registrar una nueva orden de cambio
CREATE OR REPLACE FUNCTION registrar_orden_cambio(
    p_usuario_id       UUID,
    p_monto_enviado    DECIMAL(15,2),
    p_cuenta_origen_id UUID,
    p_cuenta_destino_id UUID,
    p_region_id        INTEGER DEFAULT 1
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
    ORDER BY registrado_en DESC
    LIMIT 1;

    IF v_tipo_cambio_id IS NULL THEN
        RAISE EXCEPTION 'No hay tipo de cambio vigente';
    END IF;

    v_numero := 'ORD-AUTO-' || TO_CHAR(NOW(), 'YYYYMMDD') || '-' || LPAD(NEXTVAL('seq_numero_orden')::TEXT, 4, '0');

    INSERT INTO ordenes (
        numero_orden, usuario_id, tipo_cambio_id, cuenta_origen_id, cuenta_destino_id,
        monto_enviado, tasa_aplicada, estado, region_id
    ) VALUES (
        v_numero, p_usuario_id, v_tipo_cambio_id, p_cuenta_origen_id, p_cuenta_destino_id,
        p_monto_enviado, v_tasa, 'pendiente', p_region_id
    )
    RETURNING id INTO v_orden_id;

    RETURN QUERY SELECT v_orden_id, v_numero, 'pendiente'::VARCHAR;
END;
$$ LANGUAGE plpgsql;

-- PROCESO 2: Procesar orden pendiente (demuestra bloqueo de fila)
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
        RAISE EXCEPTION 'Orden % no está pendiente (estado actual: %)', p_numero_orden, v_orden.estado;
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

-- PROCESO 3 (CONCURRENCIA): Aplicar cupón con bloqueo exclusivo
CREATE OR REPLACE FUNCTION aplicar_cupon(p_codigo_cupon VARCHAR, p_orden_id UUID)
RETURNS TABLE(exito BOOLEAN, mensaje TEXT) AS $$
DECLARE
    v_cupon RECORD;
    v_orden RECORD;
BEGIN
    SELECT * INTO v_cupon
    FROM cupones
    WHERE codigo = p_codigo_cupon AND activo = TRUE
    FOR UPDATE;

    IF NOT FOUND THEN
        RETURN QUERY SELECT FALSE, 'Cupón no encontrado o inactivo'::TEXT;
        RETURN;
    END IF;

    IF v_cupon.vence_en IS NOT NULL AND v_cupon.vence_en < NOW() THEN
        RETURN QUERY SELECT FALSE, 'Cupón vencido'::TEXT;
        RETURN;
    END IF;

    IF v_cupon.usos_actuales >= v_cupon.usos_maximos THEN
        RETURN QUERY SELECT FALSE, 'Cupón agotado (concurrencia detectada)'::TEXT;
        RETURN;
    END IF;

    SELECT * INTO v_orden FROM ordenes WHERE id = p_orden_id FOR UPDATE;

    IF NOT FOUND THEN
        RETURN QUERY SELECT FALSE, 'Orden no encontrada'::TEXT;
        RETURN;
    END IF;

    IF v_orden.monto_enviado < COALESCE(v_cupon.monto_minimo, 0) THEN
        RETURN QUERY SELECT FALSE, 'Monto mínimo no alcanzado'::TEXT;
        RETURN;
    END IF;

    UPDATE cupones SET usos_actuales = usos_actuales + 1 WHERE id = v_cupon.id;
    UPDATE ordenes SET cupon_id = v_cupon.id WHERE id = p_orden_id;

    RETURN QUERY SELECT TRUE, ('Cupón ' || p_codigo_cupon || ' aplicado correctamente')::TEXT;
END;
$$ LANGUAGE plpgsql;
