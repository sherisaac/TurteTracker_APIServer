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
-- Table structure for table `api_key`
--

DROP TABLE IF EXISTS `api_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_key` (
  `apiId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `apiKey` varchar(75) NOT NULL,
  `active` tinyint(4) DEFAULT '1',
  `canWrite` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`apiId`),
  UNIQUE KEY `apiId_UNIQUE` (`apiId`),
  UNIQUE KEY `apiKey_UNIQUE` (`apiKey`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_key`
--

LOCK TABLES `api_key` WRITE;
/*!40000 ALTER TABLE `api_key` DISABLE KEYS */;
INSERT INTO `api_key` VALUES (1,'xwv6pr3iyc7mie16dou03zt7ww00820ei2p8ofzluh4r1ul6qff5jt08arftax60bsfl3xqt289',1,1);
/*!40000 ALTER TABLE `api_key` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nest`
--

DROP TABLE IF EXISTS `nest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nest` (
  `nestId` varchar(15) NOT NULL,
  `groupId` int(11) unsigned DEFAULT '0',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `apiId` int(10) unsigned DEFAULT NULL,
  `visible` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`nestId`),
  UNIQUE KEY `nestId_UNIQUE` (`nestId`),
  KEY `apiId` (`apiId`),
  KEY `groupId` (`groupId`),
  CONSTRAINT `nest_ibfk_1` FOREIGN KEY (`apiId`) REFERENCES `api_key` (`apiId`),
  CONSTRAINT `nest_ibfk_2` FOREIGN KEY (`groupId`) REFERENCES `nest_group` (`groupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nest`
--

LOCK TABLES `nest` WRITE;
/*!40000 ALTER TABLE `nest` DISABLE KEYS */;
INSERT INTO `nest` VALUES ('1',0,NULL,NULL,1,1),('2xmcth19vsf5682',0,41.2222,1.2222,1,1),('32me9ma5s4mhptj',0,23.2222,14515.2222,1,0),('axeyszzlldfb0dk',0,1.2222,1.2222,NULL,1),('cocsxfnawzdkhrs',0,23.2222,14515.2222,1,0),('e7v84ooyy0lkutl',0,1.2222,1.2222,1,1),('i6vwphqll57yuea',0,23.2222,14515.2222,1,0),('mldkfph15cpjygg',0,1.2222,1.2222,1,1),('p5bfggl0ftcqejg',0,23.2222,14515.2222,1,1),('pkd52w2hi9ar9i3',0,41.2222,1.2222,1,1),('ztro35vzl60gojr',0,1.2222,1.2222,1,1);
/*!40000 ALTER TABLE `nest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nest_group`
--

DROP TABLE IF EXISTS `nest_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nest_group` (
  `groupId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `radius` double DEFAULT '0',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  PRIMARY KEY (`groupId`),
  UNIQUE KEY `groupId_UNIQUE` (`groupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nest_group`
--

LOCK TABLES `nest_group` WRITE;
/*!40000 ALTER TABLE `nest_group` DISABLE KEYS */;
INSERT INTO `nest_group` VALUES (0,0,NULL,NULL);
/*!40000 ALTER TABLE `nest_group` ENABLE KEYS */;
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
  `visible` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`photoId`),
  UNIQUE KEY `photoId_UNIQUE` (`photoId`),
  KEY `photo_ibfk_1` (`nestId`),
  CONSTRAINT `photo_ibfk_1` FOREIGN KEY (`nestId`) REFERENCES `nest` (`nestId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `photo`
--

LOCK TABLES `photo` WRITE;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
INSERT INTO `photo` VALUES ('2','1','Stygian_Blade.jpg',1),('3','1','The_Bard.jpg',1),('71739y7sy8d0wh6','i6vwphqll57yuea','71739y7sy8d0wh6.jpg',0),('7zzimw4oqw8h223','i6vwphqll57yuea','7zzimw4oqw8h223.jpg',1),('asddweqw','p5bfggl0ftcqejg','asddweqw.jpg',1),('blk1eetc9wzn8fh','i6vwphqll57yuea','blk1eetc9wzn8fh.jpg',1),('c5tlaruov9ry710',NULL,'c5tlaruov9ry710.jpg',0),('kn4dm1qkw2lgbhy','i6vwphqll57yuea','kn4dm1qkw2lgbhy.jpg',1),('mtmz0bygb8ulcit',NULL,'mtmz0bygb8ulcit.jpg',0),('qkmlw9tqwdxpd2b',NULL,'qkmlw9tqwdxpd2b.jpg',1),('weqweqwe','p5bfggl0ftcqejg','weqweqwe.jpg',1);
/*!40000 ALTER TABLE `photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(25) NOT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `apiId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `apiId` (`apiId`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`apiId`) REFERENCES `api_key` (`apiId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin',NULL,NULL,1);
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

-- Dump completed on 2017-02-19 13:13:59
