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
| PostgreSQL | 16 | Base de datos distribuida (3 nodos) |
| Flyway | (gestionado por Boot) | Migraciones de base de datos |
| jjwt | 0.12.5 | Generación y validación de JWT |
| Maven | 3.9.15 | Gestión de dependencias y build |
| Docker | — | Contenedores para los 3 nodos PostgreSQL |

> **Nota:** El proyecto está desarrollado de forma **nativa**, sin uso de Lombok ni MapStruct. Todas las clases incluyen constructores, getters y setters escritos manualmente.

---

## Arquitectura por fases

El proyecto sigue una arquitectura de base de datos evolutiva definida por etapas:

```
Fase 1 (completada) ── PostgreSQL nodo único (Lima)
Fase 2 (actual)     ── PostgreSQL distribuido (Lima :5442 + Arequipa :5443 + Trujillo :5444)
Fase 3              ── MongoDB para datos no estructurados (chat, notificaciones push)
Fase 4              ── Cassandra para escrituras de alta velocidad (ticks de tipo de cambio)
Fase 5              ── Hadoop para analítica batch nocturna y reportes ejecutivos
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
│       │   ├── config/              # DataSourceConfig + RegionRoutingDataSource
│       │   ├── context/             # RegionContextHolder
│       │   ├── service/             # Lógica de negocio (pendiente)
│       │   ├── controller/          # Endpoints REST (pendiente)
│       │   └── security/            # JWT + Spring Security (pendiente)
│       └── resources/
│           ├── db/migration/
│           │   ├── V2__nodo_lima.sql        # Nodo Lima — ejecutar en todocambioya_lima
│           │   ├── V2__nodo_arequipa.sql    # Nodo Arequipa — ejecutar en todocambioya_arequipa
│           │   └── V2__nodo_trujillo.sql    # Nodo Trujillo — ejecutar en todocambioya_trujillo
│           └── application.yml             # Configuración de perfiles dev/prod + 3 datasources
├── docker-compose.yml                      # 3 nodos PostgreSQL + pgAdmin
├── .env.example
└── pom.xml
```

---

## Base de datos

### Fase 2 — PostgreSQL distribuido (actual)

La base de datos está distribuida en **3 nodos PostgreSQL independientes**, cada uno representando una región geográfica del Perú. Cada nodo corre en un contenedor Docker separado.

| Nodo | Región cubre | Puerto local | Base de datos |
|---|---|---|---|
| Lima | Lima, Ica, Callao | `5442` | `todocambioya_lima` |
| Arequipa | Arequipa, Cusco, Puno, Moquegua, Tacna | `5443` | `todocambioya_arequipa` |
| Trujillo | Trujillo, Piura, Chiclayo, Cajamarca | `5444` | `todocambioya_trujillo` |

> **Nota sobre puertos:** Los puertos `5442`, `5443`, `5444` se usan para evitar conflicto con la instalación local de PostgreSQL que ocupa el puerto estándar `5432`.

### Estrategia de distribución

Las 14 tablas del esquema se dividen en dos grupos según su naturaleza:

**Tablas replicadas** — catálogos globales con copia idéntica en los 3 nodos:

| Tabla | Razón |
|---|---|
| `regiones` | Catálogo de referencia global |
| `bancos` | Operan en todo el país |
| `tipos_cambio` | La tasa es igual para todas las regiones |
| `cupones` | Un cupón puede usarse desde cualquier región |
| `empresas` | Una empresa puede tener usuarios en varias regiones |
| `usuarios_empresa` | Los roles pueden cruzar regiones |
| `referidos` | El referidor y referido pueden ser de regiones distintas |

**Tablas fragmentadas horizontalmente** — cada nodo almacena solo las filas de su región (`region_id`):

| Tabla | Criterio de fragmentación |
|---|---|
| `usuarios` | `region_id` del usuario |
| `cuentas_bancarias` | Región del usuario dueño |
| `alertas_tipo_cambio` | Región del usuario |
| `ordenes` | `region_id` de la orden |
| `comprobantes` | Región de la orden asociada |
| `notificaciones` | Región del usuario |
| `auditoria_sesiones` | `region_id` de la sesión |

### Integridad de fragmentación

Cada nodo tiene un `CHECK CONSTRAINT` a nivel de PostgreSQL que rechaza filas con `region_id` incorrecto:

```sql
-- En nodo Lima: rechaza cualquier usuario que no sea de Lima
CONSTRAINT chk_region_lima CHECK (region_id = 1)

-- En nodo Arequipa: rechaza cualquier usuario que no sea de Arequipa
CONSTRAINT chk_region_arequipa CHECK (region_id = 2)

-- En nodo Trujillo: rechaza cualquier usuario que no sea de Trujillo
CONSTRAINT chk_region_trujillo CHECK (region_id = 3)
```

### Scripts de migración

Cada nodo tiene su propio script SQL que incluye: creación de tablas, índices, datos de catálogo (seed) y datos de prueba.

```
V2__nodo_lima.sql      → ejecutar en todocambioya_lima     (puerto 5442)
V2__nodo_arequipa.sql  → ejecutar en todocambioya_arequipa (puerto 5443)
V2__nodo_trujillo.sql  → ejecutar en todocambioya_trujillo (puerto 5444)
```

> **Importante:** Flyway apunta al nodo Lima como datasource principal. Los nodos Arequipa y Trujillo se inicializan manualmente ejecutando sus scripts en pgAdmin.

### Datos de prueba incluidos

Cada script incluye datos de prueba listos para demostrar la fragmentación:

| Nodo | Usuarios | Órdenes | Estado órdenes |
|---|---|---|---|
| Lima | Carlos Quispe, Ana Torres, Empresa Lima SAC | ORD-LIM-0001, ORD-LIM-0002 | completado |
| Arequipa | Maria Flores, Roberto Zuniga | ORD-AQP-0001, ORD-AQP-0002 | completado |
| Trujillo | Luis Chavez, Carmen Ruiz | ORD-TRU-0001, ORD-TRU-0002 | completado / pendiente |

### Cadena principal de transacción

```
regiones → usuarios → cuentas_bancarias + tipos_cambio + cupones → ordenes → comprobantes
```

### Convenciones

- Tablas de negocio usan `UUID` como PK generado por PostgreSQL con `gen_random_uuid()`
- Tablas de catálogo (`regiones`, `bancos`) usan `SERIAL` (entero autoincremental)
- Las FK entre tablas fragmentadas y replicadas son **FK lógicas** (no físicas) cuando el dato referenciado puede estar en otro nodo
- Todas las fechas se almacenan en zona horaria `America/Lima`
- El campo `ip_address` de `auditoria_sesiones` usa tipo `INET` de PostgreSQL

### Routing de datasources

El `application.yml` define 3 datasources independientes. La clase `RegionRoutingDataSource` extiende `AbstractRoutingDataSource` de Spring y enruta cada query al nodo correcto según el `region_id` del usuario autenticado:

```
region_id = 1  →  HikariPool-Lima      (localhost:5442)
region_id = 2  →  HikariPool-Arequipa  (localhost:5443)
region_id = 3  →  HikariPool-Trujillo  (localhost:5444)
```

---

## Instalación y configuración

### Requisitos previos

- Java 24 instalado
- Maven 3.9+ instalado
- Docker Desktop corriendo
- pgAdmin 4 instalado (para gestión visual de los 3 nodos)

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

---

## Ejecución

### Levantar los 3 nodos PostgreSQL

```bash
docker compose up db-lima db-arequipa db-trujillo -d

# Verificar que los 3 están healthy
docker compose ps
```

### Inicializar las bases de datos en pgAdmin

Conectar los 3 servidores en pgAdmin con estos datos:

| Servidor | Host | Puerto | Base de datos | Usuario | Password |
|---|---|---|---|---|---|
| TeCambioYa - Lima | `localhost` | `5442` | `todocambioya_lima` | `tcya_user` | `tcya_pass_local` |
| TeCambioYa - Arequipa | `localhost` | `5443` | `todocambioya_arequipa` | `tcya_user` | `tcya_pass_local` |
| TeCambioYa - Trujillo | `localhost` | `5444` | `todocambioya_trujillo` | `tcya_user` | `tcya_pass_local` |

Luego ejecutar en cada base de datos su script correspondiente desde el Query Tool (F5):

```
todocambioya_lima     ← V2__nodo_lima.sql
todocambioya_arequipa ← V2__nodo_arequipa.sql
todocambioya_trujillo ← V2__nodo_trujillo.sql
```

### Arrancar el backend

```bash
mvn spring-boot:run
```

Verificar: `http://localhost:8080/api/actuator/health`

---

## Perfiles de entorno

### Perfil `dev` (por defecto)

Apunta a los 3 nodos PostgreSQL en Docker local, muestra SQL en consola, logs detallados.

```bash
mvn spring-boot:run
```

### Perfil `prod`

Requiere variables de entorno reales con los hosts de cada nodo.

```bash
java -jar target/backend-TodoCambioYa.jar --spring.profiles.active=prod
```

---

## Capas de la aplicación

### Entity (`com.todocambioya.entity`)

14 clases Java que mapean directamente a las tablas PostgreSQL mediante anotaciones JPA estándar. Escritas de forma nativa con constructores, getters y setters manuales.

### Repository (`com.todocambioya.repository`)

14 interfaces que extienden `JpaRepository`. Spring Data JPA genera automáticamente la implementación SQL a partir del nombre del método.

### DTO (`com.todocambioya.dto`)

Objetos de transferencia de datos que definen exactamente qué información entra y sale de la API.

```
dto/request/
  ├── LoginRequest.java
  ├── RegisterRequest.java
  ├── OrdenRequest.java
  └── CuentaBancariaRequest.java

dto/response/
  ├── AuthResponse.java
  ├── UsuarioResponse.java
  ├── TipoCambioResponse.java
  ├── OrdenResponse.java
  ├── CuentaBancariaResponse.java
  └── ApiResponse.java
```

Todas las respuestas siguen el formato:

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
| `DB_HOST_LIMA` | Host nodo Lima (solo prod) | — |
| `DB_HOST_AREQUIPA` | Host nodo Arequipa (solo prod) | — |
| `DB_HOST_TRUJILLO` | Host nodo Trujillo (solo prod) | — |
| `JWT_SECRET` | Clave secreta para firmar JWT | `dev_secret_key_...` |
| `JWT_EXPIRATION_MS` | Duración del token en ms | `86400000` (24h) |

---

## Estado actual del proyecto

| Componente | Estado |
|---|---|
| Schema PostgreSQL — Fase 1 (14 tablas, nodo único) | ✅ Completado |
| Schema PostgreSQL — Fase 2 (3 nodos distribuidos) | ✅ Completado |
| Entidades JPA (14 clases) | ✅ Completado |
| Repositories (14 interfaces) | ✅ Completado |
| DTOs request/response | ✅ Completado |
| Docker Compose (3 nodos + pgAdmin) | ✅ Completado |
| RegionRoutingDataSource (routing entre nodos) | 🔄 En progreso |
| Security + JWT | 🔄 En progreso |
| Services | 🔄 En progreso |
| Controllers REST | 🔄 En progreso |
| Fase 3 — MongoDB | ⏳ Pendiente |
| Fase 4 — Cassandra | ⏳ Pendiente |
| Fase 5 — Hadoop | ⏳ Pendiente |