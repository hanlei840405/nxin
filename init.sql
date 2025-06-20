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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_privilege`
--

LOCK TABLES `auth_privilege` WRITE;
/*!40000 ALTER TABLE `auth_privilege` DISABLE KEYS */;
INSERT INTO `auth_privilege` VALUES (3,'ROOT',1,'RW','1',NULL,NULL,NULL,NULL,1),(13,'首页',2,'RW','1',NULL,NULL,NULL,NULL,1),(14,'工程管理',3,'RW','1',NULL,NULL,NULL,NULL,1),(15,'数据源管理',4,'RW','1',NULL,NULL,NULL,NULL,1),(16,'下载中心',5,'RW','1',NULL,NULL,NULL,NULL,1),(17,'在线设计',6,'RW','1',NULL,NULL,NULL,NULL,1),(18,'脚本发布',7,'RW','1',NULL,NULL,NULL,NULL,1),(19,'批处理任务',8,'RW','1',NULL,NULL,NULL,NULL,1),(20,'流处理任务',9,'RW','1',NULL,NULL,NULL,NULL,1),(21,'进程管理',10,'RW','1',NULL,NULL,NULL,NULL,1),(22,'日志跟踪',11,'RW','1',NULL,NULL,NULL,NULL,1),(23,'模型设计',12,'RW','1',NULL,NULL,NULL,NULL,1),(24,'统计设置',13,'RW','1',NULL,NULL,NULL,NULL,1),(25,'布局设置',14,'RW','1',NULL,NULL,NULL,NULL,1),(26,'用户管理',15,'RW','1',NULL,NULL,NULL,NULL,1),(27,'集群性能',16,'RW','1',NULL,NULL,NULL,NULL,1),(28,'权限管理',17,'RW','1',NULL,NULL,NULL,NULL,1);
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
  `level` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_resource`
--

LOCK TABLES `auth_resource` WRITE;
/*!40000 ALTER TABLE `auth_resource` DISABLE KEYS */;
INSERT INTO `auth_resource` VALUES (1,'ROOT','ROOT','ROOT','0','1',NULL,NULL,NULL,NULL,1),(2,'HOME','首页','HOME','1','1',NULL,NULL,NULL,NULL,0),(3,'PROJECT','工程管理','BASIC','1','1',NULL,NULL,NULL,NULL,0),(4,'DATASOURCE','数据源管理','BASIC','1','1',NULL,NULL,NULL,NULL,0),(5,'ATTACHMENT','下载中心','BASIC','1','1',NULL,NULL,NULL,NULL,0),(6,'DESIGNER','在线设计','ETL','1','1',NULL,NULL,NULL,NULL,0),(7,'PUBLISH','脚本发布','ETL','1','1',NULL,NULL,NULL,NULL,0),(8,'BATCH','批处理任务','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(9,'STREAMING','流处理任务','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(10,'PROCESS','进程管理','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(11,'LOG','日志跟踪','SCHEDULE','1','1',NULL,NULL,NULL,NULL,0),(12,'MODEL','模型设计','BI','1','1',NULL,NULL,NULL,NULL,0),(15,'MEMBER','成员管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(16,'USER','用户管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(17,'METRICS','集群性能','SYSTEM','1','1',NULL,NULL,NULL,NULL,0),(18,'PRIVILEGE','权限管理','SYSTEM','1','1',NULL,NULL,NULL,NULL,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_user_privilege`
--

LOCK TABLES `auth_user_privilege` WRITE;
/*!40000 ALTER TABLE `auth_user_privilege` DISABLE KEYS */;
INSERT INTO `auth_user_privilege` VALUES (5,3,1),(16,9,2),(19,13,1),(20,14,1),(21,15,1),(22,16,1),(23,17,1),(24,18,1),(25,19,1),(26,20,1),(27,21,1),(28,22,1),(29,23,1),(30,24,1),(31,25,1),(32,26,1),(33,27,1),(34,28,1),(35,14,3),(36,13,3),(37,9,1),(38,29,2),(39,8,2),(42,29,3),(43,30,3),(44,32,1),(45,31,2);
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
  `status` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_dictionary`
--

LOCK TABLES `basic_dictionary` WRITE;
/*!40000 ALTER TABLE `basic_dictionary` DISABLE KEYS */;
INSERT INTO `basic_dictionary` VALUES (1,'DATASOURCE','数据源',NULL,'1','2025-06-03 13:15:33','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:18:29',2),(2,'FTP','FTP服务器类型',NULL,'1','2025-06-03 13:37:58','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:37:58',1),(3,'FTP-PROXY','FTP代理类型',NULL,'1','2025-06-04 12:17:40','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:17:40',1),(5,'MYSQL-COLUMN-CATEGORY','mysql字段类型',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(6,'ORACLE-COLUMN-CATEGORY','oracle字段类型',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(7, 'REPORT-CHART-CATEGORY', '报表图形类别', '为BI分析-报表图形模块提供图形类型约束', '1', '2025-06-10 11:26:42', 'jesse.18@163.com', 'jesse.18@163.com', '2025-06-10 11:36:24', 2);
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
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典项';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_dictionary_item`
--

LOCK TABLES `basic_dictionary_item` WRITE;
/*!40000 ALTER TABLE `basic_dictionary_item` DISABLE KEYS */;
INSERT INTO `basic_dictionary_item` VALUES (4,1,'mysql','MYSQL',NULL,'1','2025-06-03 13:18:29','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:18:29',1),(5,1,'postgresql','POSTGRESQL',NULL,'1','2025-06-03 13:18:29','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:18:29',1),(6,1,'oracle','ORACLE',NULL,'1','2025-06-03 13:18:29','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:18:29',1),(7,2,'FTP','FTP',NULL,'1','2025-06-03 13:37:58','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:37:58',1),(8,2,'SFTP','SFTP',NULL,'1','2025-06-03 13:37:58','jesse.18@163.com','jesse.18@163.com','2025-06-03 13:37:58',1),(9,3,'HTTP','HTTP',NULL,'1','2025-06-04 12:17:40','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:17:40',1),(10,3,'SOCKS5','SOCKS5',NULL,'1','2025-06-04 12:17:40','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:17:40',1),(13,5,'int','int',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(14,5,'bigint','bigint',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(15,5,'varchar','varchar',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(16,5,'date','date',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(17,5,'datetime','datetime',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(18,5,'time','time',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(19,5,'decimal','decimal',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(20,5,'float','float',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(21,5,'double','double',NULL,'1','2025-06-04 12:37:57','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:37:57',1),(22,6,'varchar2','varchar2',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(23,6,'number','number',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(24,6,'integer','integer',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(25,6,'long','long',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(26,6,'float','float',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(27,6,'date','date',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(28,6,'timestamp','timestamp',NULL,'1','2025-06-04 12:39:03','jesse.18@163.com','jesse.18@163.com','2025-06-04 12:39:03',1),(33, 7, '柱状图', 'bar', '基本柱状图/堆叠柱状图/动态排序柱状图/阶梯瀑布图', '1', '2025-06-10 11:36:27', 'jesse.18@163.com', 'jesse.18@163.com', '2025-06-10 11:36:27', 1),(34, 7, '饼图', 'pie', '基础饼图/圆环图/南丁格尔图（玫瑰图）', '1', '2025-06-10 11:36:27', 'jesse.18@163.com', 'jesse.18@163.com', '2025-06-10 11:36:27', 1),(35, 7, '折线图', 'line', '基础折线图/堆叠折线图/区域面积图/平滑曲线图/阶梯线图', '1', '2025-06-10 11:36:27', 'jesse.18@163.com', 'jesse.18@163.com', '2025-06-10 11:36:27', 1),(36, 7, '散点图', 'scatter', '基础散点图', '1', '2025-06-10 11:36:27', 'jesse.18@163.com', 'jesse.18@163.com', '2025-06-10 11:36:27', 1);
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
  `status` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='FTP服务器信息';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模型元数据';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `data` text DEFAULT NULL COMMENT '样例数据',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='展示图标';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表设计';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='BI模型';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `status` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uni_component` (`shell_id`,`component`,`component_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='脚本运行时生成的文件存放在本地服务器的位置';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=2035 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=265 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

CREATE TABLE `bi_report_chart_params` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `report_id` bigint DEFAULT NULL COMMENT '报表ID',
  `chart_params_id` bigint DEFAULT NULL COMMENT '参数ID',
  `datasource_id` bigint DEFAULT NULL COMMENT '数据源ID',
  `category` varchar(255) DEFAULT NULL COMMENT '类型',
  `script` text COMMENT '脚本',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报表模型图形参数定义';

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-04 12:48:54
