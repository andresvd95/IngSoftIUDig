CREATE TABLE customer (
    id INT NOT NULL AUTO_INCREMENT,
    identification_number VARCHAR(20) UNIQUE NOT NULL,
    identification_type VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    created_date DATETIME NOT NULL,
    last_modified_date DATETIME NOT NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE operator (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    identification_number VARCHAR(20) UNIQUE NOT NULL,
    identification_type VARCHAR(10) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    created_date DATETIME NOT NULL,
    last_modified_date DATETIME NOT NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE vehicle_type (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    monthly_rate DECIMAL(10, 2) NOT NULL,
    created_date DATETIME NOT NULL,
    last_modified_date DATETIME NOT NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE parking_spot (
    id INT NOT NULL AUTO_INCREMENT,
    spot_number VARCHAR(20) UNIQUE NOT NULL,
    is_available BIT(1) DEFAULT b'0',
    vehicle_type_id INT NOT NULL,
    created_date DATETIME NOT NULL,
    last_modified_date DATETIME NOT NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT fk_spot_vehicle_type FOREIGN KEY (vehicle_type_id) REFERENCES vehicle_type(id)
);

CREATE TABLE vehicle (
    id INT NOT NULL AUTO_INCREMENT,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    brand VARCHAR(50),
    model INT,
    color VARCHAR(50),
    vehicle_type_id INT NOT NULL,
    customer_id INT NOT NULL,
    created_date DATETIME NOT NULL,
    last_modified_date DATETIME NOT NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT fk_vehicle_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_vehicle_type FOREIGN KEY (vehicle_type_id) REFERENCES vehicle_type(id)
);

CREATE TABLE subscription (
    id INT NOT NULL AUTO_INCREMENT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    customer_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    created_date DATETIME NOT NULL,
    last_modified_date DATETIME NOT NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT fk_sub_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_sub_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE parking_session (
    id INT NOT NULL AUTO_INCREMENT,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME,
    vehicle_id INT NOT NULL,
    spot_id INT NOT NULL,
    entry_operator_id INT NOT NULL,
    exit_operator_id INT,
    created_date DATETIME NOT NULL,
    last_modified_date DATETIME NOT NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT fk_session_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    CONSTRAINT fk_session_spot FOREIGN KEY (spot_id) REFERENCES parking_spot(id),
    CONSTRAINT fk_session_entry_op FOREIGN KEY (entry_operator_id) REFERENCES operator(id),
    CONSTRAINT fk_session_exit_op FOREIGN KEY (exit_operator_id) REFERENCES operator(id)
);

CREATE TABLE incident (
    id INT NOT NULL AUTO_INCREMENT,
    description TEXT NOT NULL,
    incident_time DATETIME NOT NULL,
    vehicle_id INT NOT NULL,
    operator_id INT NOT NULL,
    parking_session_id INT,
    created_date DATETIME NOT NULL,
    last_modified_date DATETIME NOT NULL,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT fk_incident_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    CONSTRAINT fk_incident_operator FOREIGN KEY (operator_id) REFERENCES operator(id),
    CONSTRAINT fk_incident_session FOREIGN KEY (parking_session_id) REFERENCES parking_session(id)
);

-- ROLLBACK
-- DROP TABLE incident;
-- DROP TABLE parking_session;
-- DROP TABLE subscription;
-- DROP TABLE vehicle;
-- DROP TABLE parking_spot;
-- DROP TABLE vehicle_type;
-- DROP TABLE operator;
-- DROP TABLE customer;
-- DELETE FROM schema_version WHERE version = '1.3';