FROM gradle:9.5.1-jdk21 AS build
WORKDIR /workspace

# Copia todo el contenido del directorio actual de una sola vez
COPY . .

# Ejecuta la compilación con la ruta completa asegurada
RUN sh gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
RUN useradd --system --uid 1001 appuser && chown appuser:appuser app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]