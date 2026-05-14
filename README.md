# TeCambioYa — Backend

> Casa de cambio virtual para Perú (USD ↔ PEN)  
> Backend REST API construido con Java Spring Boot 3.2.5

---

## Índice

- [Descripción del proyecto](#descripción-del-proyecto)
- [Tecnologías](#tecnologías)
- [Arquitectura por fases](#arquitectura-por-fases)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Base de datos](#base-de-datos)
- [Instalación y configuración](#instalación-y-configuración)
- [Ejecución](#ejecución)
- [Perfiles de entorno](#perfiles-de-entorno)
- [Capas de la aplicación](#capas-de-la-aplicación)
- [Endpoints disponibles](#endpoints-disponibles)
- [Variables de entorno](#variables-de-entorno)

---

## Descripción del proyecto

TeCambioYa es una plataforma de cambio de divisas virtual orientada al mercado peruano. Permite a usuarios naturales y empresas realizar operaciones de compra/venta de dólares (USD) a soles (PEN) de forma segura, con tasas competitivas y trazabilidad completa de cada operación.

Este repositorio contiene únicamente el **backend** de la plataforma. El frontend React se encuentra en el repositorio [tecambioya-ui](https://github.com/n1ckoxd/tecambioya-ui).

---

## Tecnologías

| Tecnología | Versión | Rol |
|---|---|---|
| Java | 24 | Lenguaje base |
| Spring Boot | 3.2.5 | Framework principal |
| Spring Security | (gestionado por Boot) | Autenticación y autorización |
| Spring Data JPA | (gestionado por Boot) | ORM y acceso a datos |
| Hibernate | (gestionado por Boot) | Implementación JPA |
| PostgreSQL | 17 | Base de datos principal |
| Flyway | (gestionado por Boot) | Migraciones de base de datos |
| jjwt | 0.12.5 | Generación y validación de JWT |
| Maven | 3.9.15 | Gestión de dependencias y build |
| Docker | — | Contenedores para BD y servicios |

> **Nota:** El proyecto está desarrollado de forma **nativa**, sin uso de Lombok ni MapStruct. Todas las clases incluyen constructores, getters y setters escritos manualmente.

---

## Arquitectura por fases

El proyecto sigue una arquitectura de base de datos evolutiva definida por etapas:

```
Fase 1 (actual) ── PostgreSQL nodo único (Lima)
Fase 2          ── PostgreSQL distribuido (Lima + Arequipa + Trujillo)
Fase 3          ── MongoDB para datos no estructurados (chat, notificaciones push)
Fase 4          ── Cassandra para escrituras de alta velocidad (ticks de tipo de cambio)
Fase 5          ── Hadoop para analítica batch nocturna y reportes ejecutivos
```

Cada fase es **aditiva** — no reemplaza la anterior, sino que suma una nueva tecnología para un patrón de datos distinto.

---

## Estructura del proyecto

```
backend-TodoCambioYa/
├── src/
│   └── main/
│       ├── java/com/todocambioya/
│       │   ├── entity/              # 14 entidades JPA (mapeo a tablas PostgreSQL)
│       │   ├── repository/          # 14 interfaces Spring Data JPA
│       │   ├── dto/
│       │   │   ├── request/         # Objetos de entrada desde el frontend
│       │   │   └── response/        # Objetos de salida hacia el frontend
│       │   ├── service/             # Lógica de negocio (pendiente)
│       │   ├── controller/          # Endpoints REST (pendiente)
│       │   └── security/            # JWT + Spring Security (pendiente)
│       └── resources/
│           ├── db/migration/
│           │   └── V1__create_schema.sql   # Schema inicial (Flyway lo ejecuta solo)
│           └── application.yml             # Configuración de perfiles dev/prod
├── docker-compose.yml
├── .env.example
└── pom.xml
```

---

## Base de datos

### Esquema — 14 tablas

El schema completo está en `src/main/resources/db/migration/V1__create_schema.sql` y es ejecutado automáticamente por Flyway al arrancar la aplicación.

```
Grupo GEO          → regiones
Grupo CORE         → usuarios, empresas
Grupo ROLES        → usuarios_empresa
Grupo BANCO        → bancos, cuentas_bancarias
Grupo FX           → tipos_cambio, alertas_tipo_cambio
Grupo BENEFICIO    → cupones, referidos
Grupo TRANSACCIÓN  → ordenes, comprobantes
Grupo SISTEMA      → notificaciones, auditoria_sesiones
```

### Cadena principal de transacción

```
regiones → usuarios → cuentas_bancarias + tipos_cambio + cupones → ordenes → comprobantes
```

### Convenciones

- Tablas con datos de negocio usan `UUID` como PK generado por PostgreSQL con `gen_random_uuid()`
- Tablas de catálogo (`regiones`, `bancos`) usan `SERIAL` (entero autoincremental)
- Todas las fechas se almacenan en zona horaria `America/Lima`
- El campo `ip_address` de `auditoria_sesiones` usa tipo `INET` de PostgreSQL, mapeado como `String` en JPA

### Migraciones con Flyway

Flyway gestiona exclusivamente el esquema. Hibernate está configurado con `ddl-auto: validate` — solo verifica que las entidades coincidan con las tablas, nunca modifica la BD.

Los scripts de migración siguen la convención de nombres:

```
V1__create_schema.sql        ← Fase 1: schema inicial
V2__distribute_regions.sql   ← Fase 2: particionado (pendiente)
```

---

## Instalación y configuración

### Requisitos previos

- Java 24 instalado
- Maven 3.9+ instalado
- PostgreSQL 17 instalado (local o vía Docker)
- Docker Desktop (opcional, recomendado)

### Verificar instalaciones

```bash
java -version
mvn -version
docker --version
```

### Clonar el repositorio

```bash
git clone https://github.com/XeanN/backend-TodoCambioYa.git
cd backend-TodoCambioYa
```

### Configurar variables de entorno

```bash
cp .env.example .env
```

Edita el archivo `.env` con tus valores locales. En desarrollo los valores por defecto del `application.yml` son suficientes.

### Crear la base de datos (sin Docker)

Si usas PostgreSQL local (pgAdmin o psql):

```sql
CREATE DATABASE tecambioya;
```

Luego ajusta el `application.yml` para apuntar al nombre correcto:

```yaml
datasource:
  url: jdbc:postgresql://localhost:5432/tecambioya
```

---

## Ejecución

### Opción A — Sin Docker (PostgreSQL local)

```bash
# 1. Asegúrate de que PostgreSQL esté corriendo localmente
# 2. Compila el proyecto
mvn compile

# 3. Arranca Spring Boot (Flyway ejecuta V1__create_schema.sql automáticamente)
mvn spring-boot:run

# 4. Verifica que está corriendo
# http://localhost:8080/api/actuator/health
```

### Opción B — Con Docker Compose

```bash
# 1. Copia las variables de entorno
cp .env.example .env

# 2. Levanta PostgreSQL Lima y pgAdmin
docker compose up db-lima pgadmin -d

# 3. Verifica los contenedores
docker compose ps

# 4. Arranca Spring Boot
mvn spring-boot:run
```

### Acceso a pgAdmin

```
URL:      http://localhost:5050
Email:    admin@tecambioya.com
Password: definido en .env
```

---

## Perfiles de entorno

El `application.yml` define dos perfiles separados con `---`:

### Perfil `dev` (por defecto)

Apunta a PostgreSQL local, muestra SQL en consola, logs detallados.

```bash
# Se activa automáticamente, no se necesita ningún parámetro
mvn spring-boot:run
```

### Perfil `prod`

Requiere variables de entorno reales. No tiene valores por defecto para credenciales.

```bash
java -jar target/backend-TodoCambioYa.jar --spring.profiles.active=prod
```

---

## Capas de la aplicación

### Entity (`com.todocambioya.entity`)

14 clases Java que mapean directamente a las tablas PostgreSQL mediante anotaciones JPA estándar. Escritas de forma nativa con constructores, getters y setters manuales.

Anotaciones utilizadas:

```java
@Entity          // marca la clase como tabla
@Table           // nombre de la tabla en BD
@Id              // clave primaria
@UuidGenerator   // genera UUID automáticamente (Hibernate)
@Column          // configuración de columna
@ManyToOne       // relación N:1
@JoinColumn      // columna de clave foránea
@PrePersist      // hook que se ejecuta antes de insertar
```

### Repository (`com.todocambioya.repository`)

14 interfaces que extienden `JpaRepository`. Spring Data JPA genera automáticamente la implementación SQL a partir del nombre del método:

```java
// Spring genera: SELECT * FROM usuarios WHERE email = ?
Optional<Usuario> findByEmail(String email);

// Spring genera: SELECT COUNT(*) FROM usuarios WHERE email = ?
boolean existsByEmail(String email);
```

### DTO (`com.todocambioya.dto`)

Objetos de transferencia de datos que definen exactamente qué información entra y sale de la API, evitando exponer las entidades directamente.

```
dto/request/
  ├── LoginRequest.java           # email + password
  ├── RegisterRequest.java        # datos de registro con validaciones
  ├── OrdenRequest.java           # crear orden de cambio
  └── CuentaBancariaRequest.java  # agregar cuenta bancaria

dto/response/
  ├── AuthResponse.java           # token JWT + datos del usuario
  ├── UsuarioResponse.java        # perfil sin password_hash
  ├── TipoCambioResponse.java     # tasas vigentes
  ├── OrdenResponse.java          # detalle de una orden
  ├── CuentaBancariaResponse.java # cuenta con nombre del banco
  └── ApiResponse.java            # wrapper genérico para todas las respuestas
```

Todas las respuestas de la API siguen el formato:

```json
{
  "success": true,
  "message": "Login exitoso",
  "data": { ... },
  "timestamp": "2026-05-13T22:00:00"
}
```

---

## Variables de entorno

| Variable | Descripción | Valor por defecto (dev) |
|---|---|---|
| `DB_USER` | Usuario de PostgreSQL | `tcya_user` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | `tcya_pass_local` |
| `DB_HOST` | Host de PostgreSQL (solo prod) | — |
| `DB_PORT` | Puerto de PostgreSQL (solo prod) | `5432` |
| `DB_NAME` | Nombre de la BD (solo prod) | — |
| `JWT_SECRET` | Clave secreta para firmar JWT | `dev_secret_key_...` |
| `JWT_EXPIRATION_MS` | Duración del token en ms | `86400000` (24h) |

> En producción `JWT_SECRET` y `DB_PASSWORD` son **obligatorios** — la aplicación no arranca sin ellos.

---

## Estado actual del proyecto

| Componente | Estado |
|---|---|
| Schema PostgreSQL (14 tablas) | ✅ Completado |
| Entidades JPA (14 clases) | ✅ Completado |
| Repositories (14 interfaces) | ✅ Completado |
| DTOs request/response | ✅ Completado |
| Security + JWT | 🔄 En progreso |
| Services | 🔄 En progreso |
| Controllers REST | 🔄 En progreso |
| Docker Compose completo | 🔄 En progreso |
| Fase 2 — PostgreSQL distribuido | ⏳ Pendiente |
| Fase 3 — MongoDB | ⏳ Pendiente |
| Fase 4 — Cassandra | ⏳ Pendiente |
| Fase 5 — Hadoop | ⏳ Pendiente |