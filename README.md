# 1. Copia el .env.example
cp .env.example .env

# 2. Levanta solo PostgreSQL Lima (Fase 1)
docker compose up db-lima pgadmin -d

# 3. Verifica que está corriendo
docker compose ps




## Dos perfiles separados por --- — dev y prod en el mismo archivo. En dev corre solo con Docker local, en prod requiere variables de entorno reales sin valores por defecto. Cambias de perfil con:

# dev (por defecto, no necesitas hacer nada)
mvn spring-boot:run

# prod
java -jar target/backend-TodoCambioYa.jar --spring.profiles.active=prod



# 1. Levantar PostgreSQL
docker compose up db-lima pgadmin -d

# 2. Arrancar Spring Boot (Flyway ejecuta V1__ automáticamente)
mvn spring-boot:run

# 3. Verificar en http://localhost:8080/api/actuator/health