
CREATE DATABASE IF NOT EXISTS hoopmanager DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hoopmanager;

-- 1. TABLA EQUIPOS
CREATE TABLE IF NOT EXISTS equipos (
    id_equipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    presupuesto DECIMAL(10, 2) DEFAULT 0.00,
    fecha_creacion DATE NOT NULL
    ) ENGINE=InnoDB;

-- 2. TABLA JUGADORES
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

-- 3. TABLA ENTRENADORES
CREATE TABLE IF NOT EXISTS entrenadores (
    id_entrenador INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    titulacion VARCHAR(50) NOT NULL,
    id_equipo INT UNIQUE,
    FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo) ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB;

-- 4. TABLA PARTIDOS
CREATE TABLE IF NOT EXISTS partidos (
    id_partido INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    equipo_rival VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    resultado_propio INT DEFAULT 0,
    resultado_rival INT DEFAULT 0
    ) ENGINE=InnoDB;

-- 5. TABLA ESTADÍSTICAS
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

-- DATOS DE PRUEBA INICIALES
INSERT INTO equipos (nombre, categoria, presupuesto, fecha_creacion) VALUES
                                                                         ('Liceo La Paz A', 'Senior', 12000.00, '2025-09-01'),
                                                                         ('Liceo La Paz B', 'Junior', 8500.00, '2025-09-01');

INSERT INTO entrenadores (nombre, apellidos, titulacion, id_equipo) VALUES
                                                                        ('Carlos', 'Fernández', 'Nivel 3 - Nacional', 1),
                                                                        ('Marta', 'Sánchez', 'Nivel 2 - Autonómico', 2);

INSERT INTO jugadores (nombre, apellidos, dorsal, posicion, altura, id_equipo) VALUES
                                                                                   ('Alejandro', 'Gómez', '7', 'Base', 1.85, 1),
                                                                                   ('David', 'Rodríguez', '15', 'Pívot', 2.05, 1),
                                                                                   ('Hugo', 'López', '23', 'Alero', 1.96, 2);

INSERT INTO partidos (fecha, equipo_rival, ubicacion, resultado_propio, resultado_rival) VALUES
                                                                                             ('2026-05-02 18:00:00', 'CB Coruña', 'Pabellón Riazor', 78, 74),
                                                                                             ('2026-05-09 12:00:00', 'Obradoiro B', 'Pabellón Liceo', 85, 90);

INSERT INTO estadisticas (id_jugador, id_partido, puntos, rebotes, asistencias, faltas) VALUES
                                                                                            (1, 1, 18, 4, 8, 2),
                                                                                            (2, 1, 14, 12, 1, 4);