-- Esquema mejorado: veterinaria
-- Compatible con MariaDB 10.4+ / MySQL 8+
-- Mejoras: normalización, integridad, índices, tablas lookup, auditoría

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

START TRANSACTION;

DROP DATABASE IF EXISTS `veterinaria`;
CREATE DATABASE `veterinaria`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `veterinaria`;

-- --------------------------------------------------------
-- Tablas lookup
-- --------------------------------------------------------

CREATE TABLE `estado_turno` (
  `id_estado_turno` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id_estado_turno`),
  UNIQUE KEY `uk_estado_turno_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `metodo_pago` (
  `id_metodo_pago` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_metodo_pago`),
  UNIQUE KEY `uk_metodo_pago_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tipo_pago` (
  `id_tipo_pago` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_tipo_pago`),
  UNIQUE KEY `uk_tipo_pago_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tipo_vacuna` (
  `id_tipo_vacuna` SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id_tipo_vacuna`),
  UNIQUE KEY `uk_tipo_vacuna_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Entidades principales
-- --------------------------------------------------------

CREATE TABLE `cliente` (
  `id_cliente` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `telefono` VARCHAR(20) DEFAULT NULL,
  `direccion` VARCHAR(200) DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `veterinario` (
  `id_veterinario` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `especialidad` VARCHAR(100) DEFAULT NULL,
  `telefono` VARCHAR(20) DEFAULT NULL,
  `correo` VARCHAR(100) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_veterinario`),
  UNIQUE KEY `uk_veterinario_correo` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `mascota` (
  `id_mascota` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `especie` VARCHAR(50) NOT NULL,
  `raza` VARCHAR(50) DEFAULT NULL,
  `fecha_nacimiento` DATE DEFAULT NULL,
  `id_cliente` INT UNSIGNED NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_mascota`),
  KEY `idx_mascota_cliente` (`id_cliente`),
  CONSTRAINT `fk_mascota_cliente`
    FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `medicamento` (
  `id_medicamento` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(500) DEFAULT NULL,
  `precio` DECIMAL(10,2) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_medicamento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `turno` (
  `id_turno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fecha` DATE NOT NULL,
  `hora` TIME NOT NULL,
  `id_estado_turno` TINYINT UNSIGNED NOT NULL DEFAULT 1,
  `id_mascota` INT UNSIGNED NOT NULL,
  `id_veterinario` INT UNSIGNED NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_turno`),
  UNIQUE KEY `uk_turno_vet_fecha_hora` (`id_veterinario`, `fecha`, `hora`),
  KEY `idx_turno_mascota` (`id_mascota`),
  KEY `idx_turno_estado_fecha` (`id_estado_turno`, `fecha`),
  KEY `idx_turno_vet_fecha` (`id_veterinario`, `fecha`, `hora`),
  CONSTRAINT `fk_turno_mascota`
    FOREIGN KEY (`id_mascota`) REFERENCES `mascota` (`id_mascota`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_turno_veterinario`
    FOREIGN KEY (`id_veterinario`) REFERENCES `veterinario` (`id_veterinario`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_turno_estado`
    FOREIGN KEY (`id_estado_turno`) REFERENCES `estado_turno` (`id_estado_turno`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `consulta` (
  `id_consulta` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `diagnostico` VARCHAR(300) NOT NULL,
  `tratamiento` VARCHAR(500) DEFAULT NULL,
  `observaciones` VARCHAR(500) DEFAULT NULL,
  `id_turno` INT UNSIGNED NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_consulta`),
  UNIQUE KEY `uk_consulta_turno` (`id_turno`),
  CONSTRAINT `fk_consulta_turno`
    FOREIGN KEY (`id_turno`) REFERENCES `turno` (`id_turno`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `consulta_medicamento` (
  `id_consulta` INT UNSIGNED NOT NULL,
  `id_medicamento` INT UNSIGNED NOT NULL,
  `cantidad` INT UNSIGNED NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_consulta`, `id_medicamento`),
  KEY `idx_cm_medicamento` (`id_medicamento`),
  CONSTRAINT `fk_cm_consulta`
    FOREIGN KEY (`id_consulta`) REFERENCES `consulta` (`id_consulta`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cm_medicamento`
    FOREIGN KEY (`id_medicamento`) REFERENCES `medicamento` (`id_medicamento`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `vacuna_aplicada` (
  `id_vacuna_aplicada` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_mascota` INT UNSIGNED NOT NULL,
  `id_tipo_vacuna` SMALLINT UNSIGNED NOT NULL,
  `fecha_aplicacion` DATE NOT NULL,
  `fecha_proxima` DATE NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_vacuna_aplicada`),
  KEY `idx_va_mascota` (`id_mascota`),
  KEY `idx_va_fecha_proxima` (`fecha_proxima`),
  CONSTRAINT `fk_va_mascota`
    FOREIGN KEY (`id_mascota`) REFERENCES `mascota` (`id_mascota`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_va_tipo_vacuna`
    FOREIGN KEY (`id_tipo_vacuna`) REFERENCES `tipo_vacuna` (`id_tipo_vacuna`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `factura` (
  `id_factura` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fecha` DATE NOT NULL,
  `total` DECIMAL(10,2) NOT NULL,
  `id_cliente` INT UNSIGNED NOT NULL,
  `id_consulta` INT UNSIGNED NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_factura`),
  UNIQUE KEY `uk_factura_consulta` (`id_consulta`),
  KEY `idx_factura_cliente` (`id_cliente`),
  KEY `idx_factura_fecha` (`fecha`),
  CONSTRAINT `fk_factura_cliente`
    FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_factura_consulta`
    FOREIGN KEY (`id_consulta`) REFERENCES `consulta` (`id_consulta`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `factura_detalle` (
  `id_factura_detalle` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_factura` INT UNSIGNED NOT NULL,
  `id_medicamento` INT UNSIGNED DEFAULT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  `cantidad` INT UNSIGNED NOT NULL DEFAULT 1,
  `precio_unitario` DECIMAL(10,2) NOT NULL,
  `subtotal` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id_factura_detalle`),
  KEY `idx_fd_factura` (`id_factura`),
  KEY `idx_fd_medicamento` (`id_medicamento`),
  CONSTRAINT `fk_fd_factura`
    FOREIGN KEY (`id_factura`) REFERENCES `factura` (`id_factura`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_fd_medicamento`
    FOREIGN KEY (`id_medicamento`) REFERENCES `medicamento` (`id_medicamento`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `pago` (
  `id_pago` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fecha` DATE NOT NULL,
  `monto` DECIMAL(10,2) NOT NULL,
  `id_tipo_pago` TINYINT UNSIGNED NOT NULL,
  `id_metodo_pago` TINYINT UNSIGNED NOT NULL,
  `id_factura` INT UNSIGNED NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_pago`),
  KEY `idx_pago_factura` (`id_factura`),
  CONSTRAINT `fk_pago_factura`
    FOREIGN KEY (`id_factura`) REFERENCES `factura` (`id_factura`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_pago_tipo`
    FOREIGN KEY (`id_tipo_pago`) REFERENCES `tipo_pago` (`id_tipo_pago`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_pago_metodo`
    FOREIGN KEY (`id_metodo_pago`) REFERENCES `metodo_pago` (`id_metodo_pago`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Datos: lookup
-- --------------------------------------------------------

INSERT INTO `estado_turno` (`id_estado_turno`, `nombre`) VALUES
(1, 'pendiente'),
(2, 'atendido'),
(3, 'cancelado');

INSERT INTO `metodo_pago` (`id_metodo_pago`, `nombre`) VALUES
(1, 'Efectivo'),
(2, 'Tarjeta'),
(3, 'Transferencia');

INSERT INTO `tipo_pago` (`id_tipo_pago`, `nombre`) VALUES
(1, 'Pago completo'),
(2, 'Abono'),
(3, 'Saldo restante');

INSERT INTO `tipo_vacuna` (`id_tipo_vacuna`, `nombre`, `descripcion`) VALUES
(1, 'Parvovirus', 'Vacuna contra parvovirus canino'),
(2, 'Rabia', 'Vacuna antirrábica'),
(3, 'Triple Felina', 'Vacuna triple para gatos'),
(4, 'Moquillo', 'Vacuna contra moquillo canino');

-- --------------------------------------------------------
-- Datos: entidades
-- --------------------------------------------------------

INSERT INTO `cliente` (`id_cliente`, `nombre`, `telefono`, `direccion`) VALUES
(1, 'Laura Gómez', '3112847563', 'Calle 45 #12-30, Bogotá'),
(2, 'Carlos Martínez', '3204591872', 'Carrera 7 #80-15, Bogotá'),
(3, 'Ana Rodríguez', '3156738291', 'Av. 68 #23-10, Bogotá'),
(4, 'Pedro Sánchez', '3009182736', 'Calle 100 #50-20, Bogotá');

INSERT INTO `veterinario` (`id_veterinario`, `nombre`, `especialidad`, `telefono`, `correo`) VALUES
(1, 'Dr. Juan Pérez', 'Medicina general', '6017234891', 'juan.perez@vetclinica.com'),
(2, 'Dra. María Torres', 'Cirugía', '6017345902', 'maria.torres@vetclinica.com'),
(3, 'Dr. Andrés López', 'Dermatología', '6017456013', 'andres.lopez@vetclinica.com');

INSERT INTO `mascota` (`id_mascota`, `nombre`, `especie`, `raza`, `fecha_nacimiento`, `id_cliente`) VALUES
(1, 'Milo', 'Perro', 'Labrador', '2022-06-07', 1),
(2, 'Luna', 'Gato', 'Siamés', '2023-06-07', 1),
(3, 'Rocky', 'Perro', 'Pastor Alemán', '2020-06-07', 2),
(4, 'Canela', 'Perro', 'Golden Retriever', '2024-06-07', 3),
(5, 'Nemo', 'Pez', 'Pez Payaso', '2024-06-07', 4),
(6, 'Manchas', 'Gato', 'Doméstico', '2021-06-07', 2);

INSERT INTO `medicamento` (`id_medicamento`, `nombre`, `descripcion`, `precio`) VALUES
(1, 'Otoclean', 'Gotas para tratamiento de otitis', 32900.00),
(2, 'Meloxicam', 'Antiinflamatorio y analgésico oral', 18500.00),
(3, 'Apoquel 16mg', 'Antihistamínico para dermatitis alérgica', 145000.00),
(4, 'Drontal Plus', 'Desparasitante intestinal de amplio espectro', 28600.00),
(5, 'Amoxicilina', 'Antibiótico de amplio espectro', 12800.00),
(6, 'Champú Malaseb', 'Champú antimicótico y antibacterial', 54900.00);

INSERT INTO `turno` (`id_turno`, `fecha`, `hora`, `id_estado_turno`, `id_mascota`, `id_veterinario`) VALUES
(1, '2025-06-01', '09:00:00', 2, 1, 1),
(2, '2025-06-01', '10:30:00', 2, 3, 2),
(3, '2025-06-02', '08:00:00', 2, 4, 1),
(4, '2025-06-03', '11:00:00', 2, 2, 3),
(5, '2025-06-10', '09:30:00', 1, 6, 1),
(6, '2025-06-10', '14:00:00', 1, 5, 2);

INSERT INTO `consulta` (`id_consulta`, `diagnostico`, `tratamiento`, `observaciones`, `id_turno`) VALUES
(1, 'Otitis leve', 'Gotas óticas cada 12 horas por 7 días', 'Revisar en 2 semanas', 1),
(2, 'Fractura en pata delantera', 'Inmovilización y antiinflamatorio oral', 'Control postoperatorio en 10 días', 2),
(3, 'Dermatitis alérgica', 'Champú medicado más antihistamínico', 'Evitar contacto con pasto húmedo', 3),
(4, 'Parásitos intestinales', 'Desparasitante oral una vez por semana por 3 semanas', 'Mantener higiene del arenero', 4);

INSERT INTO `consulta_medicamento` (`id_consulta`, `id_medicamento`, `cantidad`) VALUES
(1, 1, 1),
(1, 2, 1),
(2, 2, 2),
(2, 5, 1),
(3, 3, 1),
(3, 6, 1),
(4, 4, 3);

INSERT INTO `vacuna_aplicada` (`id_vacuna_aplicada`, `id_mascota`, `id_tipo_vacuna`, `fecha_aplicacion`, `fecha_proxima`) VALUES
(1, 1, 1, '2025-01-10', '2026-01-10'),
(2, 1, 2, '2025-01-10', '2026-01-10'),
(3, 2, 3, '2025-02-15', '2026-02-15'),
(4, 3, 4, '2024-11-20', '2025-11-20'),
(5, 4, 2, '2025-03-05', '2026-03-05'),
(6, 6, 3, '2025-04-01', '2026-04-01');

INSERT INTO `factura` (`id_factura`, `fecha`, `total`, `id_cliente`, `id_consulta`) VALUES
(1, '2025-06-01', 95700.00, 1, 1),
(2, '2025-06-01', 128500.00, 2, 2),
(3, '2025-06-02', 259900.00, 3, 3),
(4, '2025-06-03', 85800.00, 1, 4);

INSERT INTO `factura_detalle` (`id_factura_detalle`, `id_factura`, `id_medicamento`, `descripcion`, `cantidad`, `precio_unitario`, `subtotal`) VALUES
(1, 1, 1, 'Otoclean', 1, 32900.00, 32900.00),
(2, 1, 2, 'Meloxicam', 1, 18500.00, 18500.00),
(3, 1, NULL, 'Honorarios consulta', 1, 44300.00, 44300.00),
(4, 2, 2, 'Meloxicam', 2, 18500.00, 37000.00),
(5, 2, 5, 'Amoxicilina', 1, 12800.00, 12800.00),
(6, 2, NULL, 'Honorarios consulta y procedimiento', 1, 78700.00, 78700.00),
(7, 3, 3, 'Apoquel 16mg', 1, 145000.00, 145000.00),
(8, 3, 6, 'Champú Malaseb', 1, 54900.00, 54900.00),
(9, 3, NULL, 'Honorarios consulta', 1, 60000.00, 60000.00),
(10, 4, 4, 'Drontal Plus', 3, 28600.00, 85800.00);

INSERT INTO `pago` (`id_pago`, `fecha`, `monto`, `id_tipo_pago`, `id_metodo_pago`, `id_factura`) VALUES
(1, '2025-06-01', 95700.00, 1, 1, 1),
(2, '2025-06-01', 64250.00, 2, 2, 2),
(3, '2025-06-08', 64250.00, 3, 2, 2),
(4, '2025-06-02', 259900.00, 1, 3, 3),
(5, '2025-06-03', 85800.00, 1, 1, 4);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
