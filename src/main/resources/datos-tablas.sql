CREATE DATABASE IF NOT EXISTS desi2026;
USE desi2026;

CREATE TABLE IF NOT EXISTS provincia (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100),
    PRIMARY KEY (id),
    UNIQUE KEY uk_provincia_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS ciudad (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100),
    provincia_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_ciudad_provincia (provincia_id),
    CONSTRAINT fk_ciudad_provincia FOREIGN KEY (provincia_id) REFERENCES provincia (id)
);

CREATE TABLE IF NOT EXISTS persona (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    dni_cuit VARCHAR(11) NOT NULL,
    telefono VARCHAR(30),
    email VARCHAR(150),
    domicilio VARCHAR(200),
    ciudad_id BIGINT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_persona_dni_cuit (dni_cuit),
    KEY idx_persona_ciudad (ciudad_id),
    CONSTRAINT fk_persona_ciudad FOREIGN KEY (ciudad_id) REFERENCES ciudad (id)
);

CREATE TABLE IF NOT EXISTS propiedad (
    id BIGINT NOT NULL AUTO_INCREMENT,
    direccion VARCHAR(200) NOT NULL,
    ciudad_id BIGINT NOT NULL,
    tipo ENUM('CASA','DEPARTAMENTO','LOCAL','OTRO') NOT NULL,
    cantidad_ambientes INT NOT NULL,
    metros_cuadrados DECIMAL(12,2) NOT NULL,
    descripcion TEXT NOT NULL,
    comodidades TEXT,
    estado ENUM('DISPONIBLE','RESERVADA','ALQUILADA','INACTIVA') NOT NULL,
    propietario_id BIGINT NOT NULL,
    eliminado BIT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_propiedad_ciudad (ciudad_id),
    KEY idx_propiedad_propietario (propietario_id),
    CONSTRAINT fk_propiedad_ciudad FOREIGN KEY (ciudad_id) REFERENCES ciudad (id),
    CONSTRAINT fk_propiedad_propietario FOREIGN KEY (propietario_id) REFERENCES persona (id)
);

CREATE TABLE IF NOT EXISTS historial_estado_propiedad (
    id BIGINT NOT NULL AUTO_INCREMENT,
    propiedad_id BIGINT NOT NULL,
    estado ENUM('DISPONIBLE','RESERVADA','ALQUILADA','INACTIVA') NOT NULL,
    fecha_cambio DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_hist_propiedad_propiedad (propiedad_id),
    CONSTRAINT fk_hist_propiedad_propiedad FOREIGN KEY (propiedad_id) REFERENCES propiedad (id)
);

CREATE TABLE IF NOT EXISTS publicacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    propiedad_id BIGINT NOT NULL,
    precio_mensual DECIMAL(12,2) NOT NULL,
    condiciones_alquiler TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_publicacion DATE NOT NULL,
    estado ENUM('ACTIVA','PAUSADA','FINALIZADA') NOT NULL,
    eliminado BIT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_publicacion_propiedad (propiedad_id),
    CONSTRAINT fk_publicacion_propiedad FOREIGN KEY (propiedad_id) REFERENCES propiedad (id)
);

CREATE TABLE IF NOT EXISTS historial_estado_publicacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    publicacion_id BIGINT NOT NULL,
    estado ENUM('ACTIVA','PAUSADA','FINALIZADA') NOT NULL,
    fecha_cambio DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_hist_publicacion_publicacion (publicacion_id),
    CONSTRAINT fk_hist_publicacion_publicacion FOREIGN KEY (publicacion_id) REFERENCES publicacion (id)
);

CREATE TABLE IF NOT EXISTS visita (
    id BIGINT NOT NULL AUTO_INCREMENT,
    publicacion_id BIGINT NOT NULL,
    fecha_hora DATETIME(6) NOT NULL,
    estado ENUM('PENDIENTE','REALIZADA','CANCELADA') NOT NULL,
    PRIMARY KEY (id),
    KEY idx_visita_publicacion (publicacion_id),
    CONSTRAINT fk_visita_publicacion FOREIGN KEY (publicacion_id) REFERENCES publicacion (id)
);

CREATE TABLE IF NOT EXISTS contrato (
    id BIGINT NOT NULL AUTO_INCREMENT,
    propiedad_id BIGINT NOT NULL,
    inquilino_id BIGINT NOT NULL,
    fecha_inicio DATE NOT NULL,
    duracion_meses INT NOT NULL,
    importe_mensual DECIMAL(12,2) NOT NULL,
    dia_vencimiento_mensual INT NOT NULL,
    descripcion TEXT NOT NULL,
    estado ENUM('BORRADOR','ACTIVO','FINALIZADO','RESCINDIDO') NOT NULL,
    eliminado BIT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_contrato_propiedad (propiedad_id),
    KEY idx_contrato_inquilino (inquilino_id),
    CONSTRAINT fk_contrato_propiedad FOREIGN KEY (propiedad_id) REFERENCES propiedad (id),
    CONSTRAINT fk_contrato_inquilino FOREIGN KEY (inquilino_id) REFERENCES persona (id)
);

CREATE TABLE IF NOT EXISTS historial_estado_contrato (
    id BIGINT NOT NULL AUTO_INCREMENT,
    contrato_id BIGINT NOT NULL,
    estado ENUM('BORRADOR','ACTIVO','FINALIZADO','RESCINDIDO') NOT NULL,
    fecha_cambio DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_hist_contrato_contrato (contrato_id),
    CONSTRAINT fk_hist_contrato_contrato FOREIGN KEY (contrato_id) REFERENCES contrato (id)
);

CREATE TABLE IF NOT EXISTS factura (
    id BIGINT NOT NULL AUTO_INCREMENT,
    contrato_id BIGINT NOT NULL,
    concepto_facturado VARCHAR(200) NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    importe DECIMAL(12,2) NOT NULL,
    estado ENUM('PENDIENTE','PAGADA','VENCIDA','ANULADA') NOT NULL,
    fecha_pago DATE,
    medio_pago ENUM('TRANSFERENCIA','EFECTIVO','DEBITO','CREDITO'),
    importe_pagado DECIMAL(12,2),
    interes_pagado DECIMAL(12,2),
    eliminado BIT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_factura_contrato (contrato_id),
    CONSTRAINT fk_factura_contrato FOREIGN KEY (contrato_id) REFERENCES contrato (id)
);

CREATE TABLE IF NOT EXISTS historial_estado_factura (
    id BIGINT NOT NULL AUTO_INCREMENT,
    factura_id BIGINT NOT NULL,
    estado ENUM('PENDIENTE','PAGADA','VENCIDA','ANULADA') NOT NULL,
    fecha_cambio DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_hist_factura_factura (factura_id),
    CONSTRAINT fk_hist_factura_factura FOREIGN KEY (factura_id) REFERENCES factura (id)
);

CREATE TABLE IF NOT EXISTS incidente (
    id BIGINT NOT NULL AUTO_INCREMENT,
    contrato_id BIGINT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    categoria ENUM('PLOMERIA','ELECTRICIDAD','GAS','GENERAL') NOT NULL,
    fecha_alta DATETIME(6) NOT NULL,
    prioridad ENUM('BAJA','MEDIA','ALTA') NOT NULL,
    estado ENUM('ABIERTO','EN_PROCESO','RESUELTO','CANCELADO','REABIERTO') NOT NULL,
    eliminado BIT NOT NULL,
    fecha_resolucion DATETIME(6),
    observaciones_resolucion TEXT,
    costo_resolucion DECIMAL(12,2),
    responsable_tecnico VARCHAR(150),
    PRIMARY KEY (id),
    KEY idx_incidente_contrato (contrato_id),
    CONSTRAINT fk_incidente_contrato FOREIGN KEY (contrato_id) REFERENCES contrato (id)
);

CREATE TABLE IF NOT EXISTS historial_estado_incidente (
    id BIGINT NOT NULL AUTO_INCREMENT,
    incidente_id BIGINT NOT NULL,
    estado ENUM('ABIERTO','EN_PROCESO','RESUELTO','CANCELADO','REABIERTO') NOT NULL,
    fecha_hora DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_hist_incidente_incidente (incidente_id),
    CONSTRAINT fk_hist_incidente_incidente FOREIGN KEY (incidente_id) REFERENCES incidente (id)
);
