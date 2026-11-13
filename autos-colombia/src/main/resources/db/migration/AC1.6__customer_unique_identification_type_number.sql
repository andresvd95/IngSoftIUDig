-- La identificacion de un cliente se identifica por tipo + numero (CC 123 y CE 123 son personas distintas).
-- AC1.3 declaro UNIQUE solo sobre identification_number (indice con ese mismo nombre en MySQL);
-- se reemplaza por un indice unico compuesto. vehicle.license_plate y parking_spot.spot_number
-- ya son UNIQUE desde AC1.3, por lo que no requieren cambios.
ALTER TABLE customer
    DROP INDEX identification_number,
    ADD CONSTRAINT uq_customer_identification UNIQUE (identification_type, identification_number);

-- ROLLBACK
-- ALTER TABLE customer DROP INDEX uq_customer_identification, ADD CONSTRAINT identification_number UNIQUE (identification_number);
-- DELETE FROM schema_version WHERE version = '1.6';
