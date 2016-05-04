-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: contextprojectdatabase
-- ------------------------------------------------------
-- Server version	5.7.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP DATABASE IF EXISTS `presetsDatabase`;
CREATE DATABASE presetsDatabase;
USE presetsDatabase;

--
-- Table structure for table `camera`
--

DROP TABLE IF EXISTS `camera`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `camera` (
  `ID` int(11) NOT NULL,
  `Name` varchar(45) DEFAULT NULL,
  `IPadress` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `camera`
--

LOCK TABLES `camera` WRITE;
/*!40000 ALTER TABLE `camera` DISABLE KEYS */;
/*!40000 ALTER TABLE `camera` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cameramovingpresets`
--

DROP TABLE IF EXISTS `cameramovingpresets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cameramovingpresets` (
  `CameraMovingPresetID` int(11) NOT NULL,
  `Camera_ID` int(11) NOT NULL,
  `MovingPresets_ID` int(11) NOT NULL,
  PRIMARY KEY (`CameraMovingPresetID`,`Camera_ID`,`MovingPresets_ID`),
  KEY `fk_CameraMovingPresets_Camera1_idx` (`Camera_ID`),
  KEY `fk_CameraMovingPresets_MovingPresets1_idx` (`MovingPresets_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cameramovingpresets`
--

LOCK TABLES `cameramovingpresets` WRITE;
/*!40000 ALTER TABLE `cameramovingpresets` DISABLE KEYS */;
/*!40000 ALTER TABLE `cameramovingpresets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `camerapresets`
--

DROP TABLE IF EXISTS `camerapresets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `camerapresets` (
  `CameraPresetID` int(11) NOT NULL,
  `Camera_ID` int(11) NOT NULL,
  `Presets_ID` int(11) NOT NULL,
  PRIMARY KEY (`CameraPresetID`,`Camera_ID`,`Presets_ID`),
  KEY `fk_CameraPresets_Camera_idx` (`Camera_ID`),
  KEY `fk_CameraPresets_Presets1_idx` (`Presets_ID`),
  CONSTRAINT `fk_CameraPresets_Camera` FOREIGN KEY (`Camera_ID`) REFERENCES `camera` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_CameraPresets_Presets1` FOREIGN KEY (`Presets_ID`) REFERENCES `presets` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `camerapresets`
--

LOCK TABLES `camerapresets` WRITE;
/*!40000 ALTER TABLE `camerapresets` DISABLE KEYS */;
/*!40000 ALTER TABLE `camerapresets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movingpresets`
--

DROP TABLE IF EXISTS `movingpresets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movingpresets` (
  `ID` int(11) NOT NULL,
  `BeginPresetID` int(11) DEFAULT NULL,
  `Time` int(11) DEFAULT NULL,
  `EndPresetID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `BeginStatus_idx` (`BeginPresetID`,`EndPresetID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movingpresets`
--

LOCK TABLES `movingpresets` WRITE;
/*!40000 ALTER TABLE `movingpresets` DISABLE KEYS */;
/*!40000 ALTER TABLE `movingpresets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `presets`
--

DROP TABLE IF EXISTS `presets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `presets` (
  `ID` int(11) NOT NULL,
  `Pan` int(11) DEFAULT NULL,
  `Tilt` int(11) DEFAULT NULL,
  `Zoom` int(11) DEFAULT NULL,
  `Focus` int(11) DEFAULT NULL,
  `Iris` int(11) DEFAULT NULL,
  `Autofocus` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `presets`
--

LOCK TABLES `presets` WRITE;
/*!40000 ALTER TABLE `presets` DISABLE KEYS */;
/*!40000 ALTER TABLE `presets` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-28 15:41:21
