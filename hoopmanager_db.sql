DROP DATABASE IF EXISTS hoopmanager;
CREATE DATABASE hoopmanager DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hoopmanager;

CREATE TABLE equipos (
                         id_equipo INT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         categoria VARCHAR(50) NOT NULL,
                         presupuesto DECIMAL(10, 2) DEFAULT 0.00,
                         fecha_creacion DATE NOT NULL
) ENGINE=InnoDB;

CREATE TABLE usuarios (
                          id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(50) NOT NULL,
                          apellidos VARCHAR(100) NOT NULL,
                          correo VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(64) NOT NULL,
                          rol VARCHAR(20) NOT NULL DEFAULT 'JUGADOR',
                          id_equipo INT NULL,
                          CONSTRAINT fk_usuario_equipo
                              FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo)
                                  ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE entrenadores (
                              id_entrenador INT AUTO_INCREMENT PRIMARY KEY,
                              nombre VARCHAR(50) NOT NULL,
                              apellidos VARCHAR(100) NOT NULL,
                              titulacion VARCHAR(50) NOT NULL,
                              id_equipo INT UNIQUE,
                              CONSTRAINT entrenadores_ibfk_1
                                  FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo)
                                      ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE jugadores (
                           id_jugador INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(50) NOT NULL,
                           apellidos VARCHAR(100) NOT NULL,
                           dorsal INT NOT NULL,
                           posicion VARCHAR(30) NOT NULL,
                           altura DECIMAL(3, 2) DEFAULT 0.00,
                           id_equipo INT,
                           CONSTRAINT jugadores_ibfk_1
                               FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo)
                                   ON DELETE SET NULL ON UPDATE CASCADE,
                           CONSTRAINT uk_jugador_dorsal_equipo UNIQUE (id_equipo, dorsal)
) ENGINE=InnoDB;

CREATE TABLE partidos (
                          id_partido INT AUTO_INCREMENT PRIMARY KEY,
                          id_equipo INT DEFAULT NULL,
                          fecha DATETIME NOT NULL,
                          equipo_rival VARCHAR(100) NOT NULL,
                          ubicacion VARCHAR(150) NOT NULL,
                          resultado_propio INT DEFAULT 0,
                          resultado_rival INT DEFAULT 0,
                          CONSTRAINT fk_partido_equipo
                              FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo)
                                  ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE estadisticas (
                              id_estadistica INT AUTO_INCREMENT PRIMARY KEY,
                              id_jugador INT NOT NULL,
                              id_partido INT NOT NULL,
                              puntos INT DEFAULT 0,
                              rebotes INT DEFAULT 0,
                              asistencias INT DEFAULT 0,
                              faltas INT DEFAULT 0,
                              faltas_cometidas INT DEFAULT 0,
                              CONSTRAINT estadisticas_ibfk_1
                                  FOREIGN KEY (id_jugador) REFERENCES jugadores(id_jugador)
                                      ON DELETE CASCADE ON UPDATE CASCADE,
                              CONSTRAINT estadisticas_ibfk_2
                                  FOREIGN KEY (id_partido) REFERENCES partidos(id_partido)
                                      ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT INTO equipos (id_equipo, nombre, categoria, presupuesto, fecha_creacion) VALUES
                                                                                    (1, 'Liceo La Paz A', 'Senior Masculino', 18000.00, '2025-09-01'),
                                                                                    (2, 'Liceo La Paz B', 'Junior Masculino', 12000.00, '2025-09-01'),
                                                                                    (3, 'Liceo La Paz Cadete', 'Cadete Masculino', 9000.00, '2025-09-01'),
                                                                                    (4, 'Liceo La Paz Infantil', 'Infantil Masculino', 7000.00, '2025-09-01');

INSERT INTO usuarios (id_usuario, nombre, apellidos, correo, password, rol, id_equipo) VALUES
                                                                                           (1, 'Administrador', 'Principal', 'admin@hoopmanager.com', SHA2('1234', 256), 'ADMIN', NULL),
                                                                                           (2, 'Carlos', 'Fernandez', 'carlos.fernandez@hoopmanager.com', SHA2('1234', 256), 'ENTRENADOR', 1),
                                                                                           (3, 'Marta', 'Sanchez', 'marta.sanchez@hoopmanager.com', SHA2('1234', 256), 'ENTRENADOR', 2),
                                                                                           (4, 'Javier', 'Lorenzo', 'javier.lorenzo@hoopmanager.com', SHA2('1234', 256), 'ENTRENADOR', 3),
                                                                                           (5, 'Laura', 'Pazos', 'laura.pazos@hoopmanager.com', SHA2('1234', 256), 'ENTRENADOR', 4),
                                                                                           (6, 'Usuario', 'Consulta', 'usuario@hoopmanager.com', SHA2('1234', 256), 'JUGADOR', NULL);

INSERT INTO entrenadores (id_entrenador, nombre, apellidos, titulacion, id_equipo) VALUES
                                                                                       (1, 'Carlos', 'Fernandez', 'Nivel 3 - Nacional', 1),
                                                                                       (2, 'Marta', 'Sanchez', 'Nivel 2 - Autonomico', 2),
                                                                                       (3, 'Javier', 'Lorenzo', 'Nivel 2 - Autonomico', 3),
                                                                                       (4, 'Laura', 'Pazos', 'Nivel 1 - Federativo', 4);

INSERT INTO jugadores (id_jugador, nombre, apellidos, dorsal, posicion, altura, id_equipo) VALUES
                                                                                               (1, 'Alejandro', 'Gomez Perez', 7, 'Base', 1.85, 1),
                                                                                               (2, 'David', 'Rodriguez Castro', 15, 'Pivot', 2.05, 1),
                                                                                               (3, 'Marcos', 'Santos Lopez', 11, 'Escolta', 1.91, 1),
                                                                                               (4, 'Pablo', 'Varela Nunez', 4, 'Base', 1.82, 1),
                                                                                               (5, 'Hugo', 'Fernandez Rey', 23, 'Alero', 1.96, 1),
                                                                                               (6, 'Mateo', 'Iglesias Costa', 9, 'Ala-Pivot', 1.99, 1),
                                                                                               (7, 'Bruno', 'Martinez Seoane', 32, 'Pivot', 2.01, 1),
                                                                                               (8, 'Iker', 'Lorenzo Vidal', 6, 'Escolta', 1.88, 1),

                                                                                               (9, 'Diego', 'Mendez Garcia', 5, 'Base', 1.78, 2),
                                                                                               (10, 'Lucas', 'Pereira Lopez', 8, 'Escolta', 1.84, 2),
                                                                                               (11, 'Nico', 'Alonso Diaz', 10, 'Alero', 1.89, 2),
                                                                                               (12, 'Adrian', 'Suarez Blanco', 12, 'Ala-Pivot', 1.94, 2),
                                                                                               (13, 'Samuel', 'Romero Castro', 14, 'Pivot', 1.98, 2),
                                                                                               (14, 'Leo', 'Cabanas Rios', 18, 'Base', 1.76, 2),

                                                                                               (15, 'Daniel', 'Ferreiro Gomez', 4, 'Base', 1.72, 3),
                                                                                               (16, 'Martin', 'Paz Sanchez', 6, 'Escolta', 1.76, 3),
                                                                                               (17, 'Oscar', 'Rivas Torres', 13, 'Alero', 1.81, 3),
                                                                                               (18, 'Javier', 'Molina Lago', 21, 'Ala-Pivot', 1.86, 3),
                                                                                               (19, 'Tomas', 'Calvo Pena', 25, 'Pivot', 1.90, 3),

                                                                                               (20, 'Gael', 'Otero Vazquez', 3, 'Base', 1.60, 4),
                                                                                               (21, 'Noel', 'Barreiro Silva', 7, 'Escolta', 1.64, 4),
                                                                                               (22, 'Ian', 'Campos Pardo', 9, 'Alero', 1.68, 4),
                                                                                               (23, 'Teo', 'Souto Fraga', 15, 'Ala-Pivot', 1.72, 4),
                                                                                               (24, 'Roi', 'Arias Bello', 22, 'Pivot', 1.76, 4);

INSERT INTO partidos (id_partido, id_equipo, fecha, equipo_rival, ubicacion, resultado_propio, resultado_rival) VALUES
                                                                                                                    (1, 1, '2026-01-18 18:00:00', 'CB Coruna', 'Pabellon Liceo La Paz', 78, 74),
                                                                                                                    (2, 1, '2026-02-02 19:30:00', 'Obradoiro B', 'Pabellon Riazor', 69, 71),
                                                                                                                    (3, 1, '2026-02-16 18:15:00', 'Basquet Arteixo', 'Pabellon Liceo La Paz', 82, 66),
                                                                                                                    (4, 1, '2026-03-01 20:00:00', 'Maristas Coruna', 'Colegio Maristas', 73, 68),
                                                                                                                    (5, 1, '2026-03-16 19:00:00', 'Santo Domingo Betanzos', 'Pabellon Liceo La Paz', 80, 76),

                                                                                                                    (6, 2, '2026-01-20 17:00:00', 'CB Culleredo', 'Pabellon Liceo La Paz', 61, 55),
                                                                                                                    (7, 2, '2026-02-08 12:00:00', 'Basquet Cambre', 'Pabellon Cambre', 58, 63),
                                                                                                                    (8, 2, '2026-02-22 16:30:00', 'Santo Domingo Betanzos B', 'Pabellon Liceo La Paz', 74, 70),
                                                                                                                    (9, 2, '2026-03-08 18:00:00', 'CB Arteixo Junior', 'Pabellon Arteixo', 66, 59),

                                                                                                                    (10, 3, '2026-01-25 11:30:00', 'CB Arteixo Cadete', 'Pabellon Arteixo', 52, 49),
                                                                                                                    (11, 3, '2026-02-15 10:45:00', 'Maristas Cadete', 'Pabellon Liceo La Paz', 60, 57),
                                                                                                                    (12, 3, '2026-03-02 12:15:00', 'Obradoiro Cadete', 'Pabellon Santiago', 48, 54),

                                                                                                                    (13, 4, '2026-01-26 10:00:00', 'CB Cambre Infantil', 'Pabellon Liceo La Paz', 44, 38),
                                                                                                                    (14, 4, '2026-02-18 11:15:00', 'Obradoiro Infantil', 'Pabellon Santiago', 41, 46),
                                                                                                                    (15, 4, '2026-03-10 10:30:00', 'Maristas Infantil', 'Colegio Maristas', 50, 42);

INSERT INTO estadisticas (id_jugador, id_partido, puntos, rebotes, asistencias, faltas, faltas_cometidas) VALUES
                                                                                                              (1, 1, 18, 4, 8, 2, 2),
                                                                                                              (2, 1, 14, 12, 1, 3, 3),
                                                                                                              (3, 1, 12, 3, 4, 1, 1),
                                                                                                              (4, 1, 9, 2, 6, 2, 2),
                                                                                                              (5, 1, 16, 7, 3, 2, 2),
                                                                                                              (6, 1, 9, 8, 2, 4, 4),

                                                                                                              (1, 2, 11, 3, 7, 2, 2),
                                                                                                              (2, 2, 20, 10, 1, 4, 4),
                                                                                                              (3, 2, 13, 4, 2, 3, 3),
                                                                                                              (4, 2, 7, 1, 5, 1, 1),
                                                                                                              (5, 2, 10, 6, 2, 2, 2),
                                                                                                              (7, 2, 8, 9, 1, 3, 3),

                                                                                                              (1, 3, 21, 5, 9, 1, 1),
                                                                                                              (2, 3, 15, 11, 2, 3, 3),
                                                                                                              (3, 3, 18, 4, 3, 2, 2),
                                                                                                              (6, 3, 12, 8, 2, 2, 2),
                                                                                                              (8, 3, 10, 2, 5, 1, 1),
                                                                                                              (5, 3, 6, 5, 1, 2, 2),

                                                                                                              (1, 4, 16, 4, 6, 2, 2),
                                                                                                              (2, 4, 12, 13, 1, 3, 3),
                                                                                                              (3, 4, 15, 3, 4, 1, 1),
                                                                                                              (5, 4, 13, 7, 2, 2, 2),
                                                                                                              (6, 4, 9, 6, 2, 3, 3),
                                                                                                              (8, 4, 8, 2, 4, 2, 2),

                                                                                                              (1, 5, 19, 5, 7, 1, 1),
                                                                                                              (2, 5, 18, 12, 2, 3, 3),
                                                                                                              (3, 5, 11, 3, 3, 2, 2),
                                                                                                              (5, 5, 14, 8, 2, 2, 2),
                                                                                                              (7, 5, 10, 7, 1, 4, 4),
                                                                                                              (8, 5, 8, 2, 5, 1, 1),

                                                                                                              (9, 6, 15, 3, 6, 1, 1),
                                                                                                              (10, 6, 13, 2, 4, 2, 2),
                                                                                                              (11, 6, 11, 6, 2, 2, 2),
                                                                                                              (12, 6, 14, 5, 2, 2, 2),
                                                                                                              (13, 6, 8, 9, 1, 3, 3),

                                                                                                              (9, 7, 12, 2, 5, 2, 2),
                                                                                                              (10, 7, 16, 3, 3, 2, 2),
                                                                                                              (11, 7, 9, 7, 2, 3, 3),
                                                                                                              (13, 7, 10, 10, 1, 4, 4),
                                                                                                              (14, 7, 11, 4, 3, 2, 2),

                                                                                                              (9, 8, 17, 3, 7, 1, 1),
                                                                                                              (10, 8, 14, 4, 4, 2, 2),
                                                                                                              (11, 8, 12, 5, 3, 2, 2),
                                                                                                              (12, 8, 15, 7, 2, 3, 3),
                                                                                                              (13, 8, 9, 8, 1, 4, 4),
                                                                                                              (14, 8, 7, 2, 5, 1, 1),

                                                                                                              (9, 9, 13, 2, 6, 2, 2),
                                                                                                              (10, 9, 11, 3, 3, 2, 2),
                                                                                                              (11, 9, 16, 6, 2, 3, 3),
                                                                                                              (12, 9, 10, 8, 1, 2, 2),
                                                                                                              (13, 9, 9, 9, 1, 3, 3),
                                                                                                              (14, 9, 7, 3, 4, 1, 1),

                                                                                                              (15, 10, 14, 2, 5, 1, 1),
                                                                                                              (16, 10, 10, 3, 3, 2, 2),
                                                                                                              (17, 10, 12, 5, 2, 2, 2),
                                                                                                              (18, 10, 8, 8, 1, 3, 3),
                                                                                                              (19, 10, 8, 9, 1, 3, 3),

                                                                                                              (15, 11, 16, 3, 6, 1, 1),
                                                                                                              (16, 11, 12, 4, 3, 2, 2),
                                                                                                              (17, 11, 10, 6, 2, 2, 2),
                                                                                                              (18, 11, 11, 7, 1, 3, 3),
                                                                                                              (19, 11, 11, 8, 1, 3, 3),

                                                                                                              (15, 12, 11, 2, 5, 1, 1),
                                                                                                              (16, 12, 8, 3, 3, 2, 2),
                                                                                                              (17, 12, 9, 5, 2, 2, 2),
                                                                                                              (18, 12, 10, 8, 1, 3, 3),
                                                                                                              (19, 12, 10, 9, 1, 4, 4),

                                                                                                              (20, 13, 12, 2, 4, 1, 1),
                                                                                                              (21, 13, 9, 3, 2, 2, 2),
                                                                                                              (22, 13, 8, 5, 1, 2, 2),
                                                                                                              (23, 13, 7, 7, 1, 3, 3),
                                                                                                              (24, 13, 8, 8, 1, 2, 2),

                                                                                                              (20, 14, 10, 2, 4, 1, 1),
                                                                                                              (21, 14, 7, 3, 2, 2, 2),
                                                                                                              (22, 14, 9, 4, 1, 2, 2),
                                                                                                              (23, 14, 8, 8, 1, 3, 3),
                                                                                                              (24, 14, 7, 9, 1, 2, 2),

                                                                                                              (20, 15, 13, 2, 5, 1, 1),
                                                                                                              (21, 15, 11, 4, 3, 2, 2),
                                                                                                              (22, 15, 10, 5, 2, 2, 2),
                                                                                                              (23, 15, 8, 7, 1, 3, 3),
                                                                                                              (24, 15, 8, 8, 1, 2, 2);