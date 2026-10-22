# Autos Colombia

Sistema de información para apoyar la operación diaria del parqueadero **Autos Colombia**, orientado al servicio de parqueo por mensualidad.

El proyecto hace parte de la asignatura Ingeniería de Software de la Institución Universitaria Digital de Antioquia. La primera iteración se enfoca en la gestión de entradas y salidas de vehículos.

## Autor

**Andrés VD**

## Alcance de la primera iteración

La solución se proyecta para administrar:

- Registro de entrada y salida de vehículos.
- Asignación y liberación de celdas de parqueo.
- Consulta de vehículos, clientes y operadores relacionados con una estancia.
- Registro de novedades asociadas a un vehículo.
- Base para la gestión de suscripciones mensuales.

## Estado actual

Actualmente el repositorio contiene la base técnica inicial del backend, el modelo de datos y un frontend operativo inicial:

- API inicial en Java 21 con Spring Boot.
- Configuración de persistencia con Spring Data JPA y MySQL 8.
- Migraciones de base de datos administradas con Flyway.
- Contenedor de MySQL mediante Docker Compose.
- Modelo entidad-relación, modelo físico y diagrama de arquitectura en formato PNG.
- Cliente web Angular para demostrar los flujos de entrada, salida, vehículos dentro y estado de celdas con datos simulados.

La integración del frontend con los endpoints reales y las funcionalidades completas de la iteración dependen del contrato de API del backend.

## Arquitectura planteada

La arquitectura propuesta está compuesta por:

| Componente | Tecnología | Responsabilidad |
| --- | --- | --- |
| Cliente web | Angular | Interfaz para el personal del parqueadero. |
| API | Spring Boot / Java 21 | Servicios REST y reglas de negocio. |
| Persistencia | Spring Data JPA / Hibernate | Acceso a datos. |
| Base de datos | MySQL 8 | Almacenamiento operativo. |
| Migraciones | Flyway | Versionamiento del esquema de datos. |
| Contenedores | Docker Compose | Ejecución local de MySQL. |

El diagrama disponible se encuentra en [architecture.png](architecture.png).

## Modelo de datos disponible

El esquema inicial contiene las siguientes entidades:

- `customer`: clientes del parqueadero.
- `operator`: operadores que registran los movimientos.
- `vehicle_type`: tipos de vehículo y tarifa mensual.
- `parking_spot`: celdas de parqueo.
- `vehicle`: vehículos registrados por cliente.
- `subscription`: mensualidades de los vehículos.
- `parking_session`: entradas y salidas.
- `incident`: novedades asociadas a un vehículo o estancia.

Los recursos de apoyo incluidos son:

- [Modelo entidad-relación](autos-colombia-diagrama-MER.png)
- [Modelo físico](autos-colombia-diagrama-fisico.png)
- [Script inicial](autos-colombia-script-inicial.sql)

## Requisitos previos

- Java 21.
- Docker Desktop y Docker Compose.
- Maven Wrapper incluido en el proyecto.

## Ejecución local

Para iniciar el frontend dockerizado desde la raíz del repositorio:

```powershell
docker compose up --build -d
```

Abre `http://localhost:4200`. El contenedor del frontend tiene un límite de 256 MB de memoria y medio núcleo de CPU. Para ver sus registros usa `docker compose logs -f frontend`; para detenerlo ejecuta `docker compose down`.

El frontend muestra información simulada mientras se acuerda e implementa el contrato de la API con el backend.

1. Inicie MySQL desde la carpeta `autos-colombia`:

   ```powershell
   docker compose up -d
   ```

2. Defina las variables de entorno usadas por la aplicación:

   ```powershell
   $env:AUTOS_COLOMBIA_DB_HOSTNAME = "localhost"
   $env:AUTOS_COLOMBIA_DB_PORT = "3306"
   $env:AUTOS_COLOMBIA_DB_NAME = "autoscolombia"
   $env:AUTOS_COLOMBIA_DB_USERNAME = "admin"
   $env:AUTOS_COLOMBIA_DB_PASSWORD = "12345"
   ```

3. Ejecute la API:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   El comando debe ejecutarse dentro de la carpeta `autos-colombia`.

Las migraciones de Flyway se aplican al iniciar la aplicación.

## Estructura del repositorio

```text
.
├── autos-colombia/                     # Backend Spring Boot
│   ├── src/main/java/                  # Código fuente Java
│   ├── src/main/resources/             # Configuración y migraciones Flyway
│   └── docker-compose.yml              # Servicio local MySQL
├── frontend/                            # Cliente web Angular y contenedor Docker
├── autos-colombia-diagrama-MER.png     # Modelo entidad-relación
├── autos-colombia-diagrama-fisico.png  # Modelo físico de datos
├── autos-colombia-script-inicial.sql   # Script SQL de referencia
└── architecture.png                    # Arquitectura propuesta
```

## Frontend dockerizado

El proyecto web está en `frontend/`; su contenedor se sirve en `http://localhost:4200` y tiene un límite de 256 MB de memoria.

Desde la raíz del repositorio se inicia con:

```powershell
docker compose up --build -d
```

Para consultar los registros usa `docker compose logs -f frontend`; para detener el contenedor usa `docker compose down`.

Las vistas operativas usan datos simulados hasta integrar la API de Jorge. Las capturas recibidas de Stitch se conservan en `docs/mockups/`.

## Próximos entregables de la iteración 1

- Requisitos funcionales y no funcionales.
- Historias de usuario y criterios de aceptación.
- Diagramas editables de casos de uso, clases y paquetes en StarUML.
- Mockups de las historias de usuario en Figma.
- Integración del frontend con los endpoints y reglas de negocio del backend.
- Pruebas de integración de entradas, salidas, celdas y novedades.

