# 📑 Auditoría de Endpoints — Proyecto Spring Boot CamelVSDwarf

## 1. Arquitectura y objetos principales
El backend está organizado por módulos/feature:

- **user**
  - Entidad: `AppUser`
  - DTOs: `UserRequest`, `UserResponse`
  - Controlador: `/api/v1/users`
  - Servicio: `UserService`

- **team**
  - Entidad: `Team`, `TeamMember`
  - DTOs: `TeamRequest`, `TeamResponse`
  - Controlador: `/api/v1/teams`
  - Servicio: `TeamService`

- **race**
  - Entidad: `Race`
  - DTOs: `RaceRequest`, `RaceResponse`
  - Controlador: `/api/v1/races`
  - Servicio: `RaceService`

- **registration**
  - Entidad: `RaceRegistration`
  - DTOs: `RegistrationRequest`, `RegistrationResponse`
  - Controlador: `/api/v1/registrations`
  - Servicio: `RegistrationService`

- **result**
  - Entidad: `RaceResult`
  - DTOs: `ResultRequest`, `ResultResponse`
  - Controlador: `/api/v1/results`
  - Servicio: `ResultService`

- **competitor**
  - Entidad: `Competitor`
  - DTOs: `CompetitorRequest`, `CompetitorResponse`
  - ⚠️ No tiene controlador implementado

- **shared**
  - `PageResponse`
  - `ApiErrorResponse`
  - `GlobalExceptionHandler`
  - Excepciones: `BusinessConflictException`, `ResourceNotFoundException`

---

## 2. Estructura general de respuesta paginada
Clase `PageResponse<T>` con campos:
- `content`
- `page`
- `size`
- `totalElements`
- `totalPages`
- `first`
- `last`

Ejemplo de endpoints que devuelven `PageResponse`:
- `GET /api/v1/races`
- `GET /api/v1/users`
- `GET /api/v1/teams`
- `GET /api/v1/registrations`
- `GET /api/v1/results`

---

## 3. Estructura de errores centralizada
Manejada por `GlobalExceptionHandler`.  
Respuesta: `ApiErrorResponse` con campos:
- `timestamp`
- `status`
- `error`
- `message`
- `path`
- `fieldErrors`

Excepciones mapeadas:
- `ResourceNotFoundException` → 404
- `BusinessConflictException` → 409
- `MethodArgumentNotValidException` → 400
- `ConstraintViolationException` → 400

---

## 4. Rutas REST existentes

### Usuarios
- `GET /api/v1/users` → `PageResponse<UserResponse>`
- `GET /api/v1/users/{id}` → `UserResponse`
- `POST /api/v1/users` → `UserResponse` (201 Created)
- `PUT /api/v1/users/{id}` → `UserResponse`
- `PATCH /api/v1/users/{id}/status` → `UserResponse`
- `DELETE /api/v1/users/{id}` → 204 No Content

### Equipos
- CRUD completo (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`)
- Relaciones:
  - `POST /api/v1/teams/{teamId}/competitors/{competitorId}` → Añadir competidor
  - `DELETE /api/v1/teams/{teamId}/competitors/{competitorId}` → Eliminar relación

### Carreras
- CRUD completo (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`)

### Inscripciones
- CRUD completo + `PATCH /status` y `PATCH /decision`

### Resultados
- CRUD completo (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`)

---

## 5. DTOs

### User
- **Request:** `fullName`, `email`, `password`, `role`
- **Response:** `id`, `fullName`, `email`, `role`, `enabled`, `createdAt`

### Team
- **Request:** `name`, `description`, `coach`, `maximumMembers`
- **Response:** `id`, `name`, `description`, `coach`, `status`, `maximumMembers`, `createdAt`, `victories`, `defeats`, `competitorIds`

### Race
- **Request:** `name`, `description`, `scheduledAt`, `startLocation`, `finishLocation`, `distanceMeters`, `maximumParticipants`, `type`, `registrationDeadline`
- **Response:** `id`, `name`, `description`, `scheduledAt`, `startLocation`, `finishLocation`, `distanceMeters`, `maximumParticipants`, `type`, `status`, `organizerId`, `registrationDeadline`, `createdAt`, `updatedAt`

### Registration
- **Request:** `raceId`, `participantType`, `competitorId/teamId`, `startingPosition`
- **Response:** `id`, `raceId`, `participantType`, `competitorId/teamId`, `registeredAt`, `status`, `startingPosition`, `validationNotes`, `registeredById`

### Result
- **Request:** `registrationId`, `startingPosition`, `finalPosition`, `completionTimeSeconds`, `penaltyTimeSeconds`, `status`, `notes`
- **Response:** `id`, `raceId`, `registrationId`, `startingPosition`, `finalPosition`, `completionTimeSeconds`, `penaltyTimeSeconds`, `status`, `notes`, `recordedById`, `recordedAt`

### Competitor
- **Request:** `name`, `nickname`, `type`, `dateOfBirth`, `weight`, `height`, `origin`
- **Response:** `id`, `name`, `nickname`, `type`, `dateOfBirth`, `weight`, `height`, `origin`, `status`, `registeredAt`, `victories`, `defeats`, `completedRaces`

---

## 6. Reglas de negocio implementadas

- **RaceService**
  - `registrationDeadline < scheduledAt`
  - Carrera completada no puede reabrirse
  - Delete lógico → estado `CANCELLED`

- **TeamService**
  - Nombres únicos
  - Capacidad máxima
  - Competidor no puede estar en dos equipos activos
  - Equipo y competidor deben estar activos

- **UserService**
  - Email único
  - Delete lógico → usuario desactivado

- **RegistrationService**
  - Exactamente un participante (`competitorId` o `teamId`)

- **GlobalExceptionHandler**
  - JSON estándar para errores

---

## 7. Observación QA
⚠️ No existe endpoint `/api/competitors`.  
Aunque hay entidad y DTO, falta controlador REST.

---

## 8. Formato para auditar endpoints
Cada endpoint debe documentarse con:
- **Ruta REST**
- **Método HTTP**
- **Request DTO**
- **Response DTO**
- **Códigos HTTP esperados**
- **Reglas de negocio aplicables**

---

## 9. Autenticación con Keycloak (obsoleto: usar Supabase Auth)

El frontend debe autenticarse mediante **Keycloak** usando OpenID Connect (OIDC). El frontend no debe consultar la base de datos ni Supabase directamente. El flujo es:

```text
Frontend → Keycloak: login del usuario
Keycloak → Frontend: access_token JWT
Frontend → Spring Boot API: Authorization: Bearer <access_token>
Spring Boot → Keycloak: valida firma, issuer y expiración del JWT
```

Supabase se utiliza como base de datos PostgreSQL. No se debe utilizar la URL de Supabase ni sus API keys como credenciales de Keycloak.

### Configuración local

Con Docker Compose, Keycloak queda disponible en:

```text
http://localhost:8180
```

Realm:

```text
camel-vs-dwarf
```

Cliente:

```text
camel-vs-dwarf-api
```

Issuer del realm:

```text
http://localhost:8180/realms/camel-vs-dwarf
```

Configuración del backend local:

```env
KEYCLOAK_ENABLED=true
KEYCLOAK_ISSUER_URI=http://keycloak:8080/realms/camel-vs-dwarf
```

El hostname `keycloak` funciona desde el contenedor del backend dentro de Docker Compose. Desde el navegador se utiliza `localhost:8180`.

El realm de desarrollo se encuentra en:

```text
config/keycloak/camel-vs-dwarf-realm.json
```

Para iniciar el entorno:

```bash
docker compose up -d --build
```

### Roles

El realm define los siguientes roles:

| Rol | Permisos esperados |
|---|---|
| `ADMINISTRATOR` | Gestionar usuarios, competidores, equipos, carreras, inscripciones, resultados y auditoría. |
| `ORGANIZER` | Gestionar carreras, inscripciones y resultados; consultar competidores y equipos. |
| `VIEWER` | Consultar información pública, carreras, resultados y clasificaciones. |

Los roles se asignan en Keycloak desde:

```text
Users → seleccionar usuario → Role mapping → Realm roles
```

El backend convierte los roles de Keycloak en autoridades Spring con el prefijo `ROLE_`. Por ejemplo, `ADMINISTRATOR` se convierte en `ROLE_ADMINISTRATOR`.

### Login recomendado para Lovable

Lovable debe implementar Authorization Code Flow con PKCE. No debe guardar contraseñas ni usar la `sb_secret` de Supabase.

Configuración del cliente OIDC:

```javascript
const keycloakConfig = {
  url: import.meta.env.VITE_KEYCLOAK_URL || "http://localhost:8180",
  realm: "camel-vs-dwarf",
  clientId: "camel-vs-dwarf-api"
};
```

Endpoints OIDC del realm:

```text
Authorization: http://localhost:8180/realms/camel-vs-dwarf/protocol/openid-connect/auth
Token:         http://localhost:8180/realms/camel-vs-dwarf/protocol/openid-connect/token
Logout:        http://localhost:8180/realms/camel-vs-dwarf/protocol/openid-connect/logout
JWKS:          http://localhost:8180/realms/camel-vs-dwarf/protocol/openid-connect/certs
Issuer:        http://localhost:8180/realms/camel-vs-dwarf
```

Lovable puede usar `keycloak-js` o una librería OIDC equivalente. El flujo mínimo debe ser:

1. Inicializar Keycloak al abrir la aplicación.
2. Redirigir a Keycloak si el usuario no está autenticado.
3. Obtener el `accessToken`.
4. Enviar el token en cada llamada al backend.
5. Renovar el token antes de su expiración.
6. Cerrar sesión usando el endpoint de logout de Keycloak.

Ejemplo de cliente HTTP:

```javascript
async function apiFetch(path, options = {}, keycloak) {
  await keycloak.updateToken(30);

  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
      Authorization: `Bearer ${keycloak.token}`
    }
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.status === 204 ? null : response.json();
}
```

### Protección de la interfaz

El frontend debe ocultar o deshabilitar acciones según el rol del usuario:

```javascript
const roles = keycloak.tokenParsed?.realm_access?.roles || [];
const isAdmin = roles.includes("ADMINISTRATOR");
const isOrganizer = roles.includes("ORGANIZER");
const isViewer = roles.includes("VIEWER");
```

Reglas mínimas:

- `VIEWER` solo puede consultar información.
- `ORGANIZER` puede crear y administrar carreras, inscripciones y resultados.
- `ADMINISTRATOR` puede administrar todos los módulos.
- La protección del frontend solo mejora la experiencia; el backend siempre debe validar el token y los permisos.
- Si la API responde `401`, el frontend debe renovar el token o enviar al usuario al login.
- Si la API responde `403`, debe mostrar una pantalla o mensaje de acceso denegado.

### Configuración en producción

En la nube no se puede usar `http://keycloak:8080`, porque ese hostname solo existe dentro de Docker Compose. El backend y el frontend necesitan acceder a una instancia pública de Keycloak. Puedes desplegar Keycloak como un servicio separado en Render, Railway, Fly.io u otro proveedor compatible.

No es suficiente desplegar solamente el backend. La arquitectura de producción debe ser:

```text
Frontend público → Keycloak público → access_token JWT
Frontend público → API Spring Boot pública → valida el JWT de Keycloak
API Spring Boot pública → Supabase PostgreSQL
```

### Desplegar Keycloak en la nube

Keycloak debe usar una base de datos PostgreSQL persistente. Puedes usar una base de datos PostgreSQL separada o un esquema/base de datos dedicado en Supabase. No uses la misma base de datos local de Docker para producción.

Variables mínimas del servicio cloud de Keycloak:

```env
KC_BOOTSTRAP_ADMIN_USERNAME=admin
KC_BOOTSTRAP_ADMIN_PASSWORD=una-clave-segura
KC_DB=postgres
KC_DB_URL=jdbc:postgresql://SUPABASE_HOST:6543/postgres?sslmode=require
KC_DB_USERNAME=postgres.PROJECT_REF
KC_DB_PASSWORD=SUPABASE_DATABASE_PASSWORD
KC_HTTP_ENABLED=true
KC_PROXY_HEADERS=xforwarded
KC_HOSTNAME=https://keycloak.example.com
```

El comando de inicio debe ser equivalente a:

```text
start --optimized
```

Si el proveedor utiliza el Dockerfile de este proyecto, crea un servicio separado usando la imagen oficial `quay.io/keycloak/keycloak:26.7.3` o un Dockerfile específico de Keycloak. El servicio debe tener almacenamiento persistente para la base de datos de Keycloak o utilizar PostgreSQL externo.

Después de iniciar Keycloak, crea o importa el realm `camel-vs-dwarf`. En el cliente `camel-vs-dwarf-api` configura:

- `Valid redirect URIs`: la URL pública real del frontend, por ejemplo `https://frontend.example.com/*`.
- `Web origins`: la URL pública real del frontend, por ejemplo `https://frontend.example.com`.
- `Standard flow`: habilitado.
- Cliente público para Authorization Code + PKCE si el login ocurre en el navegador.

El valor del issuer debe poder abrirse públicamente en:

```text
https://keycloak.example.com/realms/camel-vs-dwarf/.well-known/openid-configuration
```

El campo `issuer` de esa respuesta debe coincidir exactamente con `KEYCLOAK_ISSUER_URI`.

### Variables del backend en Render

En el servicio Spring Boot de Render configura:

```env
KEYCLOAK_ENABLED=true
KEYCLOAK_ISSUER_URI=https://keycloak.example.com/realms/camel-vs-dwarf
DB_URL=jdbc:postgresql://SUPABASE_HOST:6543/postgres?sslmode=require&prepareThreshold=0
DB_USERNAME=postgres.PROJECT_REF
DB_PASSWORD=SUPABASE_DATABASE_PASSWORD
JPA_DDL_AUTO=update
JWT_SECRET=un-secreto-largo-y-aleatorio
```

No uses `http://keycloak:8080` ni `http://localhost:8180` en Render. Después de configurar estas variables, ejecuta un nuevo deploy del backend.

Y el frontend debe usar:

```env
VITE_KEYCLOAK_URL=https://keycloak.example.com
VITE_KEYCLOAK_REALM=camel-vs-dwarf
VITE_KEYCLOAK_CLIENT_ID=camel-vs-dwarf-api
VITE_API_BASE_URL=https://tu-api.onrender.com
```

En el frontend cloud, `VITE_KEYCLOAK_URL` debe ser únicamente la URL base de Keycloak, sin `/realms`:

```env
VITE_KEYCLOAK_URL=https://keycloak.example.com
VITE_KEYCLOAK_REALM=camel-vs-dwarf
VITE_KEYCLOAK_CLIENT_ID=camel-vs-dwarf-api
```

El cliente de Keycloak debe incluir como URLs válidas el dominio real del frontend desplegado y configurar correctamente sus `redirectUris` y `webOrigins`.

### Variables relacionadas con Supabase

Supabase y Keycloak cumplen funciones diferentes:

```env
# Supabase/PostgreSQL para Spring Boot
DB_URL=jdbc:postgresql://HOST:PORT/postgres?sslmode=require
DB_USERNAME=postgres.PROJECT_REF
DB_PASSWORD=DATABASE_PASSWORD

# Keycloak para validar JWT
KEYCLOAK_ENABLED=true
KEYCLOAK_ISSUER_URI=https://keycloak.example.com/realms/camel-vs-dwarf
```

No se deben colocar contraseñas, `JWT_SECRET`, `sb_secret` ni tokens en el README, en `.env.example` ni en el código fuente.

## 10. Integración vigente para Lovable: Supabase Auth

> Esta es la configuración vigente. La sección anterior de Keycloak es histórica y no debe usarse para implementar el frontend.

Este repositorio contiene únicamente el backend Spring Boot. La interfaz mostrada en las capturas pertenece al frontend de Lovable. El frontend debe usar Supabase Auth para registrar e iniciar sesión; no debe implementar usuarios demo ni guardar contraseñas localmente.

### Variables del frontend

Configura estas variables en Lovable:

```env
VITE_SUPABASE_URL=https://mrftaeijsdhiulsxqyme.supabase.co
VITE_SUPABASE_PUBLISHABLE_KEY=sb_publishable_...
VITE_API_BASE_URL=https://camelvsdwarf.onrender.com
```

La publishable key puede usarse en el navegador. Nunca uses `sb_secret_...` en el frontend.

### Registro de la pantalla Create an account

Los campos de la pantalla son:

```text
Full name
Email
Password
Repeat password
Requested role
```

Primero valida que las contraseñas coincidan. Luego registra únicamente la identidad en Supabase:

```javascript
const { data, error } = await supabase.auth.signUp({
  email,
  password
});
```

No envíes `fullName` ni `role` a `/auth/v1/signup`; esos son datos del perfil de la aplicación.

Después de que Supabase devuelva una sesión, crea el perfil en el backend:

```javascript
const { data: sessionData } = await supabase.auth.getSession();
const accessToken = sessionData.session?.access_token;

await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/v1/users/me`, {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${accessToken}`
  },
  body: JSON.stringify({ fullName })
});
```

El backend crea el perfil en `public.app_users` y asigna `VIEWER` como rol inicial. El campo `Requested role` no debe permitir que un usuario se asigne `ADMINISTRATOR` a sí mismo. Para producción, registra siempre como `VIEWER`; un administrador puede cambiar el rol posteriormente.

Si la confirmación de email está activa, `signUp` puede devolver usuario sin sesión. Muestra un mensaje para confirmar el correo y no llames a `/api/v1/users/me` hasta tener un `access_token`.

### Inicio de sesión de la pantalla Sign in

El campo `Username or email` debe enviarse como `email`:

```javascript
const { data, error } = await supabase.auth.signInWithPassword({
  email,
  password
});
```

Después de un login exitoso, Supabase mantiene la sesión en el navegador. Para llamar al backend:

```javascript
async function apiFetch(path, options = {}) {
  const { data } = await supabase.auth.getSession();
  const accessToken = data.session?.access_token;

  if (!accessToken) {
    throw new Error("La sesión de Supabase no está activa");
  }

  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
      Authorization: `Bearer ${accessToken}`
    }
  });

  if (response.status === 401) {
    await supabase.auth.signOut();
    throw new Error("La sesión expiró");
  }

  if (!response.ok) throw await response.json();
  return response.status === 204 ? null : response.json();
}
```

### Endpoints de Supabase Auth

Con el SDK de Supabase, Lovable no necesita construir manualmente las URLs. Si se usa Postman, las rutas son:

```text
POST https://mrftaeijsdhiulsxqyme.supabase.co/auth/v1/signup
POST https://mrftaeijsdhiulsxqyme.supabase.co/auth/v1/token?grant_type=password
```

Ambas requieren el header `apikey` con la publishable key. El login devuelve `access_token`; ese token es el único valor que debe enviarse como `Authorization: Bearer ...` al backend.

### Perfil y tablas

Supabase Auth guarda la identidad en `auth.users`. El backend guarda el perfil en `public.app_users`, relacionado mediante `supabase_user_id`.

Antes de probar `/api/v1/users/me`, ejecuta el script:

```text
docs/supabase-auth-migration.sql
```

Endpoint para crear o recuperar el perfil:

```text
POST https://camelvsdwarf.onrender.com/api/v1/users/me
Authorization: Bearer <access_token>
Content-Type: application/json
```

Body:

```json
{
  "fullName": "Usuario de Prueba"
}
```

### Variables del backend en Render

```env
SUPABASE_PROJECT_ID=mrftaeijsdhiulsxqyme
SUPABASE_JWT_SECRET=JWT_SECRET_DE_SUPABASE
FRONTEND_URL=https://TU-FRONTEND.onrender.com
SPRING_DATASOURCE_URL=jdbc:postgresql://HOST_POOLER:PUERTO/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres.PROJECT_REF
SPRING_DATASOURCE_PASSWORD=PASSWORD_DE_POSTGRES
```

`SUPABASE_JWT_SECRET` es distinto de `JWT_SECRET` y de las claves `sb_publishable_...`/`sb_secret_...`. No lo publiques ni lo incluyas en el frontend.

### Estados que Lovable debe manejar

```text
201 o 200: operación correcta
400: datos de formulario inválidos
401: falta el access_token, expiró o Render no tiene el JWT secret correcto
403: token válido, pero sin permisos suficientes
409: conflicto, por ejemplo email o nombre duplicado
429: Supabase limitó temporalmente los correos de registro
```

La pantalla de login debe eliminar los usuarios demo `admin`, `organizer` y `viewer`. Esas credenciales no existen en Supabase Auth y no deben usarse como fallback cuando la API está disponible.
