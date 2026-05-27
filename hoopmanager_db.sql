-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: hoopmanager
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `entrenadores`
--

DROP TABLE IF EXISTS `entrenadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrenadores` (
                                `id_entrenador` int NOT NULL AUTO_INCREMENT,
                                `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `apellidos` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `titulacion` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `id_equipo` int DEFAULT NULL,
                                PRIMARY KEY (`id_entrenador`),
                                UNIQUE KEY `id_equipo` (`id_equipo`),
                                CONSTRAINT `entrenadores_ibfk_1` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `equipos`
--

DROP TABLE IF EXISTS `equipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipos` (
                           `id_equipo` int NOT NULL AUTO_INCREMENT,
                           `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `categoria` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `presupuesto` decimal(10,2) DEFAULT '0.00',
                           `fecha_creacion` date NOT NULL,
                           PRIMARY KEY (`id_equipo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estadisticas`
--

DROP TABLE IF EXISTS `estadisticas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadisticas` (
                                `id_estadistica` int NOT NULL AUTO_INCREMENT,
                                `id_jugador` int NOT NULL,
                                `id_partido` int NOT NULL,
                                `puntos` int DEFAULT '0',
                                `rebotes` int DEFAULT '0',
                                `asistencias` int DEFAULT '0',
                                `faltas` int DEFAULT '0',
                                `faltas_cometidas` int DEFAULT '0',
                                PRIMARY KEY (`id_estadistica`),
                                KEY `id_jugador` (`id_jugador`),
                                KEY `id_partido` (`id_partido`),
                                CONSTRAINT `estadisticas_ibfk_1` FOREIGN KEY (`id_jugador`) REFERENCES `jugadores` (`id_jugador`) ON DELETE CASCADE ON UPDATE CASCADE,
                                CONSTRAINT `estadisticas_ibfk_2` FOREIGN KEY (`id_partido`) REFERENCES `partidos` (`id_partido`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jugadores`
--

DROP TABLE IF EXISTS `jugadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jugadores` (
                             `id_jugador` int NOT NULL AUTO_INCREMENT,
                             `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `apellidos` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `dorsal` int NOT NULL,
                             `posicion` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `altura` decimal(3,2) DEFAULT '0.00',
                             `id_equipo` int DEFAULT NULL,
                             PRIMARY KEY (`id_jugador`),
                             KEY `id_equipo` (`id_equipo`),
                             CONSTRAINT `jugadores_ibfk_1` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partidos`
--

DROP TABLE IF EXISTS `partidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partidos` (
                            `id_partido` int NOT NULL AUTO_INCREMENT,
                            `id_equipo` int DEFAULT NULL,
                            `fecha` datetime NOT NULL,
                            `equipo_rival` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `ubicacion` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `resultado_propio` int DEFAULT '0',
                            `resultado_rival` int DEFAULT '0',
                            PRIMARY KEY (`id_partido`),
                            KEY `fk_partido_equipo` (`id_equipo`),
                            CONSTRAINT `fk_partido_equipo` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
                            `id_usuario` int NOT NULL AUTO_INCREMENT,
                            `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `apellidos` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `correo` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `password` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `rol` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'JUGADOR',
                            `id_equipo` int DEFAULT NULL,
                            PRIMARY KEY (`id_usuario`),
                            UNIQUE KEY `correo` (`correo`),
                            KEY `fk_usuario_equipo` (`id_equipo`),
                            CONSTRAINT `fk_usuario_equipo` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE jugadores
    ADD CONSTRAINT uk_jugador_dorsal_equipo UNIQUE (id_equipo, dorsal);
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-27 17:02:41
