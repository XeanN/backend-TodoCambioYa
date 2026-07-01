# TodoCambioYa — Guía de levantamiento del entorno

Este documento cubre dos escenarios:

1. **Con Docker** (flujo normal, recomendado).
2. **Sin Docker** (plan de emergencia para una demo en una PC donde Docker no funcione).

---

## 1. Levantar todo con Docker (flujo normal)

### Requisitos previos

- Docker Desktop instalado y **corriendo**.
- Java 24, Maven 3.9+ instalados (para correr el backend).
- Repo clonado y archivo `.env` creado a partir de `.env.example`:

```bash
cp .env.example .env
```

### Paso 1 — Levantar los contenedores

Desde la raíz del proyecto (`backend-TodoCambioYa`):

```bash
docker compose up -d
```

Esto levanta 5 servicios: `db-lima`, `db-arequipa`, `db-trujillo`, `pgadmin` y `mongodb`.

### Paso 2 — Verificar que todo esté corriendo

```bash
docker compose ps
```

Todos deben aparecer como `Up` o `healthy`. Revisa especialmente que `todocambioya_mongodb` muestre el puerto correcto mapeado (ej. `0.0.0.0:27018->27017/tcp` si ya cambiaste el puerto por el conflicto con tu Mongo nativo).

Si algo falla, revisa logs de un servicio puntual:

```bash
docker logs todocambioya_db_lima
docker logs todocambioya_mongodb
```

### Paso 3 — Conectar pgAdmin a los 3 nodos PostgreSQL

1. Abrir `http://localhost:5050`
2. Login: `admin@todocambioya.com` / `admin123`
3. Agregar 3 servidores manualmente:

| Servidor | Host | Puerto | Base de datos | Usuario | Password |
|---|---|---|---|---|---|
| TeCambioYa - Lima | `localhost` (o `db-lima` si te conectas desde dentro del contenedor pgAdmin) | `5442` | `todocambioya_lima` | `tcya_user` | `tcya_pass_local` |
| TeCambioYa - Arequipa | `localhost` | `5443` | `todocambioya_arequipa` | `tcya_user` | `tcya_pass_local` |
| TeCambioYa - Trujillo | `localhost` | `5444` | `todocambioya_trujillo` | `tcya_user` | `tcya_pass_local` |

> Nota: si conectas pgAdmin **desde dentro del contenedor** (vía su propia UI web), usa el nombre del servicio Docker como host (`db-lima`, `db-arequipa`, `db-trujillo`) y el puerto interno `5432`, no el puerto mapeado. Si conectas desde tu Compass/cliente externo en Windows, usa `localhost` y el puerto mapeado (`5442/5443/5444`).

### Paso 4 — Ejecutar los scripts de migración (primera vez)

En cada base, abrir el **Query Tool** (F5) y correr su script correspondiente:

```
todocambioya_lima     ← src/main/resources/db/migration/V2__nodo_lima.sql
todocambioya_arequipa ← src/main/resources/db/migration/V2__nodo_arequipa.sql
todocambioya_trujillo ← src/main/resources/db/migration/V2__nodo_trujillo.sql
```

> Flyway solo migra automáticamente el nodo Lima al arrancar el backend. Arequipa y Trujillo se inicializan manualmente.

### Paso 5 — Conectar MongoDB Compass

URI (ajusta el puerto si lo cambiaste por el conflicto con tu Mongo nativo):

```
mongodb://tcya_mongo:tcya_mongo_local@localhost:27018/?authSource=admin
```

Verificar colecciones:

```bash
docker exec -it todocambioya_mongodb mongosh -u tcya_mongo -p tcya_mongo_local --authenticationDatabase admin
use todocambioya_docs
show collections
```

### Paso 6 — Arrancar el backend

```bash
mvn spring-boot:run
```

Verificar: `http://localhost:8080/api/actuator/health`

### Apagar el entorno

Apagar sin perder datos:

```bash
docker compose down
```

Apagar y borrar todos los datos (reset completo):

```bash
docker compose down -v
```

---

## 2. Sin Docker — Plan de emergencia para demo en otra PC

Úsalo solo si en la PC de la demo Docker Desktop no está disponible o no funciona. Requiere instalar PostgreSQL 16 y MongoDB de forma nativa con anticipación (no el día de la demo).

### 2.1 — PostgreSQL nativo (simulando los 3 nodos)

El proyecto espera 3 "nodos" en puertos distintos (`5442`, `5443`, `5444`). Con una sola instalación nativa de PostgreSQL (que corre en el puerto estándar `5432`), tienes dos caminos:

**Opción simple (recomendada para una demo rápida):** crear las 3 bases de datos en la misma instancia y puerto, y ajustar el `application.yml` para que las 3 apunten a `localhost:5432`.

1. Instalar PostgreSQL 16 (incluye pgAdmin).
2. Crear las 3 bases de datos desde pgAdmin o por consola:

```sql
CREATE DATABASE todocambioya_lima;
CREATE DATABASE todocambioya_arequipa;
CREATE DATABASE todocambioya_trujillo;
```

3. Crear el usuario de la app si no existe:

```sql
CREATE USER tcya_user WITH PASSWORD 'tcya_pass_local';
GRANT ALL PRIVILEGES ON DATABASE todocambioya_lima TO tcya_user;
GRANT ALL PRIVILEGES ON DATABASE todocambioya_arequipa TO tcya_user;
GRANT ALL PRIVILEGES ON DATABASE todocambioya_trujillo TO tcya_user;
```

4. Ejecutar cada script de migración en su base correspondiente (Query Tool de pgAdmin), igual que en el flujo con Docker:

```
todocambioya_lima     ← V2__nodo_lima.sql
todocambioya_arequipa ← V2__nodo_arequipa.sql
todocambioya_trujillo ← V2__nodo_trujillo.sql
```

5. **Ajustar temporalmente `application.yml`** (perfil `dev`) para que las 3 conexiones usen el puerto `5432` en vez de `5442/5443/5444`:

```yaml
datasource:
  url: jdbc:postgresql://localhost:5432/todocambioya_lima
  ...

datasource-arequipa:
  url: jdbc:postgresql://localhost:5432/todocambioya_arequipa
  ...

datasource-trujillo:
  url: jdbc:postgresql://localhost:5432/todocambioya_trujillo
  ...
```

> Importante: no subas este cambio a Git. Es solo para la demo local. Puedes guardarlo en una rama aparte o como un `application-demo.yml` que actives con `--spring.profiles.active=demo`.

### 2.2 — MongoDB nativo

Si MongoDB ya está instalado de forma nativa en esa PC (corriendo en el puerto estándar `27017`):

1. Crear el usuario root vía `mongosh`:

```js
use admin
db.createUser({
  user: "tcya_mongo",
  pwd: "tcya_mongo_local",
  roles: [{ role: "root", db: "admin" }]
})
```

2. Crear la base y correr el script de inicialización manualmente si hace falta:

```bash
mongosh -u tcya_mongo -p tcya_mongo_local --authenticationDatabase admin todocambioya_docs src/main/resources/mongodb/init-collections.js
```

3. En este caso **no hace falta cambiar el puerto** en `application.yml`, porque el Mongo nativo ya usa el `27017` estándar que la app espera por defecto.

### 2.3 — Arrancar el backend en modo demo

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

(o el nombre de perfil que hayas usado para el `application-demo.yml` con los puertos nativos).

Verificar: `http://localhost:8080/api/actuator/health`

---

## Resumen rápido — qué usar cuándo

| Escenario | Qué levantar | Puertos |
|---|---|---|
| Desarrollo normal (tu PC) | `docker compose up -d` | Postgres 5442/5443/5444, Mongo 27018 |
| Demo en PC sin Docker | Postgres y Mongo nativos | Postgres 5432 (3 bases), Mongo 27017 |

**Antes de cualquier demo sin Docker:** prueba este flujo completo con anticipación en esa misma PC, no el mismo día. Instalar PostgreSQL/MongoDB nativos y crear las bases toma tiempo y conviene descartar errores con calma.
