# CamelVSDwarf API

Backend de Spring Boot para la gestión de competidores, equipos, carreras, inscripciones y resultados del proyecto CamelVSDwarf.

## Tecnologías

- Java 21
- Spring Boot 4.1.x
- Spring Web
- Spring Data JPA
- Spring Security + JWT (Supabase Auth)
- PostgreSQL
- Gradle
- JUnit 5 + Mockito
- JaCoCo

---

## Estructura principal

- `user`: usuarios y perfiles
- `team`: equipos y relación con competidores
- `competitor`: competidores del sistema
- `race`: carreras
- `registration`: inscripciones a carreras
- `result`: resultados de carreras
- `shared`: respuestas paginadas, validaciones y manejo global de errores

---

## Cómo ejecutar el proyecto

Requisitos:

- Java 21 instalado
- Gradle wrapper incluido

Desde la raíz del proyecto:

```bash
./gradlew bootRun
```

Para compilar:

```bash
./gradlew build
```

Para ejecutar solo las pruebas:

```bash
./gradlew test
```

Para ejecutar validaciones + cobertura:

```bash
./gradlew check
```

Si quieres generar reportes HTML de tests y cobertura:

```bash
./gradlew cobertura
```

La salida queda en:

- `build/reports/tests/test/index.html`
- `build/reports/jacoco/test/html/index.html`

---

## Cómo ejecutar las pruebas

La suite incluye pruebas unitarias y de controladores con MockMvc. Puedes ejecutarlas con:

```bash
./gradlew test
```

También puedes ejecutarlas con reporte de cobertura:

```bash
./gradlew test jacocoTestReport
```

Si quieres ver el estado completo de validación:

```bash
./gradlew check
```

---

## Endpoints principales

### Competidores

#### Crear competidor

```http
POST /api/v1/competitors
```

Request body:

```json
{
  "name": "Carlos Bermúdez",
  "nickname": "El Rayo",
  "type": "CAMEL",
  "dateOfBirth": "1995-06-15",
  "weight": 82.5,
  "height": 1.78,
  "origin": "Colombia"
}
```

Respuesta esperada:

```json
{
  "id": 1,
  "name": "Carlos Bermúdez",
  "nickname": "El Rayo",
  "type": "CAMEL",
  "dateOfBirth": "1995-06-15",
  "weight": 82.5,
  "height": 1.78,
  "origin": "Colombia",
  "status": "ACTIVE",
  "registeredAt": "2026-09-13T22:00:00",
  "victories": 0,
  "defeats": 0,
  "completedRaces": 0
}
```

#### Listar competidores

```http
GET /api/v1/competitors
```

#### Obtener competidor por ID

```http
GET /api/v1/competitors/{id}
```

#### Actualizar competidor

```http
PUT /api/v1/competitors/{id}
```

#### Cambiar estado de competidor

```http
PATCH /api/v1/competitors/{id}/status
```

#### Eliminar competidor

```http
DELETE /api/v1/competitors/{id}
```

### Equipos

#### Crear equipo

```http
POST /api/v1/teams
```

Request body:

```json
{
  "name": "Atlas",
  "description": "Equipo de prueba",
  "coach": "Coach Luna",
  "maximumMembers": 6
}
```

#### Obtener equipo por ID

```http
GET /api/v1/teams/{id}
```

#### Añadir competidor a un equipo

```http
POST /api/v1/teams/{teamId}/competitors/{competitorId}
```

Ejemplo:

```http
POST https://camelvsdwarf.onrender.com/api/v1/teams/1/competitors/1
```

Importante: esta ruta no lleva JSON en el body. Se usa por path params.

#### Quitar competidor de un equipo

```http
DELETE /api/v1/teams/{teamId}/competitors/{competitorId}
```

Ejemplo:

```http
DELETE https://camelvsdwarf.onrender.com/api/v1/teams/1/competitors/1
```

---

## Ejemplos de Postman

### 1) Crear un competidor

- Método: `POST`
- URL: `https://camelvsdwarf.onrender.com/api/v1/competitors`
- Body (raw JSON):

```json
{
  "name": "Carlos Bermúdez",
  "nickname": "El Rayo",
  "type": "CAMEL",
  "dateOfBirth": "1995-06-15",
  "weight": 82.5,
  "height": 1.78,
  "origin": "Colombia"
}
```

### 2) Crear un equipo

- Método: `POST`
- URL: `https://camelvsdwarf.onrender.com/api/v1/teams`
- Body:

```json
{
  "name": "Atlas",
  "description": "Equipo de prueba",
  "coach": "Coach Luna",
  "maximumMembers": 6
}
```

### 3) Añadir competidor a un equipo

- Método: `POST`
- URL: `https://camelvsdwarf.onrender.com/api/v1/teams/1/competitors/1`
- Body: vacío

### 4) Ver el equipo con sus miembros

- Método: `GET`
- URL: `https://camelvsdwarf.onrender.com/api/v1/teams/1`

Respuesta esperada:

```json
{
  "id": 1,
  "name": "Atlas",
  "description": "Equipo de prueba",
  "coach": "Coach Luna",
  "status": "ACTIVE",
  "maximumMembers": 6,
  "createdAt": "2026-09-13T22:00:00",
  "victories": 0,
  "defeats": 0,
  "competitorIds": [1]
}
```

---

## Reglas de negocio importantes

- Los nombres de equipos son únicos.
- Un competidor no puede pertenecer a dos equipos activos a la vez.
- Un equipo y un competidor deben estar activos para poder relacionarse.
- La capacidad máxima del equipo no puede superarse.
- El nickname del competidor debe ser único.
- Los emails de usuarios no pueden duplicarse.
- Las fechas de inscripción deben ser anteriores a la fecha de inicio de carrera.

---

## Manejo de errores

Cuando algo falla, la API responde con un JSON estándar con detalles del error, por ejemplo:

```json
{
  "timestamp": "2026-09-13T23:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Competitor nickname is already in use",
  "path": "/api/v1/competitors",
  "fieldErrors": []
}
```

---

## Estado actual

Se han agregado y validado los siguientes elementos:

- CRUD de competidores
- Controlador REST para competidores
- Servicio de competidores con validaciones de negocio
- Relación competidor-equipo
- Tests unitarios y de controladores
- Configuración de CI y reportes de cobertura
