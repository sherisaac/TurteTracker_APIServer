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
  `groupId` int(11) unsigned DEFAULT '0',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `visible` tinyint(4) DEFAULT '1',
  `userId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`nestId`),
  UNIQUE KEY `nestId_UNIQUE` (`nestId`),
  KEY `groupId` (`groupId`),
  KEY `userId` (`userId`),
  CONSTRAINT `nest_ibfk_2` FOREIGN KEY (`groupId`) REFERENCES `nest_group` (`groupId`),
  CONSTRAINT `nest_ibfk_3` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nest`
--

LOCK TABLES `nest` WRITE;
/*!40000 ALTER TABLE `nest` DISABLE KEYS */;
INSERT INTO `nest` VALUES ('1',0,NULL,NULL,1,NULL),('2xmcth19vsf5682',0,41.2222,1.2222,1,NULL),('32me9ma5s4mhptj',0,23.2222,14515.2222,0,NULL),('axeyszzlldfb0dk',0,1.2222,1.2222,1,NULL),('cocsxfnawzdkhrs',0,23.2222,14515.2222,0,NULL),('dij6vxwwc10ires',0,41.2222,1.2222,1,NULL),('e7v84ooyy0lkutl',0,1.2222,1.2222,1,NULL),('i6vwphqll57yuea',0,23.2222,14515.2222,0,NULL),('mldkfph15cpjygg',0,1.2222,1.2222,1,NULL),('p5bfggl0ftcqejg',0,23.2222,14515.2222,1,NULL),('pkd52w2hi9ar9i3',0,41.2222,1.2222,1,NULL),('u2dfc90j5esd67r',0,41.2222,1.2222,1,NULL),('ztro35vzl60gojr',0,1.2222,1.2222,1,NULL);
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
  `userId` int(10) unsigned DEFAULT NULL,
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
INSERT INTO `photo` VALUES ('0vgje15kqfxi25o',NULL,'0vgje15kqfxi25o.jpg',0,1),('2','1','Stygian_Blade.jpg',0,1),('3','1','The_Bard.jpg',0,1),('71739y7sy8d0wh6','i6vwphqll57yuea','71739y7sy8d0wh6.jpg',0,0),('7zzimw4oqw8h223','i6vwphqll57yuea','7zzimw4oqw8h223.jpg',0,1),('asddweqw','p5bfggl0ftcqejg','asddweqw.jpg',0,1),('blk1eetc9wzn8fh','i6vwphqll57yuea','blk1eetc9wzn8fh.jpg',0,1),('c5tlaruov9ry710',NULL,'c5tlaruov9ry710.jpg',0,0),('kn4dm1qkw2lgbhy','i6vwphqll57yuea','kn4dm1qkw2lgbhy.jpg',0,1),('kqgv73htixqhm2x',NULL,'kqgv73htixqhm2x.jpg',0,1),('mtmz0bygb8ulcit',NULL,'mtmz0bygb8ulcit.jpg',0,0),('ovys0mu8unp243d',NULL,'ovys0mu8unp243d.jpg',0,1),('p6ignma5bofhlds',NULL,'p6ignma5bofhlds.jpg',0,1),('qkmlw9tqwdxpd2b',NULL,'qkmlw9tqwdxpd2b.jpg',0,1),('weqweqwe','p5bfggl0ftcqejg','weqweqwe.jpg',0,1);
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
  `password` char(64) NOT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `role` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'Higher role value more power. 0 equals no insert power.',
  PRIMARY KEY (`userId`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','fd25965ce169b5c023282bb5fa2e239b6716726db5defaa8ceff225be85dc',NULL,NULL,99);
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

-- Dump completed on 2017-03-05 12:48:38
