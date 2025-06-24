-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: nxin_etl_2024_001_mybatis
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `auth_apply`
--

DROP TABLE IF EXISTS `auth_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_apply` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `privilege_id` bigint DEFAULT NULL COMMENT '权限ID',
  `reason` varchar(255) DEFAULT NULL COMMENT '申请原因',
  `expire_date` datetime DEFAULT NULL COMMENT '失效日期',
  `audit_status` varchar(1) DEFAULT NULL COMMENT '审核状态',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `idx_status_audit_stauts_privilege_id` (`status`,`audit_status`,`privilege_id`) /*!80000 INVISIBLE */,
  KEY `idx_creator` (`creator`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限申请';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_apply`
--

LOCK TABLES `auth_apply` WRITE;
/*!40000 ALTER TABLE `auth_apply` DISABLE KEYS */;
INSERT INTO `auth_apply` VALUES (1,57,'aaa','2027-01-01 00:00:00','0','1','2025-06-23 00:00:00','jesse.18@163.com','jesse.18@163.com','2025-06-23 00:00:00',1);
/*!40000 ALTER TABLE `auth_apply` ENABLE KEYS */;
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
  `category` varchar(2) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT '辅助描述权限信息',
  `status` varchar(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_status_resource_id_category` (`status`,`resource_id`,`category`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_privilege`
--

LOCK TABLES `auth_privilege` WRITE;
/*!40000 ALTER TABLE `auth_privilege` DISABLE KEYS */;
INSERT INTO `auth_privilege` VALUES (3,'ROOT',1,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(7,'第二组',21,'RW',NULL,'1','2024-01-17 00:58:46','jesse.18@163.com','jesse.18@163.com','2024-01-17 00:58:46',1),(8,'第三组',22,'RW',NULL,'1','2024-01-17 01:00:12','jesse.18@163.com','jesse.18@163.com','2024-01-17 01:00:12',1),(9,'第四组',23,'RW',NULL,'1','2024-01-17 01:03:45','jesse.18@163.com','jesse.18@163.com','2024-01-17 01:03:45',1),(10,'第二组',21,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(11,'第五组',24,'RW',NULL,'1','2024-01-22 20:17:31','jesse.18@163.com','jesse.18@163.com','2024-01-22 20:17:31',1),(12,'第五组',24,'RW',NULL,'1','2024-01-22 20:17:31','jesse.18@163.com','jesse.18@163.com','2024-01-22 20:17:31',1),(13,'首页',2,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(14,'工程管理',3,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(15,'数据源管理',4,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(16,'下载中心',5,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(17,'在线设计',6,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(18,'脚本发布',7,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(19,'批处理任务',8,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(20,'流处理任务',9,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(21,'进程管理',10,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(22,'日志跟踪',11,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(23,'模型设计',12,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(24,'统计设置',13,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(25,'布局设置',14,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(26,'用户管理',15,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(27,'集群性能',16,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(28,'权限管理',18,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(29,'第三组',22,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(30,'第四组',23,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(31,'第六组',25,'R',NULL,'1','2025-06-03 12:10:31','jesse.18@163.com','jesse.18@163.com','2025-06-03 12:10:31',1),(32,'第六组',25,'RW',NULL,'1','2025-06-03 12:10:31','jesse.18@163.com','jesse.18@163.com','2025-06-03 12:10:31',1),(35,'样例基础柱状图',27,'R',NULL,'1','2025-06-16 11:39:08','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:39:08',1),(36,'样例基础柱状图',27,'RW',NULL,'1','2025-06-16 11:39:08','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:39:08',1),(37,'样例产品销售报表',28,'R',NULL,'1','2025-06-19 11:10:35','jesse.18@163.com','jesse.18@163.com','2025-06-19 11:10:35',1),(38,'样例产品销售报表',28,'RW',NULL,'1','2025-06-19 11:10:35','jesse.18@163.com','jesse.18@163.com','2025-06-19 11:10:35',1),(41,'产品销售模型',30,'R',NULL,'1','2025-06-20 19:59:27','jesse.18@163.com','jesse.18@163.com','2025-06-20 19:59:27',1),(42,'产品销售模型',30,'RW',NULL,'1','2025-06-20 19:59:27','jesse.18@163.com','jesse.18@163.com','2025-06-20 19:59:27',1),(43,'报表图形',36,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(44,'本地数据库nxin_etl_2024_001_mybatis',31,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(45,'本地数据库nxin_etl_2024_001_mybatis',31,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(46,'world',32,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(47,'world',32,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(48,'mysql jdbc',33,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(49,'mysql jdbc',33,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(50,'oracle',34,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(51,'oracle',34,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(52,'postgres',35,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(53,'postgres',35,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(54,'第一组',37,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(55,'第一组',37,'RW',NULL,'1',NULL,NULL,NULL,NULL,1),(56,'第七组',38,'R',NULL,'1','2025-06-22 15:16:28','jesse.18@163.com','jesse.18@163.com','2025-06-22 15:16:28',1),(57,'第七组',38,'RW',NULL,'1','2025-06-22 15:16:28','jesse.18@163.com','jesse.18@163.com','2025-06-22 15:16:28',1),(58,'ABC',39,'R',NULL,'1',NULL,NULL,NULL,NULL,1),(59,'ABC',39,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
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
  `level` varchar(1) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_status_code_level_cateogry` (`status`,`code`,`level`,`category`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_resource`
--

LOCK TABLES `auth_resource` WRITE;
/*!40000 ALTER TABLE `auth_resource` DISABLE KEYS */;
INSERT INTO `auth_resource` VALUES (1,'ROOT','ROOT','ROOT','0','1',NULL,NULL,NULL,NULL,1),(2,'HOME','首页','HOME','1','1',NULL,NULL,NULL,NULL,0),(3,'PROJECT','工程管理','BASIC','1','1',NULL,NULL,NULL,NULL,0),(4,'DATASOURCE','数据源管理','BASIC','1','1',NULL,NULL,NULL,NULL,0),(5,'ATTACHMENT','下载中心','BASIC','1','1',NULL,NULL,NULL,NULL,0),(6,'DESIGNER','在线设计','ETL','1','1',NULL,NULL,NULL,NULL,0),(7,'PUBLISH','脚本发布','ETL','1','1',NULL,NULL,NULL,NULL,0),(8,'BATCH','批处理任务','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(9,'STREAMING','流处理任务','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(10,'PROCESS','进程管理','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(11,'LOG','日志跟踪','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(12,'MODEL','模型设计','BI','1','1',NULL,NULL,NULL,NULL,0),(13,'REPORT','统计设置','BI','1','1',NULL,NULL,NULL,NULL,0),(14,'LAYOUT','布局设置','BI','1','1',NULL,NULL,NULL,NULL,0),(15,'MEMBER','成员管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(16,'USER','用户管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(17,'METRICS','集群性能','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(18,'PRIVILEGE','权限管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(21,'9','第二组','PROJECT','2','0','2024-01-17 00:58:46','jesse.18@163.com','jesse.18@163.com','2024-01-17 00:58:46',1),(22,'10','第三组','PROJECT','2','1','2024-01-17 01:00:12','jesse.18@163.com','jesse.18@163.com','2024-01-17 01:00:12',1),(23,'11','第四组','PROJECT','2','1','2024-01-17 01:03:45','jesse.18@163.com','jesse.18@163.com','2024-01-17 01:03:45',1),(24,'12','第五组','PROJECT','2','1','2024-01-22 20:17:30','jesse.18@163.com','jesse.18@163.com','2024-01-22 20:17:30',1),(25,'13','第六组','PROJECT','2','1','2025-06-03 12:10:31','jesse.18@163.com','jesse.18@163.com','2025-06-03 12:10:31',1),(27,'1','样例基础柱状图','CHART','2','1','2025-06-16 11:39:08','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:39:08',1),(28,'1','样例产品销售报表','REPORT','2','1','2025-06-19 11:10:35','jesse.18@163.com','jesse.18@163.com','2025-06-19 11:10:35',1),(30,'3','产品销售模型','MODEL','2','1','2025-06-20 19:59:27','jesse.18@163.com','jesse.18@163.com','2025-06-20 19:59:27',1),(31,'1','本地数据库nxin_etl_2024_001_mybatis','DATASOURCE','2','1',NULL,NULL,NULL,NULL,1),(32,'2','world','DATASOURCE','2','1',NULL,NULL,NULL,NULL,1),(33,'3','mysql jdbc','DATASOURCE','2','1',NULL,NULL,NULL,NULL,1),(34,'7','oracle','DATASOURCE','2','1',NULL,NULL,NULL,NULL,1),(35,'8','postgres','DATASOURCE','2','1',NULL,NULL,NULL,NULL,1),(36,'CHART','报表图形','BI','1','1',NULL,NULL,NULL,NULL,1),(37,'1','第一组','PROJECT','2','1',NULL,NULL,NULL,NULL,1),(38,'14','第七组','PROJECT','2','1','2025-06-22 15:16:28','jesse.18@163.com','jesse.18@163.com','2025-06-22 15:16:28',1),(39,'1','ABC','MODEL','2','1','2025-06-20 19:59:27','jesse.18@163.com','jesse.18@163.com','2025-06-20 19:59:27',1);
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
  `status` varchar(1) DEFAULT NULL,
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
  KEY `idx_user_id` (`user_id`) /*!80000 INVISIBLE */,
  KEY `idx_privilege_id` (`privilege_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_user_privilege`
--

LOCK TABLES `auth_user_privilege` WRITE;
/*!40000 ALTER TABLE `auth_user_privilege` DISABLE KEYS */;
INSERT INTO `auth_user_privilege` VALUES (5,3,1),(16,9,2),(19,13,1),(20,14,1),(21,15,1),(22,16,1),(23,17,1),(24,18,1),(25,19,1),(26,20,1),(27,21,1),(28,22,1),(29,23,1),(30,24,1),(31,25,1),(32,26,1),(33,27,1),(34,28,1),(35,14,3),(36,13,3),(37,9,1),(39,8,2),(42,29,3),(43,30,3),(44,32,1),(45,31,2),(46,34,1),(47,36,1),(48,38,1),(50,42,1),(51,55,2),(52,57,1),(53,28,3);
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
  `category` varchar(255) DEFAULT NULL COMMENT '连接方式',
  `generic` bit(1) DEFAULT NULL COMMENT '使用自定义(jbdc)方式连接',
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
  `charset` varchar(255) DEFAULT NULL COMMENT '字符集',
  `status` varchar(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_status_name` (`status`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_datasource`
--

LOCK TABLES `basic_datasource` WRITE;
/*!40000 ALTER TABLE `basic_datasource` DISABLE KEYS */;
INSERT INTO `basic_datasource` VALUES (1,'本地数据库nxin_etl_2024_001_mybatis','mysql',_binary '\0',11,_binary '\0',_binary '\0','root','1qaz@WSX','[{\"name\":\"userSSL\",\"value\":\"true\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"}]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'nxin_etl_2024_001_mybatis','127.0.0.1',3306,NULL,NULL,NULL,NULL,NULL,'1',NULL,'jesse.18@163.com','jesse.18@163.com','2024-01-26 00:59:21',5),(2,'world','MYSQL',_binary '\0',1,_binary '',_binary '\0','root','1qaz@WSX','[{\"name\":\"defaultFetchSize\",\"value\":\"500\"},{\"name\":\"useCursorFetch\",\"value\":\"true\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"}]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'world','127.0.0.1',3306,NULL,NULL,NULL,NULL,NULL,'1','2024-02-19 11:36:09','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:14:31',4),(3,'mysql jdbc','MYSQL',_binary '',1,_binary '\0',_binary '\0','root','1qaz@WSX','[{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"}]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'jdbc:mysql://localhost:3306/nxin_etl_2024_001_mybatis?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10','com.mysql.cj.jdbc.Driver',NULL,'1','2024-04-10 23:31:35','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:14:18',3),(7,'oracle','ORACLE',_binary '\0',1,_binary '\0',_binary '\0','scott','tiger','[{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"}]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'orcl','192.168.153.128',1521,NULL,NULL,NULL,NULL,NULL,'1','2025-04-22 11:24:37','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:14:12',2),(8,'postgres','POSTGRESQL',_binary '\0',1,_binary '\0',_binary '\0','postgres','1qaz@WSX','[{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"},{\"name\":\"\",\"value\":\"\"}]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'postgres','localhost',5432,NULL,NULL,NULL,NULL,NULL,'1','2025-05-29 10:05:49','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:14:07',2);
/*!40000 ALTER TABLE `basic_datasource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `basic_dictionary`
--

DROP TABLE IF EXISTS `basic_dictionary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `basic_dictionary` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_status_code` (`status`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_dictionary`
--

LOCK TABLES `basic_dictionary` WRITE;
/*!40000 ALTER TABLE `basic_dictionary` DISABLE KEYS */;
INSERT INTO `basic_dictionary` VALUES (1,'DATASOURCE','数据源',NULL,'1','2025-06-03 13:15:33','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:18:29',2),(2,'FTP','FTP服务器类型',NULL,'1','2025-06-03 13:37:58','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:37:58',1),(3,'FTP-PROXY','FTP代理类型',NULL,'1','2025-06-04 12:17:40','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:17:40',1),(5,'MYSQL-COLUMN-CATEGORY','mysql字段类型',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(6,'ORACLE-COLUMN-CATEGORY','oracle字段类型',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(7,'REPORT-CHART-CATEGORY','报表图形类别','为BI分析-报表图形模块提供图形类型约束','1','2025-06-16 11:28:23','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:28:23',1);
/*!40000 ALTER TABLE `basic_dictionary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `basic_dictionary_item`
--

DROP TABLE IF EXISTS `basic_dictionary_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `basic_dictionary_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dictionary_id` bigint DEFAULT NULL COMMENT '字典表ID',
  `name` varchar(255) DEFAULT NULL COMMENT '字典项键',
  `value` varchar(255) DEFAULT NULL COMMENT '字典项值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_status_dictionary_id` (`status`,`dictionary_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典项';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_dictionary_item`
--

LOCK TABLES `basic_dictionary_item` WRITE;
/*!40000 ALTER TABLE `basic_dictionary_item` DISABLE KEYS */;
INSERT INTO `basic_dictionary_item` VALUES (4,1,'mysql','MYSQL',NULL,'1','2025-06-03 13:18:29','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:18:29',1),(5,1,'postgresql','POSTGRESQL',NULL,'1','2025-06-03 13:18:29','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:18:29',1),(6,1,'oracle','ORACLE',NULL,'1','2025-06-03 13:18:29','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:18:29',1),(7,2,'FTP','FTP',NULL,'1','2025-06-03 13:37:58','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:37:58',1),(8,2,'SFTP','SFTP',NULL,'1','2025-06-03 13:37:58','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:37:58',1),(9,3,'HTTP','HTTP',NULL,'1','2025-06-04 12:17:40','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:17:40',1),(10,3,'SOCKS5','SOCKS5',NULL,'1','2025-06-04 12:17:40','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:17:40',1),(13,5,'int','int',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(14,5,'bigint','bigint',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(15,5,'varchar','varchar',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(16,5,'date','date',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(17,5,'datetime','datetime',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(18,5,'time','time',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(19,5,'decimal','decimal',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(20,5,'float','float',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(21,5,'double','double',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(22,6,'varchar2','varchar2',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(23,6,'number','number',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(24,6,'integer','integer',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(25,6,'long','long',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(26,6,'float','float',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(27,6,'date','date',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(28,6,'timestamp','timestamp',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(29,7,'柱状图','bar','基本柱状图/堆叠柱状图/动态排序柱状图/阶梯瀑布图','1','2025-06-16 11:28:23','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:28:23',1),(30,7,'饼图','pie','基础饼图/圆环图/南丁格尔图（玫瑰图）','1','2025-06-16 11:28:23','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:28:23',1),(31,7,'折线图','line','基础折线图/堆叠折线图/区域面积图/平滑曲线图/阶梯线图','1','2025-06-16 11:28:23','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:28:23',1),(32,7,'散点图','scatter','基础散点图','1','2025-06-16 11:28:23','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:28:23',1);
/*!40000 ALTER TABLE `basic_dictionary_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `basic_ftp`
--

DROP TABLE IF EXISTS `basic_ftp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `basic_ftp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL COMMENT 'FTP SFTP',
  `project_id` bigint DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `use_private_key` bit(1) DEFAULT NULL,
  `private_key` text,
  `private_key_password` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `port` int DEFAULT NULL,
  `proxy_category` varchar(255) DEFAULT NULL,
  `proxy_host` varchar(255) DEFAULT NULL,
  `proxy_port` int DEFAULT NULL,
  `proxy_username` varchar(255) DEFAULT NULL,
  `proxy_password` varchar(255) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_status_name` (`status`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='FTP服务器信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_ftp`
--

LOCK TABLES `basic_ftp` WRITE;
/*!40000 ALTER TABLE `basic_ftp` DISABLE KEYS */;
INSERT INTO `basic_ftp` VALUES (1,'localhost','SFTP',1,'nxin','1qaz@WSX',_binary '\0',NULL,NULL,'127.0.0.1',22,NULL,NULL,NULL,NULL,NULL,'1',NULL,'jesse.18@163.com',NULL,NULL,0),(2,'docker','FTP',1,'admin','admin',_binary '\0',NULL,NULL,'127.0.0.1',21000,NULL,NULL,NULL,NULL,NULL,'1',NULL,'jesse.18@163.com',NULL,NULL,0),(3,'ubuntu','SFTP',1,'hanlei',NULL,_binary '','-----BEGIN OPENSSH PRIVATE KEY-----\n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\n-----END OPENSSH PRIVATE KEY-----\n',NULL,'172.29.74.100',22,NULL,NULL,NULL,NULL,NULL,'1',NULL,'jesse.18@163.com',NULL,NULL,0);
/*!40000 ALTER TABLE `basic_ftp` ENABLE KEYS */;
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
  `status` varchar(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_status_name` (`status`,`name`) /*!80000 INVISIBLE */,
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_project`
--

LOCK TABLES `basic_project` WRITE;
/*!40000 ALTER TABLE `basic_project` DISABLE KEYS */;
INSERT INTO `basic_project` VALUES (1,'第一组',1,'第一组','1',NULL,'jesse.18@163.com','jesse.18@163.com','2024-02-17 20:16:31',25),(2,'第二组',1,'21','0',NULL,'jesse.18@163.com','jesse.18@163.com','2024-01-12 01:00:02',4),(3,'第三组',1,'3','0',NULL,'jesse.18@163.com','jesse.18@163.com','2024-01-12 00:47:33',3),(4,'第四组',1,NULL,'0','2024-01-12 00:47:43','jesse.18@163.com','jesse.18@163.com','2024-01-12 00:47:43',0),(9,'第二组',1,NULL,'0','2024-01-17 00:58:46','jesse.18@163.com','jesse.18@163.com','2024-01-18 13:43:34',2),(10,'第三组',1,NULL,'1','2024-01-17 01:00:12','jesse.18@163.com','jesse.18@163.com','2024-01-18 22:09:18',19),(11,'第四组',1,NULL,'1','2024-01-17 01:03:45','jesse.18@163.com','jesse.18@163.com','2024-02-17 23:50:38',11),(12,'第五组',1,NULL,'1','2024-01-22 20:17:30','jesse.18@163.com','jesse.18@163.com','2024-01-22 20:17:30',0),(13,'第六组',1,NULL,'1','2025-06-03 12:10:31','jesse.18@163.com','jesse.18@163.com','2025-06-03 12:10:31',1),(14,'第七组',1,NULL,'1','2025-06-22 15:16:28','jesse.18@163.com','jesse.18@163.com','2025-06-22 15:16:28',1);
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
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`) /*!80000 INVISIBLE */,
  KEY `idx_user_id` (`user_id`)
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
-- Table structure for table `bi_chart`
--

DROP TABLE IF EXISTS `bi_chart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bi_chart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `category` varchar(255) DEFAULT NULL COMMENT '图形类型',
  `options` varchar(1000) DEFAULT NULL COMMENT '选项',
  `data` text COMMENT '样例数据',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `publish` bit(1) DEFAULT NULL COMMENT '是否发布',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `idx_status_name` (`status`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='展示图标';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_chart`
--

LOCK TABLES `bi_chart` WRITE;
/*!40000 ALTER TABLE `bi_chart` DISABLE KEYS */;
INSERT INTO `bi_chart` VALUES (1,'demo-basic-bar','样例基础柱状图','pie','{\n  \"legend\": {},\n  \"tooltip\": {},\n  \"dataset\": {\n    \"dimensions\": [<#list dimensions as dim>\"${dim}\"<#if dim_has_next>,</#if></#list>],\n    \"source\": [<#list source as row>{<#list row?keys as key>\"${key}\": \"${row[key]}\"<#if key_has_next>,</#if></#list>}<#if row_has_next>,</#if></#list>]\n  },\n  \"xAxis\": {\"type\": \"category\"},\n  \"yAxis\": {},\n  \"series\": [<#list dimensions as dim><#if dim_index gt 0>{ \"type\": \"bar\" }<#if dim_has_next>,</#if></#if></#list>]\n}','{\n \"dimensions\": [\"product\", \"2015\", \"2016\", \"2017\"],\n \"source\": [\n      { \"product\": \"Matcha Latte\", \"2015\": 43.3, \"2016\": 85.8, \"2017\": 93.7 },\n      { \"product\": \"Milk Tea\", \"2015\": 83.1, \"2016\": 73.4, \"2017\": 55.1 },\n      { \"product\": \"Cheese Cocoa\", \"2015\": 86.4, \"2016\": 65.2, \"2017\": 82.5 },\n      { \"product\": \"Walnut Brownie\", \"2015\": 72.4, \"2016\": 53.9, \"2017\": 39.1 }\n    ]\n}','x轴为【dimensions】中第一个元素在source集合中的枚举值，y轴为【dimensions】中剩余元素在source集合中对应的数值',_binary '','2025-06-20 23:21:40','1','2025-06-15 00:00:00','jesse.18@163.com','jesse.18@163.com','2025-06-20 23:21:40',1);
/*!40000 ALTER TABLE `bi_chart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bi_chart_params`
--

DROP TABLE IF EXISTS `bi_chart_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bi_chart_params` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field` varchar(255) DEFAULT NULL COMMENT '字段',
  `category` varchar(255) DEFAULT NULL COMMENT '字段类型',
  `description` varchar(100) DEFAULT NULL COMMENT '字段描述',
  `parent_id` bigint DEFAULT NULL COMMENT '上级字段ID',
  `path` varchar(255) DEFAULT NULL COMMENT '字段路径',
  `chart_id` bigint DEFAULT NULL COMMENT '报表图形ID',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `idx_status_chart_id` (`status`,`chart_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表图形参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_chart_params`
--

LOCK TABLES `bi_chart_params` WRITE;
/*!40000 ALTER TABLE `bi_chart_params` DISABLE KEYS */;
INSERT INTO `bi_chart_params` VALUES (1,'dimensions','array','维度集合',NULL,NULL,1,'1','2025-06-15 00:00:00',NULL,NULL,NULL,1),(2,'source','array','数据集',NULL,NULL,1,'1','2025-06-15 00:00:00',NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `bi_chart_params` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bi_metadata`
--

DROP TABLE IF EXISTS `bi_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bi_metadata` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `column_name` varchar(255) DEFAULT NULL COMMENT '列名',
  `column_code` varchar(255) DEFAULT NULL COMMENT '列编码',
  `column_category` varchar(255) DEFAULT NULL COMMENT '列类型',
  `column_length` int DEFAULT NULL COMMENT '列长度',
  `column_decimal` int DEFAULT NULL COMMENT '列小数长度',
  `column_not_null` bit(1) DEFAULT NULL COMMENT '是否为空',
  `column_foreign_model_id` bigint DEFAULT NULL COMMENT '列绑定的外键模型ID',
  `model_id` bigint DEFAULT NULL COMMENT '模型ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_status_model_id` (`status`,`model_id`) /*!80000 INVISIBLE */,
  KEY `idx_foreign_model_id` (`column_foreign_model_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模型元数据';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_metadata`
--

LOCK TABLES `bi_metadata` WRITE;
/*!40000 ALTER TABLE `bi_metadata` DISABLE KEYS */;
INSERT INTO `bi_metadata` VALUES (1,'编码','code','varchar',50,NULL,NULL,1,1,'2025-06-20 19:16:00',NULL,'2025-06-20 20:34:54','jesse.18@163.com','1',2),(2,'名称','name','varchar',255,NULL,NULL,NULL,1,'2025-06-20 19:16:00',NULL,'2025-06-20 20:34:54','jesse.18@163.com','1',2),(3,'数量','amount','int',NULL,NULL,NULL,NULL,1,'2025-06-20 19:16:00',NULL,'2025-06-20 20:34:54','jesse.18@163.com','1',2),(4,'价格','price','float',10,2,NULL,NULL,1,'2025-06-20 19:16:00',NULL,'2025-06-20 20:34:54','jesse.18@163.com','1',2),(5,'类型(销售/退货)','category','varchar',10,NULL,NULL,NULL,1,'2025-06-20 19:16:00',NULL,'2025-06-20 20:34:54','jesse.18@163.com','1',2),(6,'发生时间','created','datetime',NULL,NULL,NULL,NULL,1,'2025-06-20 19:16:00',NULL,'2025-06-20 20:34:54','jesse.18@163.com','1',2),(7,'产品','product','varchar',100,NULL,NULL,NULL,3,'2025-06-20 19:59:27','jesse.18@163.com','2025-06-20 20:50:42','jesse.18@163.com','1',5),(8,'年份','year','varchar',10,NULL,NULL,NULL,3,'2025-06-20 19:59:27','jesse.18@163.com','2025-06-20 20:50:42','jesse.18@163.com','1',5),(9,'销售金额','sales','decimal',8,2,NULL,NULL,3,'2025-06-20 19:59:27','jesse.18@163.com','2025-06-20 20:50:42','jesse.18@163.com','1',5),(10,'说明','description','varchar',255,NULL,NULL,NULL,3,'2025-06-20 20:03:28','jesse.18@163.com','2025-06-20 20:50:42','jesse.18@163.com','0',5),(11,'说明','description','varchar',255,NULL,NULL,NULL,1,'2025-06-20 20:34:54','jesse.18@163.com','2025-06-20 20:34:54','jesse.18@163.com','1',1),(12,'备注','remark','varchar',255,NULL,NULL,NULL,3,'2025-06-20 20:50:42','jesse.18@163.com','2025-06-20 20:50:42','jesse.18@163.com','1',1);
/*!40000 ALTER TABLE `bi_metadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bi_model`
--

DROP TABLE IF EXISTS `bi_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bi_model` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL COMMENT '模型编码',
  `name` varchar(255) DEFAULT NULL COMMENT '模型名称',
  `description` varchar(255) DEFAULT NULL COMMENT '模型描述',
  `datasource_id` bigint DEFAULT NULL COMMENT '数据源ID',
  `project_id` bigint DEFAULT NULL COMMENT '工程ID',
  `publish` bit(1) DEFAULT NULL COMMENT '是否发布',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `idx_status_project_id_name` (`status`,`project_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='BI模型';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_model`
--

LOCK TABLES `bi_model` WRITE;
/*!40000 ALTER TABLE `bi_model` DISABLE KEYS */;
INSERT INTO `bi_model` VALUES (1,'abc','ABC',NULL,2,1,_binary '','2025-06-20 20:47:01','2025-06-20 19:15:56','jesse.18@163.com','2025-06-20 20:47:01','jesse.18@163.com','1',6),(3,'DEMO-PRODUCT_SALE','产品销售模型',NULL,2,1,_binary '','2025-06-20 20:52:17','2025-06-20 19:59:27','jesse.18@163.com','2025-06-20 20:52:17','jesse.18@163.com','1',8);
/*!40000 ALTER TABLE `bi_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bi_report`
--

DROP TABLE IF EXISTS `bi_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bi_report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL COMMENT '报表编码',
  `name` varchar(255) DEFAULT NULL COMMENT '报表名称',
  `project_id` bigint DEFAULT NULL COMMENT '工程ID',
  `chart_id` bigint DEFAULT NULL COMMENT '展示图表ID',
  `model_id` bigint DEFAULT NULL COMMENT '模型ID',
  `script` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '报表脚本',
  `mapping` varchar(1000) DEFAULT NULL COMMENT '图表结构映射字段',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `publish` bit(1) DEFAULT NULL COMMENT '是否发布',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `idx_status_project_id_name` (`status`,`project_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表设计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_report`
--

LOCK TABLES `bi_report` WRITE;
/*!40000 ALTER TABLE `bi_report` DISABLE KEYS */;
INSERT INTO `bi_report` VALUES (1,'DEMO-REPORT-PRODUCT-SALE','样例产品销售报表',1,1,1,NULL,NULL,NULL,_binary '','2025-06-22 20:41:00','1','2025-06-22 00:00:00','jesse.18@163.com','jesse.18@163.com','2025-06-22 22:32:23',3);
/*!40000 ALTER TABLE `bi_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bi_report_chart_params`
--

DROP TABLE IF EXISTS `bi_report_chart_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bi_report_chart_params` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `report_id` bigint DEFAULT NULL COMMENT '报表ID',
  `chart_params_id` bigint DEFAULT NULL COMMENT '参数ID',
  `datasource_id` bigint DEFAULT NULL COMMENT '数据源ID',
  `category` varchar(255) DEFAULT NULL COMMENT '类型',
  `script` text COMMENT '脚本',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `idx_status_report_id` (`status`,`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表模型图形参数定义';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_report_chart_params`
--

LOCK TABLES `bi_report_chart_params` WRITE;
/*!40000 ALTER TABLE `bi_report_chart_params` DISABLE KEYS */;
INSERT INTO `bi_report_chart_params` VALUES (5,1,1,NULL,'constant','[\'name\',\'数量\']','1','2025-06-22 22:27:29','jesse.18@163.com','jesse.18@163.com','2025-06-22 22:32:23',1),(6,1,2,NULL,'sql','select name,amount as 数量 from abc','1','2025-06-22 22:27:29','jesse.18@163.com','jesse.18@163.com','2025-06-22 22:32:23',1);
/*!40000 ALTER TABLE `bi_report_chart_params` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kettle_attachment_storage`
--

DROP TABLE IF EXISTS `kettle_attachment_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kettle_attachment_storage` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `project_id` bigint DEFAULT NULL,
  `shell_id` bigint DEFAULT NULL,
  `shell_name` varchar(255) DEFAULT NULL,
  `shell_parent_id` bigint DEFAULT NULL,
  `component` varchar(255) DEFAULT NULL,
  `component_id` varchar(255) DEFAULT NULL,
  `category` int DEFAULT NULL COMMENT '类型：0:download,1:upload',
  `storage_dir` varchar(255) DEFAULT NULL,
  `storage_dir_relative` varchar(255) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uni_component` (`shell_id`,`component`,`component_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='脚本运行时生成的文件存放在本地服务器的位置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kettle_attachment_storage`
--

LOCK TABLES `kettle_attachment_storage` WRITE;
/*!40000 ALTER TABLE `kettle_attachment_storage` DISABLE KEYS */;
/*!40000 ALTER TABLE `kettle_attachment_storage` ENABLE KEYS */;
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
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_shell_id` (`shell_id`),
  KEY `idx_shell_publish_id` (`shell_publish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kettle_running_process`
--

LOCK TABLES `kettle_running_process` WRITE;
/*!40000 ALTER TABLE `kettle_running_process` DISABLE KEYS */;
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
  `status` varchar(1) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `md5_graph` varchar(255) DEFAULT NULL COMMENT 'mxgraph图形文件md5值',
  `md5_xml` varchar(255) DEFAULT NULL COMMENT '脚本文件md5值',
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_name_parent_id` (`name`,`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  `status` varchar(1) DEFAULT NULL,
  `md5_graph` varchar(255) DEFAULT NULL COMMENT '图形文本md5值',
  `md5_xml` varchar(255) DEFAULT NULL COMMENT '脚本md5值',
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_shell_id` (`shell_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  `status` varchar(1) DEFAULT NULL,
  `begin_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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

-- Dump completed on 2025-06-24 21:11:01
