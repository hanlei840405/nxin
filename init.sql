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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限申请';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_apply`
--

LOCK TABLES `auth_apply` WRITE;
/*!40000 ALTER TABLE `auth_apply` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_privilege`
--

LOCK TABLES `auth_privilege` WRITE;
/*!40000 ALTER TABLE `auth_privilege` DISABLE KEYS */;
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (3,'ROOT',1,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (13,'首页',2,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (14,'工程管理',3,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (15,'数据源管理',4,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (16,'下载中心',5,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (17,'在线设计',6,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (18,'脚本发布',7,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (19,'批处理任务',8,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (20,'流处理任务',9,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (21,'进程管理',10,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (22,'日志跟踪',11,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (23,'模型设计',12,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (24,'统计设置',13,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (25,'布局设置',14,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (26,'用户管理',15,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (27,'集群性能',16,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (28,'权限管理',18,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (43,'报表图形',36,'RW',NULL,'1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (35,'样例基础柱状图',27,'R',NULL,'1','2025-06-16 11:39:08','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:39:08',1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (36,'样例基础柱状图',27,'RW',NULL,'1','2025-06-16 11:39:08','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:39:08',1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (68,'样例基础饼图',44,'R',NULL,'1','2025-07-07 10:45:10','jesse.18@163.com','jesse.18@163.com','2025-07-07 10:45:10',1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (69,'样例基础饼图',44,'RW',NULL,'1','2025-07-07 10:45:10','jesse.18@163.com','jesse.18@163.com','2025-07-07 10:45:10',1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (72,'样例基础折线图',46,'R',NULL,'1','2025-07-07 11:05:01','jesse.18@163.com','jesse.18@163.com','2025-07-07 11:05:01',1);
INSERT INTO `auth_privilege` (`id`,`name`,`resource_id`,`category`,`description`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (73,'样例基础折线图',46,'RW',NULL,'1','2025-07-07 11:05:01','jesse.18@163.com','jesse.18@163.com','2025-07-07 11:05:01',1);
/*!40000 ALTER TABLE `auth_privilege` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_resource`
--

LOCK TABLES `auth_resource` WRITE;
/*!40000 ALTER TABLE `auth_resource` DISABLE KEYS */;
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (1,'ROOT','ROOT','ROOT','0','1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (2,'HOME','首页','HOME','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (3,'PROJECT','工程管理','BASIC','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (4,'DATASOURCE','数据源管理','BASIC','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (5,'ATTACHMENT','下载中心','BASIC','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (6,'DESIGNER','在线设计','ETL','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (7,'PUBLISH','脚本发布','ETL','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (8,'BATCH','批处理任务','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (9,'STREAMING','流处理任务','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (10,'PROCESS','进程管理','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (11,'LOG','日志跟踪','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (12,'MODEL','模型设计','BI','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (13,'REPORT','统计设置','BI','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (14,'LAYOUT','布局设置','BI','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (15,'MEMBER','成员管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (16,'USER','用户管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (17,'METRICS','集群性能','SYSTEM','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (18,'PRIVILEGE','权限管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (27,'1','样例基础柱状图','CHART','2','1','2025-06-16 11:39:08','jesse.18@163.com','jesse.18@163.com','2025-06-16 11:39:08',1);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (36,'CHART','报表图形','BI','1','1',NULL,NULL,NULL,NULL,1);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (44,'2','样例基础饼图','CHART','2','1','2025-07-07 10:45:10','jesse.18@163.com','jesse.18@163.com','2025-07-07 10:45:10',1);
INSERT INTO `auth_resource` (`id`,`code`,`name`,`category`,`level`,`status`,`create_time`,`creator`,`modifier`,`modify_time`,`version`) VALUES (46,'3','样例基础折线图','CHART','2','1','2025-07-07 11:05:01','jesse.18@163.com','jesse.18@163.com','2025-07-07 11:05:01',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_user_privilege`
--

LOCK TABLES `auth_user_privilege` WRITE;
/*!40000 ALTER TABLE `auth_user_privilege` DISABLE KEYS */;
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (5,3,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (19,13,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (20,14,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (21,15,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (22,16,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (23,17,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (24,18,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (25,19,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (26,20,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (27,21,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (28,22,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (29,23,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (30,24,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (31,25,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (32,26,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (33,27,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (34,28,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (35,14,3);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (36,13,3);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (47,36,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (53,28,3);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (59,69,1);
INSERT INTO `auth_user_privilege` (`id`,`privilege_id`,`user_id`) VALUES (61,73,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='展示图标';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_chart`
--

LOCK TABLES `bi_chart` WRITE;
/*!40000 ALTER TABLE `bi_chart` DISABLE KEYS */;
INSERT INTO `bi_chart` VALUES (1,'demo-basic-bar','样例基础柱状图','bar','{\n  \"legend\": {},\n  \"tooltip\": {},\n  \"dataset\": {\n    \"dimensions\": [<#list dimensions as dim>\"${dim}\"<#if dim_has_next>,</#if></#list>],\n    \"source\": [<#list source as row>{<#list row?keys as key>\"${key}\": \"${row[key]}\"<#if key_has_next>,</#if></#list>}<#if row_has_next>,</#if></#list>]\n  },\n  \"xAxis\": {\"type\": \"category\"},\n  \"yAxis\": {},\n  \"series\": [<#list dimensions as dim><#if dim_index gt 0>{ \"type\": \"bar\" }<#if dim_has_next>,</#if></#if></#list>]\n}','{\n \"dimensions\": [\"product\", \"2015\", \"2016\", \"2017\"],\n \"source\": [\n      { \"product\": \"Matcha Latte\", \"2015\": 43.3, \"2016\": 85.8, \"2017\": 93.7 },\n      { \"product\": \"Milk Tea\", \"2015\": 83.1, \"2016\": 73.4, \"2017\": 55.1 },\n      { \"product\": \"Cheese Cocoa\", \"2015\": 86.4, \"2016\": 65.2, \"2017\": 82.5 },\n      { \"product\": \"Walnut Brownie\", \"2015\": 72.4, \"2016\": 53.9, \"2017\": 39.1 }\n    ]\n}','x轴为【dimensions】中第一个元素在source集合中的枚举值，y轴为【dimensions】中剩余元素在source集合中对应的数值',_binary '','2025-06-20 23:21:40','1','2025-06-15 00:00:00','jesse.18@163.com','jesse.18@163.com','2025-07-07 10:43:42',3),(2,'demo-basic-pie','样例基础饼图','pie','{\n  \"legend\": {},\n  \"tooltip\": {},\n  \"dataset\": {\n    \"dimensions\": [<#list dimensions as dim>\"${dim}\"<#if dim_has_next>,</#if></#list>],\n    \"source\": [<#list source as row>{<#list row?keys as key>\"${key}\": \"${row[key]}\"<#if key_has_next>,</#if></#list>}<#if row_has_next>,</#if></#list>]\n  },\n  \"xAxis\": {\"type\": \"category\"},\n  \"yAxis\": {},\n  \"series\": [<#list dimensions as dim><#if dim_index gt 0>{ \"type\": \"pie\" }<#if dim_has_next>,</#if></#if></#list>]\n}','{\n \"dimensions\": [\"product\", \"2015\", \"2016\", \"2017\"],\n \"source\": [\n      { \"product\": \"Matcha Latte\", \"2015\": 43.3, \"2016\": 85.8, \"2017\": 93.7 },\n      { \"product\": \"Milk Tea\", \"2015\": 83.1, \"2016\": 73.4, \"2017\": 55.1 },\n      { \"product\": \"Cheese Cocoa\", \"2015\": 86.4, \"2016\": 65.2, \"2017\": 82.5 },\n      { \"product\": \"Walnut Brownie\", \"2015\": 72.4, \"2016\": 53.9, \"2017\": 39.1 }\n    ]\n}',NULL,_binary '','2025-07-07 10:45:14','1','2025-07-07 10:45:10','jesse.18@163.com','jesse.18@163.com','2025-07-07 10:45:14',2),(3,'demo-basic-line','样例基础折线图','line','{\n  \"legend\": {},\n  \"tooltip\": {},\n  \"dataset\": {\n    \"dimensions\": [<#list dimensions as dim>\"${dim}\"<#if dim_has_next>,</#if></#list>],\n    \"source\": [<#list source as row>{<#list row?keys as key>\"${key}\": \"${row[key]}\"<#if key_has_next>,</#if></#list>}<#if row_has_next>,</#if></#list>]\n  },\n  \"xAxis\": {\"type\": \"category\"},\n  \"yAxis\": {},\n  \"series\": [<#list dimensions as dim><#if dim_index gt 0>{ \"type\": \"line\" }<#if dim_has_next>,</#if></#if></#list>]\n}','{\n \"dimensions\": [\"year\", \"Matcha Latte\", \"Milk Tea\", \"Cheese Cocoa\", \"Walnut Brownie\"],\n \"source\": [\n      { \"year\": \"2015\", \"Matcha Latte\": 43.3, \"Milk Tea\": 83.1, \"Cheese Cocoa\": 86.4, \"Walnut Brownie\": 72.4 },\n      { \"year\": \"2016\", \"Matcha Latte\": 85.8, \"Milk Tea\": 73.4, \"Cheese Cocoa\": 65.2, \"Walnut Brownie\": 53.9 },\n      { \"year\": \"2017\", \"Matcha Latte\": 93.7, \"Milk Tea\": 55.1, \"Cheese Cocoa\": 82.5, \"Walnut Brownie\": 39.1 }\n    ]\n}',NULL,_binary '','2025-07-07 11:05:05','1','2025-07-07 11:05:01','jesse.18@163.com','jesse.18@163.com','2025-07-07 11:05:05',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表图形参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_chart_params`
--

LOCK TABLES `bi_chart_params` WRITE;
/*!40000 ALTER TABLE `bi_chart_params` DISABLE KEYS */;
INSERT INTO `bi_chart_params` VALUES (1,'dimensions','array','维度集合',NULL,NULL,1,'1','2025-06-15 00:00:00',NULL,NULL,NULL,1),(2,'source','array','数据集',NULL,NULL,1,'1','2025-06-15 00:00:00',NULL,NULL,NULL,1),(3,'dimensions','array','维度集合',NULL,NULL,2,'1','2025-07-07 00:00:00',NULL,NULL,NULL,1),(4,'source','array','数据集',NULL,NULL,2,'1','2025-07-07 00:00:00',NULL,NULL,NULL,1),(5,'dimensions','array','维度集合',NULL,NULL,3,'1','2025-07-07 11:05:01','jesse.18@163.com','jesse.18@163.com','2025-07-07 11:05:01',1),(6,'source','array','数据集',NULL,NULL,3,'1','2025-07-07 11:05:01','jesse.18@163.com','jesse.18@163.com','2025-07-07 11:05:01',1);
/*!40000 ALTER TABLE `bi_chart_params` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bi_layout`
--

DROP TABLE IF EXISTS `bi_layout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bi_layout` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `authenticate` bit(1) DEFAULT NULL COMMENT '是否需要权限认证',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `idx_status_name` (`status`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表布局';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_layout`
--

LOCK TABLES `bi_layout` WRITE;
/*!40000 ALTER TABLE `bi_layout` DISABLE KEYS */;
/*!40000 ALTER TABLE `bi_layout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bi_layout_report`
--

DROP TABLE IF EXISTS `bi_layout_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bi_layout_report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `layout_id` bigint DEFAULT NULL COMMENT '布局ID',
  `report_id` bigint DEFAULT NULL COMMENT '报表ID',
  `frequency` int DEFAULT NULL COMMENT '刷新频率',
  `x` int DEFAULT NULL COMMENT '坐标X',
  `y` int DEFAULT NULL COMMENT '坐标Y',
  `w` int DEFAULT NULL COMMENT '组件宽',
  `h` int DEFAULT NULL COMMENT '组件长',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `idx_status_layout_id` (`status`,`layout_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表图形参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_layout_report`
--

LOCK TABLES `bi_layout_report` WRITE;
/*!40000 ALTER TABLE `bi_layout_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `bi_layout_report` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模型元数据';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_metadata`
--

LOCK TABLES `bi_metadata` WRITE;
/*!40000 ALTER TABLE `bi_metadata` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表设计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_report`
--

LOCK TABLES `bi_report` WRITE;
/*!40000 ALTER TABLE `bi_report` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表模型图形参数定义';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bi_report_chart_params`
--

LOCK TABLES `bi_report_chart_params` WRITE;
/*!40000 ALTER TABLE `bi_report_chart_params` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=1272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=1531 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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

-- Dump completed on 2025-07-08 15:41:09