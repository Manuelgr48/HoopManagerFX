CREATE DATABASE IF NOT EXISTS hoopmanager DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hoopmanager;

-- 1. TABLA USUARIOS
CREATE TABLE IF NOT EXISTS usuarios (
                                        id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                                        username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'JUGADOR'
    ) ENGINE=InnoDB;

-- 2. TABLA EQUIPOS
CREATE TABLE IF NOT EXISTS equipos (
                                       id_equipo INT AUTO_INCREMENT PRIMARY KEY,
                                       nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    presupuesto DECIMAL(10, 2) DEFAULT 0.00,
    fecha_creacion DATE NOT NULL
    ) ENGINE=InnoDB;

-- 3. TABLA JUGADORES
CREATE TABLE IF NOT EXISTS jugadores (
                                         id_jugador INT AUTO_INCREMENT PRIMARY KEY,
                                         nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dorsal INT NOT NULL,
    posicion VARCHAR(30) NOT NULL,
    altura DECIMAL(3, 2) DEFAULT 0.00,
    id_equipo INT,
    FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo) ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB;

-- 4. TABLA ENTRENADORES
CREATE TABLE IF NOT EXISTS entrenadores (
                                            id_entrenador INT AUTO_INCREMENT PRIMARY KEY,
                                            nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    titulacion VARCHAR(50) NOT NULL,
    id_equipo INT UNIQUE,
    FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo) ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB;

-- 5. TABLA PARTIDOS
CREATE TABLE IF NOT EXISTS partidos (
                                        id_partido INT AUTO_INCREMENT PRIMARY KEY,
                                        fecha DATETIME NOT NULL,
                                        equipo_rival VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    resultado_propio INT DEFAULT 0,
    resultado_rival INT DEFAULT 0
    ) ENGINE=InnoDB;

-- 6. TABLA ESTADÍSTICAS
CREATE TABLE IF NOT EXISTS estadisticas (
                                            id_estadistica INT AUTO_INCREMENT PRIMARY KEY,
                                            id_jugador INT NOT NULL,
                                            id_partido INT NOT NULL,
                                            puntos INT DEFAULT 0,
                                            rebotes INT DEFAULT 0,
                                            asistencias INT DEFAULT 0,
                                            faltas INT DEFAULT 0,
                                            FOREIGN KEY (id_jugador) REFERENCES jugadores(id_jugador) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_partido) REFERENCES partidos(id_partido) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB;

-- DATOS DE PRUEBA
-- Usuario admin con contraseña '1234' hasheada en SHA-256
INSERT INTO usuarios (username, password, rol) VALUES
    ('admin', SHA2('1234', 256), 'ADMIN');

INSERT INTO equipos (nombre, categoria, presupuesto, fecha_creacion) VALUES
                                                                         ('Liceo La Paz A', 'Senior', 12000.00, '2025-09-01'),
                                                                         ('Liceo La Paz B', 'Junior', 8500.00, '2025-09-01');

INSERT INTO entrenadores (nombre, apellidos, titulacion, id_equipo) VALUES
                                                                        ('Carlos', 'Fernández', 'Nivel 3 - Nacional', 1),
                                                                        ('Marta', 'Sánchez', 'Nivel 2 - Autonómico', 2);

INSERT INTO jugadores (nombre, apellidos, dorsal, posicion, altura, id_equipo) VALUES
                                                                                   ('Alejandro', 'Gómez', 7, 'Base', 1.85, 1),
                                                                                   ('David', 'Rodríguez', 15, 'Pívot', 2.05, 1),
                                                                                   ('Hugo', 'López', 23, 'Alero', 1.96, 2);