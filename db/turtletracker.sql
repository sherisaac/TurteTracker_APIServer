CREATE DATABASE  IF NOT EXISTS `turtletracker` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `turtletracker`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: turtletracker
-- ------------------------------------------------------
-- Server version	5.7.17-log

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

--
-- Table structure for table `nest`
--

DROP TABLE IF EXISTS `nest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nest` (
  `nestId` varchar(15) NOT NULL,
  `family` varchar(45) NOT NULL DEFAULT 'general',
  `userId` varchar(15) DEFAULT NULL,
  `longitude` double DEFAULT '0',
  `latitude` double DEFAULT '0',
  `visible` tinyint(4) DEFAULT '1',
  `createDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastUpdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `notes` varchar(255) DEFAULT '',
  PRIMARY KEY (`nestId`),
  UNIQUE KEY `nestId_UNIQUE` (`nestId`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nest`
--

LOCK TABLES `nest` WRITE;
/*!40000 ALTER TABLE `nest` DISABLE KEYS */;
/*!40000 ALTER TABLE `nest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `photo`
--

DROP TABLE IF EXISTS `photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `photo` (
  `photoId` varchar(15) NOT NULL,
  `nestId` varchar(15) DEFAULT NULL,
  `filename` varchar(45) NOT NULL,
  `userId` varchar(15) DEFAULT NULL,
  `visible` tinyint(4) NOT NULL DEFAULT '1',
  `createDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastUpdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`photoId`),
  UNIQUE KEY `photoId_UNIQUE` (`photoId`),
  KEY `fk_nestId_idx` (`nestId`),
  KEY `userId` (`userId`),
  CONSTRAINT `photo_ibfk_1` FOREIGN KEY (`nestId`) REFERENCES `nest` (`nestId`),
  CONSTRAINT `photo_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `photo`
--

LOCK TABLES `photo` WRITE;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userId` varchar(15) NOT NULL,
  `username` varchar(25) NOT NULL,
  `password` char(64) NOT NULL,
  `firstName` varchar(45) DEFAULT '',
  `lastName` varchar(45) DEFAULT '',
  `role` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'Higher role value more power. 0 equals no insert power.',
  `createDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastUpdate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `userId_UNIQUE` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1','admin','fd25965ce169b5c023282bb5fa2e239b6716726db5defaa8ceff225be85dc','','',99,'2017-03-18 18:00:13','2017-03-18 21:18:18'),('lC8a4v0V7Orl4mx','ikeotl','ba7816bf8f1cfea414140de5dae2223b0361a396177a9cb410ff61f2015ad','ISAAV','boop',100,'2017-03-18 19:52:46','2017-03-18 21:45:34');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-18 18:36:26
