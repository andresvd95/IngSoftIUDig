-- Garantiza a nivel de base de datos que un vehiculo y una celda solo pueden tener
-- UNA estancia abierta (exit_time IS NULL). Respaldo de la validacion en la aplicacion
-- ante solicitudes simultaneas. Las columnas generadas valen NULL cuando la estancia
-- esta cerrada, y MySQL permite multiples NULL en un indice UNIQUE.
ALTER TABLE parking_session
    ADD COLUMN open_vehicle_id INT GENERATED ALWAYS AS (IF(exit_time IS NULL, vehicle_id, NULL)) STORED,
    ADD COLUMN open_spot_id INT GENERATED ALWAYS AS (IF(exit_time IS NULL, spot_id, NULL)) STORED,
    ADD CONSTRAINT uq_session_open_vehicle UNIQUE (open_vehicle_id),
    ADD CONSTRAINT uq_session_open_spot UNIQUE (open_spot_id);

-- ROLLBACK
-- ALTER TABLE parking_session DROP INDEX uq_session_open_vehicle, DROP INDEX uq_session_open_spot,
--     DROP COLUMN open_vehicle_id, DROP COLUMN open_spot_id;
-- DELETE FROM schema_version WHERE version = '1.5';
