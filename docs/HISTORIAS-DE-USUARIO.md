# Historias de usuario (corregidas)

Versión ampliada de HU-01 y HU-02 del informe EA1, con la consulta de vehículos activos (HU-03) y los rechazos por
duplicidad, celda ocupada y salida sin ingreso abierto. Cada criterio indica la respuesta real del backend.

## HU-01 — Registrar ingreso de vehículo

**Como** operador del parqueadero, **quiero** digitar la placa de un vehículo que llega y asignarle una celda libre,
**para** registrar la hora de entrada y actualizar el estado de ocupación de las celdas.

Criterios de aceptación:

1. No se permite guardar si la placa está vacía o el vehículo no está registrado (`GET /vehicle?licensePlate=` → 404).
2. La interfaz solo ofrece celdas `Disponible` y compatibles con el tipo del vehículo.
3. Al guardar, la celda pasa a `Ocupada` de forma inmediata y la estancia queda persistida (sobrevive a recargar la página).
4. **Rechazo por duplicidad:** si el vehículo ya tiene una estancia abierta, se rechaza (HTTP 409,
   «El vehículo ya se encuentra dentro del parqueadero»).
5. **Rechazo por celda ocupada:** si la celda ya está ocupada, se rechaza (HTTP 409, «La celda X ya se encuentra ocupada»);
   también si su tipo no corresponde al del vehículo (HTTP 409).
6. Ante dos ingresos simultáneos sobre la misma celda o el mismo vehículo solo uno se acepta; el otro recibe HTTP 409
   (bloqueo pesimista `SELECT … FOR UPDATE` + índices únicos de la migración AC1.5).

## HU-02 — Registrar salida de vehículo

**Como** operador del parqueadero, **quiero** buscar la placa de un vehículo activo y marcar su salida,
**para** finalizar la permanencia y liberar el espacio utilizado.

Criterios de aceptación:

1. Al buscar la placa se muestran los datos de la estancia abierta (cliente, celda, hora de entrada).
2. **Rechazo por salida sin ingreso abierto:** si la placa no tiene una estancia abierta, la interfaz indica «No hay una estancia
   activa»; si se intenta cerrar una estancia inexistente el backend responde 404, y si ya estaba cerrada, 409
   («La estancia ya fue cerrada»).
3. Al confirmar, se registra la hora y el operador de salida (distinto del de entrada) y se libera la celda **real de la estancia**
   (la que está almacenada, no la enviada por el cliente).
4. La estancia cerrada pasa al historial (`GET /parking-session/history`), que se conserva tras recargar.
5. Dos salidas simultáneas de la misma estancia: solo una se acepta (HTTP 409 para la otra).

## HU-03 — Consultar vehículos activos (nueva)

**Como** operador del parqueadero, **quiero** ver en tiempo real todos los vehículos que permanecen dentro,
**para** conocer la ocupación y localizar rápidamente un vehículo.

Criterios de aceptación:

1. `GET /parking-session/active` devuelve solo las estancias sin salida, de la más reciente a la más antigua.
2. La vista «Vehículos dentro» muestra placa, vehículo, cliente, celda y hora de entrada, y se actualiza tras cada ingreso o salida.
3. El resumen operativo muestra el conteo de vehículos dentro y de celdas disponibles/ocupadas calculado desde el backend.
4. Cuando no hay vehículos dentro se muestra un estado vacío con acceso a «Registrar entrada».

## Contrato de la API (resumen)

| Método | Ruta | Respuestas |
| --- | --- | --- |
| `GET` | `/vehicle?licensePlate=` | 200 / 404 vehículo no registrado |
| `GET` | `/parking-spot?size=` | 200 |
| `GET` | `/parking-session/active` | 200 |
| `GET` | `/parking-session/history` | 200 |
| `POST` | `/parking-session/vehicle-entry` `{vehicleId, spotId, entryOperatorId}` | 200 / 400 validación / 404 / 409 duplicado, celda ocupada o tipo incompatible |
| `POST` | `/parking-session/vehicle-exit` `{id, exitOperatorId}` | 200 / 400 / 404 sin ingreso / 409 ya cerrada |
