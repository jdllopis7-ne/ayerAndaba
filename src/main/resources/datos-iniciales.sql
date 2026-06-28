CREATE DATABASE IF NOT EXISTS desi2026;
USE desi2026;

INSERT INTO provincia (id, nombre) VALUES
(1, 'Santa Fe'),
(2, 'Cordoba')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO ciudad (id, nombre, provincia_id) VALUES
(1, 'Santa Fe', 1),
(2, 'Rosario', 1),
(3, 'Cordoba', 2)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), provincia_id = VALUES(provincia_id);

INSERT INTO persona (id, apellido, dni_cuit, domicilio, email, nombre, telefono, ciudad_id) VALUES
(1, 'Perez', '20111222', 'San Martin 1000', 'propietario@example.com', 'Juan', '3425551111', 1),
(2, 'Gomez', '27888999', 'Belgrano 2000', 'inquilino@example.com', 'Ana', '3425552222', 2)
ON DUPLICATE KEY UPDATE apellido = VALUES(apellido), nombre = VALUES(nombre), telefono = VALUES(telefono);
