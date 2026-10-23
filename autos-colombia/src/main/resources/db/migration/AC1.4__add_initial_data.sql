INSERT INTO vehicle_type (name, description, monthly_rate, created_date, last_modified_date, created_by, updated_by) VALUES
('CARRO', 'Vehículo particular estándar', 150000.00, NOW(), NOW(), 'FLYWAY', 'FLYWAY'),
('MOTO', 'Motocicleta de dos ruedas', 70000.00, NOW(), NOW(), 'FLYWAY', 'FLYWAY');

INSERT INTO customer (identification_number, identification_type, name, email, phone, created_date, last_modified_date, created_by, updated_by) VALUES
('1020304050', 'CC', 'Juan Perez', 'juan.perez@email.com', '3001234567', NOW(), NOW(), 'FLYWAY', 'FLYWAY'),
('900800700', 'NIT', 'Empresa Tech SAS', 'contacto@techsas.com', '3109876543', NOW(), NOW(), 'FLYWAY', 'FLYWAY');

INSERT INTO operator (name, identification_number, identification_type, username, created_date, last_modified_date, created_by, updated_by) VALUES
('Carlos Gomez', '1122334455', 'CC', 'cgomez', NOW(), NOW(), 'FLYWAY', 'FLYWAY');

INSERT INTO parking_spot (spot_number, is_available, vehicle_type_id, created_date, last_modified_date, created_by, updated_by) VALUES
('A-01', 1, 1, NOW(), NOW(), 'FLYWAY', 'FLYWAY'),
('M-01', 0, 2, NOW(), NOW(), 'FLYWAY', 'FLYWAY');

INSERT INTO vehicle (license_plate, brand, model, color, vehicle_type_id, customer_id, created_date, last_modified_date, created_by, updated_by) VALUES
('ABC-123', 'Mazda', 2020, 'Rojo', 1, 1, NOW(), NOW(), 'FLYWAY', 'FLYWAY'),
('XYZ-99D', 'Yamaha', 2023, 'Negro', 2, 2, NOW(), NOW(), 'FLYWAY', 'FLYWAY');

INSERT INTO subscription (start_date, end_date, amount, status, customer_id, vehicle_id, created_date, last_modified_date, created_by, updated_by) VALUES
('2023-10-01', '2023-10-31', 150000.00, 'ACTIVE', 1, 1, NOW(), NOW(), 'FLYWAY', 'FLYWAY'),
('2023-09-01', '2023-09-30', 70000.00, 'EXPIRED', 2, 2, NOW(), NOW(), 'FLYWAY', 'FLYWAY');

INSERT INTO parking_session (entry_time, exit_time, vehicle_id, spot_id, entry_operator_id, exit_operator_id, created_date, last_modified_date, created_by, updated_by) VALUES
('2023-10-15 08:00:00', '2023-10-15 18:00:00', 1, 1, 1, 1, NOW(), NOW(), 'FLYWAY', 'FLYWAY'),
('2023-10-23 07:30:00', NULL, 2, 2, 1, NULL, NOW(), NOW(), 'FLYWAY', 'FLYWAY');

INSERT INTO incident (description, incident_time, vehicle_id, operator_id, parking_session_id, created_date, last_modified_date, created_by, updated_by) VALUES
('Vehículo ingresó con un rayón en la puerta derecha', '2023-10-15 08:05:00', 1, 1, 1, NOW(), NOW(), 'FLYWAY', 'FLYWAY');

-- ROLLBACK
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE incident;
-- TRUNCATE TABLE parking_session;
-- TRUNCATE TABLE subscription;
-- TRUNCATE TABLE vehicle;
-- TRUNCATE TABLE parking_spot;
-- TRUNCATE TABLE operator;
-- TRUNCATE TABLE customer;
-- TRUNCATE TABLE vehicle_type;
-- SET FOREIGN_KEY_CHECKS = 1;
-- DELETE FROM schema_version WHERE version = '1.4';