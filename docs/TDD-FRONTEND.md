# TDD del frontend de Autos Colombia

## Control del documento

| Campo | Valor |
| --- | --- |
| Componente | Cliente web de Autos Colombia |
| Responsable | Andrés VD |
| Tecnología | Angular y TypeScript |
| Despliegue | Contenedor Docker con servidor web |
| Estado | Diseño técnico inicial |

## 1. Objetivo técnico

Implementar un frontend Angular modular, responsive y comprobable para los flujos de entrada y salida de vehículos. La arquitectura debe permitir trabajar con datos simulados mientras Jorge completa la API y sustituirlos posteriormente sin reescribir las pantallas.

## 2. Principios

- Componentes de presentación sin conocimiento del transporte HTTP.
- Servicios por dominio como frontera entre pantallas y API.
- Tipos explícitos para solicitudes, respuestas y errores.
- Estado local o compartido únicamente donde sea necesario.
- Rutas cargadas de forma diferida por funcionalidad.
- Configuración externa para la URL de la API.
- Accesibilidad y responsive como criterios de terminación.
- Dependencias adicionales solo cuando resuelvan una necesidad comprobada.

## 3. Tecnologías previstas

| Área | Selección |
| --- | --- |
| Framework | Angular estable con componentes standalone |
| Lenguaje | TypeScript con modo estricto |
| Estilos | SCSS y variables de diseño propias |
| Formularios | Angular Reactive Forms |
| HTTP | HttpClient e interceptores funcionales |
| Estado | Signals y servicios; sin librería global en la Iteración 1 |
| Pruebas unitarias | Herramienta configurada por Angular y pruebas de componentes |
| Pruebas de flujo | Playwright, si el tiempo de la iteración lo permite |
| Producción | Compilación estática servida por Nginx |
| Contenedores | Docker y Docker Compose |

La versión exacta de Angular se fijará al crear el proyecto y se conservará en el archivo de dependencias. No se actualizará durante la iteración salvo por una corrección necesaria.

## 4. Arquitectura del frontend

```text
Navegador
   |
Angular Router
   |
Layout operativo
   |
Funciones de dominio
   |-- dashboard
   |-- parking-entry
   |-- parking-exit
   |-- active-sessions
   |-- parking-spots
   `-- incidents
   |
Servicios de aplicación
   |-- implementación HTTP
   `-- implementación simulada
   |
API Spring Boot de Autos Colombia
```

Los componentes consumen interfaces de servicio. La selección entre datos simulados y API real se realizará mediante configuración e inyección de dependencias.

## 5. Estructura propuesta

```text
frontend/
├── src/app/
│   ├── core/
│   │   ├── config/
│   │   ├── http/
│   │   ├── layout/
│   │   └── services/
│   ├── shared/
│   │   ├── components/
│   │   ├── models/
│   │   ├── pipes/
│   │   └── validators/
│   ├── features/
│   │   ├── dashboard/
│   │   ├── parking-entry/
│   │   ├── parking-exit/
│   │   ├── active-sessions/
│   │   ├── parking-spots/
│   │   └── incidents/
│   ├── app.config.ts
│   └── app.routes.ts
├── public/
├── Dockerfile
├── nginx.conf
└── package.json
```

## 6. Rutas

| Ruta | Pantalla | Protección prevista |
| --- | --- | --- |
| `/login` | Inicio de sesión | Pública, si se implementa seguridad |
| `/dashboard` | Resumen operativo | Operador |
| `/entradas/nueva` | Registrar entrada | Operador |
| `/salidas/nueva` | Registrar salida | Operador |
| `/estancias` | Vehículos dentro | Operador |
| `/estancias/:id` | Detalle de estancia | Operador |
| `/celdas` | Estado de celdas | Operador |

El registro de novedades se abrirá desde una estancia activa como diálogo o panel lateral; no necesita una ruta independiente.

## 7. Modelos de interfaz

```typescript
type ParkingSpotStatus = 'AVAILABLE' | 'OCCUPIED' | 'OUT_OF_SERVICE';

interface VehicleSummary {
  id: number;
  licensePlate: string;
  brand: string | null;
  model: number | null;
  color: string | null;
  vehicleType: VehicleTypeSummary;
  customer: CustomerSummary;
}

interface ParkingSession {
  id: number;
  vehicle: VehicleSummary;
  parkingSpot: ParkingSpotSummary;
  entryTime: string;
  exitTime: string | null;
  entryOperatorName: string;
  incidents: IncidentSummary[];
}

interface CreateEntryRequest {
  vehicleId: number;
  parkingSpotId: number;
  observation?: string;
}

interface CreateExitRequest {
  parkingSessionId: number;
}

interface CreateIncidentRequest {
  parkingSessionId: number;
  category: string;
  description: string;
}
```

Los nombres definitivos deben alinearse con el contrato entregado por el backend. El frontend normalizará las respuestas HTTP en un adaptador si la API usa una estructura diferente.

## 8. Contrato mínimo propuesto para el backend

Este contrato es una propuesta para acordar con Jorge; no describe endpoints que ya existan.

| Método | Ruta propuesta | Uso |
| --- | --- | --- |
| GET | `/api/v1/vehicles/by-plate/{plate}` | Buscar vehículo por placa. |
| GET | `/api/v1/parking-sessions/active` | Listar estancias activas y aplicar filtros. |
| GET | `/api/v1/parking-sessions/active/by-plate/{plate}` | Consultar estancia activa por placa. |
| GET | `/api/v1/parking-sessions/{id}` | Consultar detalle de estancia. |
| POST | `/api/v1/parking-sessions/entries` | Registrar entrada. |
| PATCH | `/api/v1/parking-sessions/{id}/exit` | Confirmar salida. |
| GET | `/api/v1/parking-spots` | Consultar celdas por estado o tipo. |
| POST | `/api/v1/parking-sessions/{id}/incidents` | Registrar novedad. |
| GET | `/api/v1/parking-sessions/{id}/incidents` | Consultar novedades. |
| GET | `/api/v1/dashboard/summary` | Consultar resumen operativo. |

### Respuesta de error propuesta

```json
{
  "timestamp": "2026-10-21T13:35:00Z",
  "status": 409,
  "code": "VEHICLE_ALREADY_INSIDE",
  "message": "El vehículo ya tiene una estancia activa",
  "fieldErrors": []
}
```

Códigos de negocio mínimos: `VEHICLE_NOT_FOUND`, `VEHICLE_ALREADY_INSIDE`, `ACTIVE_SESSION_NOT_FOUND`, `SPOT_NOT_AVAILABLE`, `SPOT_TYPE_MISMATCH`, `SUBSCRIPTION_EXPIRED` y `VALIDATION_ERROR`.

## 9. Servicios del frontend

```typescript
interface ParkingOperationsService {
  findVehicleByPlate(plate: string): Observable<VehicleSummary>;
  getActiveSessionByPlate(plate: string): Observable<ParkingSession>;
  listActiveSessions(filters: SessionFilters): Observable<ParkingSession[]>;
  registerEntry(request: CreateEntryRequest): Observable<ParkingSession>;
  registerExit(request: CreateExitRequest): Observable<ParkingSession>;
  listParkingSpots(filters: ParkingSpotFilters): Observable<ParkingSpotSummary[]>;
  registerIncident(request: CreateIncidentRequest): Observable<IncidentSummary>;
}
```

Se implementarán `HttpParkingOperationsService` y `MockParkingOperationsService`. Ambos deben respetar la misma interfaz.

## 10. Gestión de estado

Cada funcionalidad mantendrá su estado de vista con signals: datos, filtros, indicador de carga, error y resultado de la última operación. El resumen de ocupación y la lista de estancias compartirán un servicio de actualización para reflejar entradas y salidas sin duplicar solicitudes innecesarias.

No se incorporará una librería de estado global en esta iteración. Se reconsiderará si aparecen flujos concurrentes, caché compleja o edición distribuida.

## 11. Formularios y validación

- La placa se normaliza con `trim()` y mayúsculas antes de consultar.
- Se valida un formato colombiano flexible de cinco o seis caracteres alfanuméricos, sin asumir que todos los vehículos usan el mismo patrón.
- La celda es obligatoria para confirmar una entrada.
- La categoría y descripción son obligatorias para registrar una novedad.
- La descripción tendrá límites visibles de longitud acordados con el backend.
- Los botones de confirmación se deshabilitan durante el envío para evitar operaciones duplicadas.
- Las respuestas 409 deben presentarse como conflictos de negocio, no como fallas técnicas genéricas.

## 12. HTTP y autenticación

Un interceptor agregará encabezados comunes, correlación y token Bearer cuando el backend implemente autenticación. Otro interceptor traducirá errores técnicos al modelo de error de la aplicación.

La URL base no debe quedar compilada directamente en los servicios. Se obtendrá desde una configuración cargada al iniciar la aplicación, por ejemplo `/assets/config.json`, para usar la misma imagen Docker en diferentes entornos.

## 13. Diseño responsive

- Puntos de verificación: 390 x 844, 768 x 1024 y 1440 x 900.
- Escritorio: barra lateral, contenido amplio y tablas.
- Tablet: navegación compacta y cuadrículas reducidas.
- Móvil: navegación desplegable o inferior, formularios de una columna y registros apilados.
- La acción principal permanece visible sin ocultar el contenido.
- La cuadrícula de celdas ajusta columnas automáticamente y conserva el código de cada celda legible.

## 14. Accesibilidad

- HTML semántico y orden lógico de encabezados.
- Etiquetas programáticas para controles.
- Foco visible y orden de teclado predecible.
- Diálogos con captura y retorno de foco.
- Contraste mínimo WCAG AA.
- Mensajes de estado anunciados mediante regiones `aria-live` cuando corresponda.
- Texto e icono como mínimo para distinguir estados de celda.
- Respeto por la preferencia de movimiento reducido.

## 15. Docker

El frontend usará una compilación en dos etapas:

1. Una imagen de Node compila la aplicación Angular.
2. Una imagen de Nginx sirve los archivos estáticos.

Nginx atenderá el frontend en el puerto 80 del contenedor, resolverá las rutas de Angular hacia `index.html` y podrá actuar como proxy de `/api` hacia el servicio del backend. En Docker Compose, los servicios previstos serán `frontend`, `backend` y `mysql-db`. La comunicación usará nombres de servicio, no `localhost` entre contenedores.

El Dockerfile, `nginx.conf` y el Compose integrado se implementarán cuando se cree el proyecto Angular, porque la ruta exacta de salida depende del nombre y configuración generados.

## 16. Estrategia de pruebas

| Nivel | Cobertura mínima |
| --- | --- |
| Unitaria | Normalización de placas, validadores, mapeadores y servicios de estado. |
| Componentes | Formularios, estados de carga, errores y confirmaciones. |
| Integración HTTP | Contratos de servicios con respuestas exitosas y errores de negocio. |
| Flujo | Entrada, novedad y salida de un vehículo. |
| Responsive | Tres tamaños objetivo y contenido con textos largos. |
| Accesibilidad | Teclado, foco, etiquetas, contraste y anuncios de estado. |

## 17. Observabilidad y manejo de errores

Los errores se clasificarán en validación local, conflicto de negocio, autorización, conectividad y error inesperado. En desarrollo se podrá registrar información técnica en consola; en producción no se mostrarán trazas ni datos sensibles al usuario.

Las solicitudes podrán incluir un identificador de correlación cuando el backend lo soporte. El frontend mostrará una opción de reintento en consultas fallidas, pero no repetirá automáticamente operaciones de escritura.

## 18. Seguridad

- No almacenar contraseñas ni información sensible en el frontend.
- Preferir token en memoria; la estrategia definitiva se acordará con el backend.
- Escapar contenido dinámico y evitar HTML no confiable.
- No incluir secretos en variables compiladas, repositorio o imagen Docker.
- Aplicar encabezados de seguridad desde Nginx.
- Validar en el backend todas las reglas aunque también existan validaciones visuales.

## 19. Secuencia de implementación

1. Crear el proyecto Angular y fijar versiones.
2. Configurar rutas, tema, layout y componentes compartidos.
3. Definir modelos, interfaces de servicio y datos simulados.
4. Implementar resumen, entrada y salida.
5. Implementar estancias activas, celdas, detalle y novedades.
6. Alinear adaptadores con el contrato real de la API.
7. Agregar pruebas y revisar accesibilidad y responsive.
8. Crear Dockerfile, Nginx y Compose integrado.
9. Actualizar README con ejecución local y contenedores.

## 20. Condiciones para iniciar la construcción

- Mockups aprobados para escritorio y móvil.
- Navegación y componentes compartidos identificados.
- Contrato mínimo de API revisado con Jorge, aunque los endpoints aún estén en construcción.
- Decisión sobre inclusión o exclusión de autenticación en la Iteración 1.
- Nombre definitivo de la carpeta y aplicación Angular.

