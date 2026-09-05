# Explicación de los archivos del proyecto CamelVSDwarf

Este proyecto está organizado en paquetes según el dominio de negocio. La idea principal es separar las responsabilidades para que cada parte del sistema tenga un propósito claro.

## 1. Archivo principal de la aplicación

### CamelVsDwarfApplication.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/CamelVsDwarfApplication.java`
- Función: es la clase principal de Spring Boot.
- Qué hace: inicializa la aplicación y levanta el contenedor de Spring.
- Anotación importante: `@SpringBootApplication`
- Es el punto de entrada desde el que se ejecuta el backend.

---

## 2. Paquete user

### AppUser.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/user/AppUser.java`
- Función: representa al usuario de la aplicación.
- Es una entidad JPA (`@Entity`).
- Guarda datos como:
  - nombre completo
  - email
  - contraseña cifrada
  - rol
  - estado enabled
  - fecha de creación
- Es la tabla principal para autenticación y autorización.

### AppUserRepository.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/user/AppUserRepository.java`
- Función: acceso a la base de datos para usuarios.
- Extiende `JpaRepository`.
- Permite hacer CRUD y consultas básicas por email, id, etc.

### LoginRequest.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/user/LoginRequest.java`
- Función: DTO para recibir los datos del login.
- Sirve para encapsular la información enviada por el cliente:
  - email
  - password

### RegisterRequest.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/user/RegisterRequest.java`
- Función: DTO para registrar un usuario nuevo.
- Se usa al crear una cuenta.

### Role.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/user/Role.java`
- Función: enum con los roles posibles del sistema.
- Ejemplos comunes:
  - ADMIN
  - USER
  - ORGANIZER
- Permite gestionar permisos y acceso según el rol.

### TokenResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/user/TokenResponse.java`
- Función: respuesta que devuelve el token JWT al usuario autenticado.
- Se usa cuando el login es correcto y se genera un token para el frontend.

### UserResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/user/UserResponse.java`
- Función: DTO de salida para devolver datos del usuario sin exponer información sensible.
- Normalmente no devuelve la contraseña.

---

## 3. Paquete race

### Race.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/race/Race.java`
- Función: entidad que representa una carrera.
- Guarda información como:
  - nombre
  - descripción
  - fecha y hora
  - ubicación de inicio y final
  - distancia
  - número máximo de participantes
  - tipo de carrera
  - estado
  - organizador
- Relaciona la carrera con un usuario organizador.

### RaceRepository.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/race/RaceRepository.java`
- Función: acceso a las carreras en base de datos.
- Permite buscar, guardar, actualizar y listar carreras.

### RaceRequest.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/race/RaceRequest.java`
- Función: DTO para crear o actualizar una carrera desde el cliente.

### RaceResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/race/RaceResponse.java`
- Función: DTO de salida para devolver la información de la carrera al cliente.

### RaceStatus.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/race/RaceStatus.java`
- Función: enum que representa el estado de la carrera.
- Ejemplos: DRAFT, OPEN, CLOSED, FINISHED.

### RaceStatusRequest.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/race/RaceStatusRequest.java`
- Función: DTO para cambiar el estado de una carrera.

### RaceType.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/race/RaceType.java`
- Función: enum que identifica el tipo de carrera.
- Ejemplos: ROAD, TRAIL, MTB, etc.

---

## 4. Paquete team

### Team.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/team/Team.java`
- Función: representa un equipo.
- Guarda:
  - nombre
  - descripción
  - entrenador
  - estado del equipo
  - máximo de integrantes
  - victorias y derrotas

### TeamMember.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/team/TeamMember.java`
- Función: representa la relación entre un usuario y un equipo.
- Sirve para saber qué usuarios pertenecen a cada equipo.

### TeamRepository.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/team/TeamRepository.java`
- Función: acceso a los equipos de la base de datos.

### TeamMemberRepository.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/team/TeamMemberRepository.java`
- Función: acceso a los miembros de equipo.

### TeamRequest.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/team/TeamRequest.java`
- Función: DTO para crear o modificar un equipo.

### TeamResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/team/TeamResponse.java`
- Función: DTO de salida con la información del equipo.

### TeamStatus.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/team/TeamStatus.java`
- Función: enum del estado del equipo.

---

## 5. Paquete competitor

### Competitor.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/competitor/Competitor.java`
- Función: entidad para un participante/competidor.
- Representa a una persona o corredor asociado a una carrera o equipo.

### CompetitorRepository.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/competitor/CompetitorRepository.java`
- Función: acceso para guardar y consultar competidores.

### CompetitorRequest.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/competitor/CompetitorRequest.java`
- Función: DTO para crear o actualizar un competidor.

### CompetitorResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/competitor/CompetitorResponse.java`
- Función: DTO para devolver la información del competidor.

### CompetitorStatus.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/competitor/CompetitorStatus.java`
- Función: enum del estado del competidor.

### CompetitorType.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/competitor/CompetitorType.java`
- Función: enum del tipo de competidor.

---

## 6. Paquete registration

### RaceRegistration.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/registration/RaceRegistration.java`
- Función: entidad para registrar la inscripción de un usuario o equipo a una carrera.
- Guarda información sobre la participación en una carrera.

### RaceRegistrationRepository.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/registration/RaceRegistrationRepository.java`
- Función: acceso a las inscripciones.

### RegistrationRequest.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/registration/RegistrationRequest.java`
- Función: DTO de entrada para registrar inscripción.

### RegistrationResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/registration/RegistrationResponse.java`
- Función: DTO de salida con la información de la inscripción.

### RegistrationStatus.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/registration/RegistrationStatus.java`
- Función: enum para el estado de inscripción.

### RegistrationDecisionRequest.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/registration/RegistrationDecisionRequest.java`
- Función: DTO para aceptar o rechazar una inscripción.

---

## 7. Paquete result

### RaceResult.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/result/RaceResult.java`
- Función: entidad que almacena el resultado final de una carrera.
- Guarda el rendimiento, clasificaciones y posición final.

### RaceResultRepository.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/result/RaceResultRepository.java`
- Función: acceso a los resultados de carrera.

### ResultRequest.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/result/ResultRequest.java`
- Función: DTO para registrar resultados.

### ResultResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/result/ResultResponse.java`
- Función: DTO para devolver resultados al cliente.

### ResultStatus.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/result/ResultStatus.java`
- Función: enum del estado del resultado.

---

## 8. Paquete audit

### AuditLog.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/audit/AuditLog.java`
- Función: registra eventos del sistema.
- Sirve para auditar acciones importantes como:
  - login
  - modificación de carrera
  - inscripción
  - aprobación/rechazo

### AuditLogRepository.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/audit/AuditLogRepository.java`
- Función: acceso a los logs de auditoría.

### AuditLogResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/audit/AuditLogResponse.java`
- Función: DTO para devolver información del registro de auditoría.

---

## 9. Paquete shared

### shared/dto
- Contiene objetos de transferencia de datos reutilizables.

### ApiErrorResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/shared/dto/ApiErrorResponse.java`
- Función: respuesta estandarizada para errores del sistema.
- Sirve para devolver formato uniforme de error al frontend.

### PageResponse.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/shared/dto/PageResponse.java`
- Función: DTO para paginación de resultados.

### shared/exception
- Contiene las excepciones globales del backend.

### BusinessConflictException.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/shared/exception/BusinessConflictException.java`
- Función: excepción para conflictos de negocio, por ejemplo:
  - una carrera ya está cerrada
  - una inscripción duplicada

### ResourceNotFoundException.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/shared/exception/ResourceNotFoundException.java`
- Función: excepción cuando un recurso no existe.

### GlobalExceptionHandler.java
- Ubicación: `src/main/java/com/example/camelvsdwarf/shared/exception/GlobalExceptionHandler.java`
- Función: centraliza el manejo de errores.
- Usa `@RestControllerAdvice` para capturar excepciones en todos los controladores.

---

## 10. Paquetes config, controller, service, security

### config
- Ubicación: `src/main/java/com/example/camelvsdwarf/config`
- Función: configuración de la aplicación.
- Aquí se pueden definir beans, seguridad, CORS, serialización, etc.

### controller
- Ubicación: `src/main/java/com/example/camelvsdwarf/controller`
- Función: expone los endpoints REST.
- Aquí van los controladores de cada dominio.

### service
- Ubicación: `src/main/java/com/example/camelvsdwarf/service`
- Función: contiene la lógica de negocio.
- Es la capa intermedia entre controller y repository.

### security
- Ubicación: `src/main/java/com/example/camelvsdwarf/security`
- Función: configura autenticación, autorización y seguridad del sistema.
- Normalmente incluye filtros, JWT y reglas de acceso.

---

## 11. Cómo se conectan las capas

El flujo general del proyecto es:

1. El cliente llama a un endpoint del `controller`.
2. El `controller` delega la lógica al `service`.
3. El `service` usa los `repository` para acceder a la base de datos.
4. Las entidades JPA representan los datos.
5. Los `dto` se usan para enviar y recibir datos entre capas.
6. Las excepciones se manejan centralmente en `GlobalExceptionHandler`.

En pocas palabras:

- `controller` = recibe peticiones
- `service` = aplica la lógica
- `repository` = accede a datos
- `entity` = representa tablas
- `dto` = representa datos de entrada/salida
- `shared` = utilidades y manejo de errores

---

## 12. Resumen del proyecto

CamelVSDwarf está estructurado como una API REST con arquitectura por dominios:

- `user`: usuarios y autenticación
- `race`: carreras
- `team`: equipos
- `competitor`: competidores
- `registration`: inscripciones
- `result`: resultados
- `audit`: auditoría
- `shared`: utilidades y manejo de errores

Esta organización hace que el proyecto sea más mantenible, claro y escalable.
