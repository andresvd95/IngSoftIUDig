CREATE TABLE IF NOT EXISTS dummy (
        id INT NOT NULL AUTO_INCREMENT,
        name VARCHAR(250) NOT NULL,
        PRIMARY KEY (id)
);

-- ROLLBACK
-- DROP TABLE dummy;
-- DELETE FROM schema_version WHERE version = '1.1';