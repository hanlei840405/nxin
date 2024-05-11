-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: nxin_etl_2024_001_mybatis
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `analysis_layout`
--

DROP TABLE IF EXISTS `analysis_layout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `analysis_layout` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resource_code` varchar(255) DEFAULT '' COMMENT '资源码',
  `arrange` text,
  `description` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `analysis_layout`
--

LOCK TABLES `analysis_layout` WRITE;
/*!40000 ALTER TABLE `analysis_layout` DISABLE KEYS */;
/*!40000 ALTER TABLE `analysis_layout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_privilege`
--

DROP TABLE IF EXISTS `auth_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_privilege` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `resource_id` bigint DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_privilege`
--

LOCK TABLES `auth_privilege` WRITE;
/*!40000 ALTER TABLE `auth_privilege` DISABLE KEYS */;
INSERT INTO `auth_privilege` VALUES (3,'ROOT',1,'RW','1',NULL,NULL,NULL,NULL,1),(7,'[9]第二组',21,'RW','1','2024-01-17 00:58:46','jesse.18@163.com','jesse.18@163.com','2024-01-17 00:58:46',1),(8,'[10]第三组',22,'RW','1','2024-01-17 01:00:12','jesse.18@163.com','jesse.18@163.com','2024-01-17 01:00:12',1),(9,'[11]第四组',23,'RW','1','2024-01-17 01:03:45','jesse.18@163.com','jesse.18@163.com','2024-01-17 01:03:45',1),(10,'[9]第二组',21,'R','1',NULL,NULL,NULL,NULL,1),(11,'[12]第五组',24,'RW','1','2024-01-22 20:17:31','jesse.18@163.com','jesse.18@163.com','2024-01-22 20:17:31',1),(12,'[12]第五组',24,'RW','1','2024-01-22 20:17:31','jesse.18@163.com','jesse.18@163.com','2024-01-22 20:17:31',1),(13,'首页',2,'RW','1',NULL,NULL,NULL,NULL,1),(14,'工程管理',3,'RW','1',NULL,NULL,NULL,NULL,1),(15,'数据源管理',4,'RW','1',NULL,NULL,NULL,NULL,1),(16,'下载中心',5,'RW','1',NULL,NULL,NULL,NULL,1),(17,'在线设计',6,'RW','1',NULL,NULL,NULL,NULL,1),(18,'脚本发布',7,'RW','1',NULL,NULL,NULL,NULL,1),(19,'批处理任务',8,'RW','1',NULL,NULL,NULL,NULL,1),(20,'流处理任务',9,'RW','1',NULL,NULL,NULL,NULL,1),(21,'进程管理',10,'RW','1',NULL,NULL,NULL,NULL,1),(22,'日志跟踪',11,'RW','1',NULL,NULL,NULL,NULL,1),(23,'模型设计',12,'RW','1',NULL,NULL,NULL,NULL,1),(24,'统计设置',13,'RW','1',NULL,NULL,NULL,NULL,1),(25,'布局设置',14,'RW','1',NULL,NULL,NULL,NULL,1),(26,'用户管理',15,'RW','1',NULL,NULL,NULL,NULL,1),(27,'集群性能',16,'RW','1',NULL,NULL,NULL,NULL,1),(28,'权限管理',17,'RW','1',NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `auth_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_privilege_resource_启弃`
--

DROP TABLE IF EXISTS `auth_privilege_resource_启弃`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_privilege_resource_启弃` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `privilege_id` bigint DEFAULT NULL,
  `resource_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_resource_privilege` (`resource_id`,`privilege_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_privilege_resource_启弃`
--

LOCK TABLES `auth_privilege_resource_启弃` WRITE;
/*!40000 ALTER TABLE `auth_privilege_resource_启弃` DISABLE KEYS */;
INSERT INTO `auth_privilege_resource_启弃` VALUES (1,3,1),(6,8,22),(9,9,23);
/*!40000 ALTER TABLE `auth_privilege_resource_启弃` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_resource`
--

DROP TABLE IF EXISTS `auth_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_resource`
--

LOCK TABLES `auth_resource` WRITE;
/*!40000 ALTER TABLE `auth_resource` DISABLE KEYS */;
INSERT INTO `auth_resource` VALUES (1,'ROOT','ROOT','ROOT','0','1',NULL,NULL,NULL,NULL,1),(2,'HOME','首页','HOME','1','1',NULL,NULL,NULL,NULL,0),(3,'PROJECT','工程管理','BASIC','1','1',NULL,NULL,NULL,NULL,0),(4,'DATASOURCE','数据源管理','BASIC','1','1',NULL,NULL,NULL,NULL,0),(5,'ATTACHMENT','下载中心','BASIC','1','1',NULL,NULL,NULL,NULL,0),(6,'DESIGNER','在线设计','ETL','1','1',NULL,NULL,NULL,NULL,0),(7,'PUBLISH','脚本发布','ETL','1','1',NULL,NULL,NULL,NULL,0),(8,'BATCH','批处理任务','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(9,'STREAMING','流处理任务','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(10,'PROCESS','进程管理','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(11,'LOG','日志跟踪','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(12,'MODEL','模型设计','REPORT','1','1',NULL,NULL,NULL,NULL,0),(13,'REPORT','统计设置','REPORT','1','1',NULL,NULL,NULL,NULL,0),(14,'LAYOUT','布局设置','REPORT','1','1',NULL,NULL,NULL,NULL,0),(15,'MEMBER','成员管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(16,'USER','用户管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(17,'METRICS','集群性能','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(18,'PRIVILEGE','权限管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(21,'9','[9]第二组','PROJECT','2','0','2024-01-17 00:58:46','jesse.18@163.com','jesse.18@163.com','2024-01-17 00:58:46',1),(22,'10','[10]第三组','PROJECT','2','1','2024-01-17 01:00:12','jesse.18@163.com','jesse.18@163.com','2024-01-17 01:00:12',1),(23,'11','[11]第四组','PROJECT','2','1','2024-01-17 01:03:45','jesse.18@163.com','jesse.18@163.com','2024-01-17 01:03:45',1),(24,'12','[12]第五组','PROJECT','2','1','2024-01-22 20:17:30','jesse.18@163.com','jesse.18@163.com','2024-01-22 20:17:30',1);
/*!40000 ALTER TABLE `auth_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_user`
--

DROP TABLE IF EXISTS `auth_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `wechat` varchar(255) DEFAULT NULL,
  `birth_date` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_user`
--

LOCK TABLES `auth_user` WRITE;
/*!40000 ALTER TABLE `auth_user` DISABLE KEYS */;
INSERT INTO `auth_user` VALUES (1,'jesse Han',NULL,'$2a$10$4kklyHMy8IF2.2Eh9s8RCuHVEuyL5HLBJ8I6P0lUV/rhlw1QPvLNG','jesse.18@163.com',NULL,NULL,NULL,'1',NULL,'2022-06-25 12:50:45',NULL,'2022-06-25 12:50:45',0),(2,'tom','M','$2a$10$oefM/JBewtrQEmRY/CgLeOcCInXiExQCZXCKhPlqpuxg6iZ10psKC','firegunner@126.com',NULL,NULL,NULL,'1','jesse.18@163.com','2024-01-22 15:13:40','jesse.18@163.com','2024-01-22 15:14:29',1),(3,'jerry','F','$2a$10$SaHG4.HCnPQm9romZjRf7.ht7weLvoy5k4fYupiatAW.L6aN6HFz6','abc@163.com',NULL,NULL,NULL,'1','jesse.18@163.com','2024-01-22 17:49:06','jesse.18@163.com','2024-01-22 17:49:06',1);
/*!40000 ALTER TABLE `auth_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_user_privilege`
--

DROP TABLE IF EXISTS `auth_user_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_user_privilege` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `privilege_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_privilege` (`user_id`,`privilege_id`) /*!80000 INVISIBLE */
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_user_privilege`
--

LOCK TABLES `auth_user_privilege` WRITE;
/*!40000 ALTER TABLE `auth_user_privilege` DISABLE KEYS */;
INSERT INTO `auth_user_privilege` VALUES (5,3,1),(11,9,1),(16,9,2),(18,12,1),(19,13,1),(20,14,1),(21,15,1),(22,16,1),(23,17,1),(24,18,1),(25,19,1),(26,20,1),(27,21,1),(28,22,1),(29,23,1),(30,24,1),(31,25,1),(32,26,1),(33,27,1),(34,28,1),(35,14,3),(36,13,3);
/*!40000 ALTER TABLE `auth_user_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `basic_datasource`
--

DROP TABLE IF EXISTS `basic_datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `basic_datasource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `project_id` bigint DEFAULT NULL,
  `use_cursor` bit(1) DEFAULT NULL,
  `use_pool` bit(1) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `parameter` varchar(255) DEFAULT NULL,
  `pool_initial` int DEFAULT NULL,
  `pool_initial_size` int DEFAULT NULL,
  `pool_max_active` int DEFAULT NULL,
  `pool_max_idle` int DEFAULT NULL,
  `pool_max_size` int DEFAULT NULL,
  `pool_max_wait` int DEFAULT NULL,
  `pool_min_idle` int DEFAULT NULL,
  `schema_name` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `port` int DEFAULT NULL,
  `data_space` varchar(255) DEFAULT NULL,
  `index_space` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL COMMENT 'jdbc url',
  `driver` varchar(255) DEFAULT NULL COMMENT '驱动名称',
  `status` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_datasource`
--

LOCK TABLES `basic_datasource` WRITE;
/*!40000 ALTER TABLE `basic_datasource` DISABLE KEYS */;
INSERT INTO `basic_datasource` VALUES (1,'本地数据库nxin_etl_2024_001_mybatis','mysql',11,_binary '\0',_binary '\0','root','1qaz@WSX','[{\"name\":\"userSSL\",\"value\":\"true\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"}]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'nxin_etl_2024_001_mybatis','127.0.0.1',3306,NULL,NULL,NULL,NULL,'1',NULL,'jesse.18@163.com','jesse.18@163.com','2024-01-26 00:59:21',5),(2,'world','mysql',1,_binary '\0',_binary '\0','root','1qaz@WSX','[{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"}]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'world','127.0.0.1',3306,NULL,NULL,NULL,NULL,'1','2024-02-19 11:36:09','jesse.18@163.com','jesse.18@163.com','2024-02-19 11:36:09',0),(3,'mysql jdbc','jdbc',1,_binary '\0',_binary '\0','root','1qaz@WSX','[{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"}]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'jdbc:mysql://localhost:3306/nxin_etl_2024_001_mybatis?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10','com.mysql.cj.jdbc.Driver','1','2024-04-10 23:31:35','jesse.18@163.com','jesse.18@163.com','2024-04-10 23:36:59',1);
/*!40000 ALTER TABLE `basic_datasource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `basic_project`
--

DROP TABLE IF EXISTS `basic_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `basic_project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_project`
--

LOCK TABLES `basic_project` WRITE;
/*!40000 ALTER TABLE `basic_project` DISABLE KEYS */;
INSERT INTO `basic_project` VALUES (1,'第一组',1,'第一组','1',NULL,'jesse.18@163.com','jesse.18@163.com','2024-02-17 20:16:31',25),(2,'第二组',1,'21','0',NULL,'jesse.18@163.com','jesse.18@163.com','2024-01-12 01:00:02',4),(3,'第三组',1,'3','0',NULL,'jesse.18@163.com','jesse.18@163.com','2024-01-12 00:47:33',3),(4,'第四组',1,NULL,'0','2024-01-12 00:47:43','jesse.18@163.com','jesse.18@163.com','2024-01-12 00:47:43',0),(9,'第二组',1,NULL,'0','2024-01-17 00:58:46','jesse.18@163.com','jesse.18@163.com','2024-01-18 13:43:34',2),(10,'第三组',1,NULL,'1','2024-01-17 01:00:12','jesse.18@163.com','jesse.18@163.com','2024-01-18 22:09:18',19),(11,'第四组',1,NULL,'1','2024-01-17 01:03:45','jesse.18@163.com','jesse.18@163.com','2024-02-17 23:50:38',11),(12,'第五组',1,NULL,'1','2024-01-22 20:17:30','jesse.18@163.com','jesse.18@163.com','2024-01-22 20:17:30',0);
/*!40000 ALTER TABLE `basic_project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `basic_project_user`
--

DROP TABLE IF EXISTS `basic_project_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `basic_project_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `project_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_project_user`
--

LOCK TABLES `basic_project_user` WRITE;
/*!40000 ALTER TABLE `basic_project_user` DISABLE KEYS */;
INSERT INTO `basic_project_user` VALUES (1,1,1),(2,2,1),(3,3,1),(4,4,1);
/*!40000 ALTER TABLE `basic_project_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kettle_running_process`
--

DROP TABLE IF EXISTS `kettle_running_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kettle_running_process` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `instance_id` varchar(255) DEFAULT NULL,
  `instance_name` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `prod` varchar(255) DEFAULT NULL,
  `project_id` bigint DEFAULT NULL,
  `shell_id` bigint DEFAULT NULL,
  `shell_publish_id` bigint DEFAULT NULL,
  `version` int NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_shell_id` (`shell_id`),
  KEY `idx_shell_publish_id` (`shell_publish_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1645 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kettle_running_process`
--

LOCK TABLES `kettle_running_process` WRITE;
/*!40000 ALTER TABLE `kettle_running_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kettle_shell`
--

DROP TABLE IF EXISTS `kettle_shell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kettle_shell` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `project_id` bigint DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `executable` bit(1) DEFAULT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `streaming` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  `md5_graph` varchar(255) DEFAULT NULL COMMENT 'mxgraph图形文件md5值',
  `md5_xml` varchar(255) DEFAULT NULL COMMENT '脚本文件md5值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_name_parent_id` (`name`,`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kettle_shell`
--

LOCK TABLES `kettle_shell` WRITE;
/*!40000 ALTER TABLE `kettle_shell` DISABLE KEYS */;
/*!40000 ALTER TABLE `kettle_shell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kettle_shell_publish`
--

DROP TABLE IF EXISTS `kettle_shell_publish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kettle_shell_publish` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `business_id` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `deploy_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `prod` varchar(255) DEFAULT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `streaming` varchar(255) DEFAULT NULL,
  `task_id` varchar(255) DEFAULT NULL,
  `project_id` bigint DEFAULT NULL,
  `shell_id` bigint DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `version` int NOT NULL,
  `md5_graph` varchar(255) DEFAULT NULL COMMENT '图形文本md5值',
  `md5_xml` varchar(255) DEFAULT NULL COMMENT '脚本md5值',
  PRIMARY KEY (`id`),
  KEY `idx_shell_id` (`shell_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kettle_shell_publish`
--

LOCK TABLES `kettle_shell_publish` WRITE;
/*!40000 ALTER TABLE `kettle_shell_publish` DISABLE KEYS */;
/*!40000 ALTER TABLE `kettle_shell_publish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_etl_job`
--

DROP TABLE IF EXISTS `log_etl_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_etl_job` (
  `ID_JOB` int DEFAULT NULL,
  `CHANNEL_ID` varchar(255) DEFAULT NULL,
  `JOBNAME` varchar(255) DEFAULT NULL,
  `STATUS` varchar(15) DEFAULT NULL,
  `LINES_READ` bigint DEFAULT NULL,
  `LINES_WRITTEN` bigint DEFAULT NULL,
  `LINES_UPDATED` bigint DEFAULT NULL,
  `LINES_INPUT` bigint DEFAULT NULL,
  `LINES_OUTPUT` bigint DEFAULT NULL,
  `LINES_REJECTED` bigint DEFAULT NULL,
  `ERRORS` bigint DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `ENDDATE` datetime DEFAULT NULL,
  `LOGDATE` datetime DEFAULT NULL,
  `DEPDATE` datetime DEFAULT NULL,
  `REPLAYDATE` datetime DEFAULT NULL,
  `LOG_FIELD` longtext,
  KEY `idx_id_job` (`ID_JOB`),
  KEY `idx_errors_status_jobname` (`ERRORS`,`STATUS`,`JOBNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_etl_job`
--

LOCK TABLES `log_etl_job` WRITE;
/*!40000 ALTER TABLE `log_etl_job` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_etl_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_etl_job_entry`
--

DROP TABLE IF EXISTS `log_etl_job_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_etl_job_entry` (
  `ID_BATCH` int DEFAULT NULL,
  `CHANNEL_ID` varchar(255) DEFAULT NULL,
  `LOG_DATE` datetime DEFAULT NULL,
  `TRANSNAME` varchar(255) DEFAULT NULL,
  `STEPNAME` varchar(255) DEFAULT NULL,
  `LINES_READ` bigint DEFAULT NULL,
  `LINES_WRITTEN` bigint DEFAULT NULL,
  `LINES_UPDATED` bigint DEFAULT NULL,
  `LINES_INPUT` bigint DEFAULT NULL,
  `LINES_OUTPUT` bigint DEFAULT NULL,
  `LINES_REJECTED` bigint DEFAULT NULL,
  `ERRORS` bigint DEFAULT NULL,
  `RESULT` varchar(5) DEFAULT NULL,
  `NR_RESULT_ROWS` bigint DEFAULT NULL,
  `NR_RESULT_FILES` bigint DEFAULT NULL,
  KEY `idx_id_batch` (`ID_BATCH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_etl_job_entry`
--

LOCK TABLES `log_etl_job_entry` WRITE;
/*!40000 ALTER TABLE `log_etl_job_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_etl_job_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_etl_transform`
--

DROP TABLE IF EXISTS `log_etl_transform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_etl_transform` (
  `ID_BATCH` int DEFAULT NULL,
  `CHANNEL_ID` varchar(255) DEFAULT NULL,
  `TRANSNAME` varchar(255) DEFAULT NULL,
  `STATUS` varchar(15) DEFAULT NULL,
  `LINES_READ` bigint DEFAULT NULL,
  `LINES_WRITTEN` bigint DEFAULT NULL,
  `LINES_UPDATED` bigint DEFAULT NULL,
  `LINES_INPUT` bigint DEFAULT NULL,
  `LINES_OUTPUT` bigint DEFAULT NULL,
  `LINES_REJECTED` bigint DEFAULT NULL,
  `ERRORS` bigint DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `ENDDATE` datetime DEFAULT NULL,
  `LOGDATE` datetime DEFAULT NULL,
  `DEPDATE` datetime DEFAULT NULL,
  `REPLAYDATE` datetime DEFAULT NULL,
  `LOG_FIELD` longtext,
  KEY `IDX_id_batch` (`ID_BATCH`),
  KEY `idx_errors_status_transname` (`ERRORS`,`STATUS`,`TRANSNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_etl_transform`
--

LOCK TABLES `log_etl_transform` WRITE;
/*!40000 ALTER TABLE `log_etl_transform` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_etl_transform` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_etl_transform_channel`
--

DROP TABLE IF EXISTS `log_etl_transform_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_etl_transform_channel` (
  `ID_BATCH` int DEFAULT NULL,
  `CHANNEL_ID` varchar(255) DEFAULT NULL,
  `LOG_DATE` datetime DEFAULT NULL,
  `LOGGING_OBJECT_TYPE` varchar(255) DEFAULT NULL,
  `OBJECT_NAME` varchar(255) DEFAULT NULL,
  `OBJECT_COPY` varchar(255) DEFAULT NULL,
  `REPOSITORY_DIRECTORY` varchar(255) DEFAULT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `OBJECT_ID` varchar(255) DEFAULT NULL,
  `OBJECT_REVISION` varchar(255) DEFAULT NULL,
  `PARENT_CHANNEL_ID` varchar(255) DEFAULT NULL,
  `ROOT_CHANNEL_ID` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_etl_transform_channel`
--

LOCK TABLES `log_etl_transform_channel` WRITE;
/*!40000 ALTER TABLE `log_etl_transform_channel` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_etl_transform_channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_etl_transform_metrics`
--

DROP TABLE IF EXISTS `log_etl_transform_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_etl_transform_metrics` (
  `ID_BATCH` int DEFAULT NULL,
  `CHANNEL_ID` varchar(255) DEFAULT NULL,
  `LOG_DATE` datetime DEFAULT NULL,
  `METRICS_DATE` datetime DEFAULT NULL,
  `METRICS_CODE` varchar(255) DEFAULT NULL,
  `METRICS_DESCRIPTION` varchar(255) DEFAULT NULL,
  `METRICS_SUBJECT` varchar(255) DEFAULT NULL,
  `METRICS_TYPE` varchar(255) DEFAULT NULL,
  `METRICS_VALUE` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_etl_transform_metrics`
--

LOCK TABLES `log_etl_transform_metrics` WRITE;
/*!40000 ALTER TABLE `log_etl_transform_metrics` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_etl_transform_metrics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_etl_transform_performance`
--

DROP TABLE IF EXISTS `log_etl_transform_performance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_etl_transform_performance` (
  `ID_BATCH` int DEFAULT NULL,
  `SEQ_NR` int DEFAULT NULL,
  `LOGDATE` datetime DEFAULT NULL,
  `TRANSNAME` varchar(255) DEFAULT NULL,
  `STEPNAME` varchar(255) DEFAULT NULL,
  `STEP_COPY` int DEFAULT NULL,
  `LINES_READ` bigint DEFAULT NULL,
  `LINES_WRITTEN` bigint DEFAULT NULL,
  `LINES_UPDATED` bigint DEFAULT NULL,
  `LINES_INPUT` bigint DEFAULT NULL,
  `LINES_OUTPUT` bigint DEFAULT NULL,
  `LINES_REJECTED` bigint DEFAULT NULL,
  `ERRORS` bigint DEFAULT NULL,
  `INPUT_BUFFER_ROWS` bigint DEFAULT NULL,
  `OUTPUT_BUFFER_ROWS` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_etl_transform_performance`
--

LOCK TABLES `log_etl_transform_performance` WRITE;
/*!40000 ALTER TABLE `log_etl_transform_performance` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_etl_transform_performance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_etl_transform_step`
--

DROP TABLE IF EXISTS `log_etl_transform_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_etl_transform_step` (
  `ID_BATCH` int DEFAULT NULL,
  `CHANNEL_ID` varchar(255) DEFAULT NULL,
  `LOG_DATE` datetime DEFAULT NULL,
  `TRANSNAME` varchar(255) DEFAULT NULL,
  `STEPNAME` varchar(255) DEFAULT NULL,
  `STEP_COPY` int DEFAULT NULL,
  `LINES_READ` bigint DEFAULT NULL,
  `LINES_WRITTEN` bigint DEFAULT NULL,
  `LINES_UPDATED` bigint DEFAULT NULL,
  `LINES_INPUT` bigint DEFAULT NULL,
  `LINES_OUTPUT` bigint DEFAULT NULL,
  `LINES_REJECTED` bigint DEFAULT NULL,
  `ERRORS` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_etl_transform_step`
--

LOCK TABLES `log_etl_transform_step` WRITE;
/*!40000 ALTER TABLE `log_etl_transform_step` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_etl_transform_step` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_history`
--

DROP TABLE IF EXISTS `task_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `running_process_id` bigint DEFAULT NULL COMMENT 'kettle运行时的实例id',
  `shell_publish_id` bigint DEFAULT NULL,
  `log_channel_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `begin_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_history`
--

LOCK TABLES `task_history` WRITE;
/*!40000 ALTER TABLE `task_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-31 14:25:55
