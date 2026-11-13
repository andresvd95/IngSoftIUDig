# Plan de pruebas resumido. Resultado obtenido: ejecución de `./mvnw test` (ManagementApiTest 13/13, ParkingSessionFlowTest 7/7).
M = "ManagementApiTest"
OK = "Aprobada"
PRUEBAS = [
    ("PP-01", "HU-04", "Registrar cliente válido", "CC 1020304050, María Gómez, correo y teléfono", "200 y el cliente se puede consultar", f"{M}.createsCustomerAndReadsItBack", OK),
    ("PP-02", "HU-04", "Cliente con datos inválidos", "Nombre vacío / correo mal formado", "400 con mensajes de validación", f"{M}.rejectsInvalidCustomerWith400", OK),
    ("PP-03", "HU-04", "Cliente duplicado", "Mismo tipo y número de identificación; mismo número con otro tipo", "409 al duplicar; con otro tipo se acepta", f"{M}.rejectsDuplicateCustomerIdentificationWith409ButAllowsOtherType", OK),
    ("PP-04", "HU-05", "Buscar, editar cliente", "Búsqueda por nombre; edición propia, con choque y con id inexistente", "Filtra el listado; 200 / 409 / 404", f"{M}.createsCustomerAndReadsItBack · updatesCustomerAndRejectsCollisionAndMissing", OK),
    ("PP-05", "HU-05", "Eliminar cliente", "Cliente sin estancias; luego repetir", "204 y después 404", f"{M}.deletesCustomerWith204AndThen404", OK),
    ("PP-06", "HU-05", "Eliminar cliente con vehículo dentro", "Cliente con estancia abierta", "409 y el cliente se conserva", f"{M}.blocksCustomerDeletionWhenAVehicleIsInside", OK),
    ("PP-07", "HU-06", "Registrar vehículo y listar por cliente", "Placa en minúsculas, tipo y cliente válidos", "200, placa normalizada a mayúsculas, aparece en el cliente", f"{M}.createsVehicleNormalizesPlateAndListsByCustomer", OK),
    ("PP-08", "HU-06", "Placa duplicada", "Placa ya registrada", "409", f"{M}.rejectsDuplicatePlateWith409", OK),
    ("PP-09", "HU-06", "Vehículo con cliente o tipo inexistente / cuerpo inválido", "customerId o vehicleTypeId inexistente", "404; 400 si el cuerpo es inválido", f"{M}.rejectsVehicleWithMissingCustomerOrTypeOrInvalidBody", OK),
    ("PP-10", "HU-07", "Crear, editar y eliminar celda", "Número A-12, tipo Carro", "Celda creada disponible; edición y borrado correctos", f"{M}.createsEditsAndDeletesSpot", OK),
    ("PP-11", "HU-07", "Celda inválida o duplicada", "Sin número / sin tipo; número repetido", "400; 409", f"{M}.rejectsInvalidSpotWith400AndDuplicateWith409", OK),
    ("PP-12", "HU-08", "Editar celda con choque o inexistente", "Número de otra celda; id inexistente", "409; 404", f"{M}.rejectsSpotEditCollisionAndMissing", OK),
    ("PP-13", "HU-08", "Editar o eliminar celda ocupada", "Celda con estancia abierta", "409 y el flujo de ingreso/salida sigue funcionando", f"{M}.rejectsEditAndDeletionOfOccupiedSpotAndFlowStillWorks", OK),
    ("PP-14", "HU-09", "Estado de celdas tras ingreso y salida", "Ingreso y salida de un vehículo", "La celda pasa a Ocupada y vuelve a Disponible", "ParkingSessionFlowTest (7 pruebas)", OK),
]
