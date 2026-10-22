# PRD del frontend de Autos Colombia

## Control del documento

| Campo | Valor |
| --- | --- |
| Producto | Sistema de información Autos Colombia |
| Entregable | Frontend de la Iteración 1 |
| Responsable | Andrés VD |
| Estado | Definición inicial |
| Alcance | Entrada y salida de vehículos |

## 1. Propósito

Construir una aplicación web responsive para que el operador del parqueadero registre y consulte las entradas y salidas de vehículos con rapidez y trazabilidad. El frontend será desarrollado en Angular, consumirá la API administrada por el responsable del backend y podrá ejecutarse mediante Docker.

La Iteración 1 se considera terminada cuando el operador puede registrar una entrada, consultar los vehículos presentes, revisar la disponibilidad de celdas, registrar una novedad y confirmar una salida desde escritorio o móvil.

## 2. Problema

El parqueadero necesita reemplazar registros manuales o dispersos por un flujo único que relacione cada vehículo con su cliente, su celda, el operador responsable, las horas de entrada y salida y las novedades ocurridas durante la estancia.

El operador necesita completar estas tareas mientras atiende vehículos. Por esta razón, la placa debe ser el dato principal de búsqueda, las acciones frecuentes deben estar disponibles desde el primer nivel y los errores deben explicar cómo continuar.

## 3. Usuarios

### Operador del parqueadero

Registra movimientos y novedades durante su turno. Necesita velocidad, confirmaciones claras y visibilidad inmediata sobre la disponibilidad de celdas.

### Administrador

En esta iteración puede consultar la misma información operativa. La administración de usuarios, tarifas, pagos y reportes avanzados queda fuera del alcance.

## 4. Objetivos del producto

- Reducir los pasos requeridos para registrar una entrada o salida.
- Evitar que un vehículo tenga más de una estancia activa.
- Evitar la asignación simultánea de una celda a dos vehículos.
- Permitir consultar la ocupación actual sin recargar manualmente la página.
- Registrar novedades dentro del contexto de una estancia activa.
- Mantener una experiencia utilizable en móvil, tablet y escritorio.

## 5. Alcance funcional

### Incluido

| Código | Capacidad | Resultado esperado |
| --- | --- | --- |
| FE-01 | Resumen operativo | Visualizar ocupación y últimos movimientos. |
| FE-02 | Registro de entrada | Buscar un vehículo, seleccionar una celda y confirmar la entrada. |
| FE-03 | Registro de salida | Buscar una estancia activa y confirmar la salida. |
| FE-04 | Vehículos dentro | Consultar, buscar y filtrar estancias activas. |
| FE-05 | Estado de celdas | Diferenciar celdas disponibles, ocupadas y fuera de servicio. |
| FE-06 | Registro de novedad | Asociar una descripción a un vehículo y su estancia activa. |
| FE-07 | Detalle de estancia | Consultar vehículo, propietario, celda, duración y novedades. |
| FE-08 | Respuesta adaptable | Usar los flujos principales desde 390 px hasta pantallas de escritorio. |

### Fuera del alcance

- Procesamiento de pagos.
- Gestión completa de clientes, operadores o tarifas.
- Recuperación de contraseña y administración de permisos.
- Carga real de fotografías o archivos.
- Lectura automática de placas.
- Reportes financieros o históricos avanzados.
- Aplicación móvil nativa.

La pantalla de inicio de sesión puede formar parte del mockup, pero su implementación depende de que el backend entregue autenticación y autorización.

## 6. Flujos principales

### Entrada de un vehículo

1. El operador abre Registrar entrada.
2. Ingresa la placa y solicita la búsqueda.
3. El sistema presenta el vehículo y valida que no tenga una estancia activa.
4. El operador selecciona una celda compatible y disponible.
5. Revisa el resumen y confirma.
6. El sistema muestra la hora de entrada y la celda asignada.

Si el vehículo no existe, el frontend mostrará una ruta para registrar datos básicos únicamente cuando el backend confirme que ese caso pertenece a la Iteración 1.

### Salida de un vehículo

1. El operador ingresa la placa.
2. El sistema presenta la estancia activa y sus novedades.
3. El operador verifica la información y confirma la salida.
4. El sistema informa la hora de salida y que la celda fue liberada.

### Registro de una novedad

1. El operador abre una estancia activa.
2. Selecciona Registrar novedad.
3. El sistema conserva visible el contexto de placa y celda.
4. El operador selecciona una categoría y describe lo ocurrido.
5. El sistema confirma el registro y actualiza la línea de tiempo.

## 7. Requisitos de experiencia

- La placa debe mostrarse en mayúsculas y ser el identificador visual principal.
- Las acciones Registrar entrada y Registrar salida deben estar disponibles desde el resumen.
- Los formularios no deben perder la información ingresada ante un error recuperable.
- Toda operación que modifique datos requiere confirmación visible.
- Los estados no pueden comunicarse solamente mediante color.
- Los errores deben indicar el problema y una acción de recuperación.
- En móvil, las tablas deben transformarse en una presentación legible sin depender de desplazamiento horizontal.
- Los controles táctiles deben tener al menos 44 por 44 px.

## 8. Estados obligatorios

| Estado | Comportamiento esperado |
| --- | --- |
| Cargando | Mantener estable la estructura e indicar progreso. |
| Vacío | Explicar que no existen registros y ofrecer la acción siguiente. |
| Error | Mostrar una causa comprensible y permitir reintentar. |
| Parcial | Presentar la información disponible e identificar lo no cargado. |
| Éxito | Confirmar el resultado y mostrar el dato clave de la operación. |
| Sin permiso | Explicar la restricción cuando exista autenticación por roles. |

## 9. Diseño responsive y accesibilidad

Los mockups y la implementación se validarán en 390 x 844 px, 768 x 1024 px y 1440 x 900 px. La interfaz cumplirá contraste AA, navegación mediante teclado, foco visible, etiquetas asociadas a campos, mensajes de validación comprensibles y estructura semántica.

## 10. Dependencias del backend

El frontend necesita que Jorge confirme y publique:

- URL base y política de versiones de la API.
- Formatos JSON de solicitudes, respuestas y errores.
- Consulta de vehículo por placa.
- Consulta de estancias activas.
- Creación de entrada y confirmación de salida.
- Consulta de celdas y su disponibilidad.
- Registro y consulta de novedades.
- Reglas de negocio para mensualidad vencida y vehículo no registrado.
- Configuración CORS para el origen del frontend.
- Mecanismo de autenticación, si se incluye en la Iteración 1.

## 11. Criterios de aceptación del frontend

- Los flujos pueden demostrarse inicialmente con datos simulados y posteriormente con la API real sin reemplazar los componentes de pantalla.
- Una entrada exitosa actualiza las vistas de vehículos dentro y celdas.
- Una salida exitosa elimina la estancia de la lista activa y libera visualmente la celda.
- Las validaciones evitan enviar formularios incompletos.
- Los errores HTTP se traducen en mensajes comprensibles.
- Ninguna pantalla principal presenta desbordamiento horizontal en los tres tamaños objetivo.
- La aplicación puede construirse y servirse dentro de un contenedor Docker.

## 12. Plan de trabajo

### Fase 1 Diseño y contrato

- Generar mockups en Stitch.
- Revisar navegación, formularios y estados.
- Acordar el contrato de API con el backend.

### Fase 2 Base del frontend

- Crear el proyecto Angular.
- Definir rutas, tema, layout y componentes compartidos.
- Implementar una fuente de datos simulada detrás de servicios.

### Fase 3 Flujos de la iteración

- Construir entrada, salida, vehículos activos, celdas, detalle y novedades.
- Integrar la API a medida que los endpoints estén disponibles.

### Fase 4 Calidad y contenedores

- Implementar pruebas unitarias y de integración de los flujos críticos.
- Verificar accesibilidad y comportamiento responsive.
- Construir la imagen Docker del frontend.
- Integrar frontend, backend y MySQL mediante Docker Compose.

## 13. Riesgos y decisiones pendientes

| Riesgo o decisión | Tratamiento |
| --- | --- |
| El backend todavía no expone endpoints | Desarrollar contra servicios simulados con las mismas interfaces previstas. |
| El contrato puede cambiar | Centralizar modelos y adaptadores; no consumir respuestas HTTP directamente desde componentes. |
| Seguridad JWT aún no existe | Mantener autenticación desacoplada y no bloquear los flujos de la iteración. |
| Diferencias entre modelo y reglas reales | Validar con Jorge antes de cerrar los formularios. |
| Docker requiere coordinación entre proyectos | Usar nombres de servicio y variables de entorno, sin direcciones IP fijas. |

## 14. Indicadores de finalización

- Todos los flujos incluidos tienen pantalla, estados y navegación funcional.
- Los seis casos principales FE-01 a FE-06 consumen datos simulados o reales mediante servicios.
- Los tamaños objetivo fueron revisados sin bloqueos de uso.
- El contenedor del frontend responde correctamente y puede comunicarse con la API por configuración.
- El README contiene instrucciones actualizadas para levantar el sistema.

