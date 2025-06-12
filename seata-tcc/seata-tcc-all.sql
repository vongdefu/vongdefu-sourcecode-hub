

-- MySQL dump 10.13  Distrib 8.4.5, for Linux (x86_64)
--
-- Host: localhost    Database: seata_tcc_account
-- ------------------------------------------------------
-- Server version       8.4.5

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `seata_tcc_account`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `seata_tcc_account` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `seata_tcc_account`;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用 户userId',
                           `money` bigint DEFAULT NULL COMMENT '余额，单位分',
                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `frozen` bigint DEFAULT NULL COMMENT '冻结的money',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'11',9328,'2025-06-12 09:08:51',0);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `undo_log`
--

DROP TABLE IF EXISTS `undo_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `undo_log` (
                            `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
                            `xid` varchar(100) NOT NULL COMMENT 'global transaction id',
                            `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
                            `rollback_info` longblob NOT NULL COMMENT 'rollback info',
                            `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
                            `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
                            `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
                            UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='TCC transaction mode undo table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `undo_log`
--

LOCK TABLES `undo_log` WRITE;
/*!40000 ALTER TABLE `undo_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `seata_tcc_order`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `seata_tcc_order` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `seata_tcc_order`;

--
-- Table structure for table `ordertb`
--

DROP TABLE IF EXISTS `ordertb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordertb` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `order_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '订单Id',
                           `product_id` bigint DEFAULT NULL COMMENT '商品Id',
                           `num` bigint DEFAULT NULL COMMENT '数量',
                           `user_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户唯一Id',
                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `status` int DEFAULT NULL COMMENT '订单状态 1 未付款 2 已付款 3 已完成 4 待确认 5 已删除',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordertb`
--

LOCK TABLES `ordertb` WRITE;
/*!40000 ALTER TABLE `ordertb` DISABLE KEYS */;
INSERT INTO `ordertb` VALUES (1,'1',36,10,'11','2025-06-12 09:22:52',1),(2,'1',36,10,'11','2025-06-12 09:23:10',1),(3,'72ff67b0-66c8-4f78-a311-a110ef48d067',36,10,'11','2025-06-12 09:31:41',3),(4,'c419f9b8-bb47-46ef-bc62-1650c12c9ae3',36,3,'11','2025-06-12 09:33:06',3),(5,'f774309d-096c-4d1a-b7ec-08f3c3efaebe',36,3,'11','2025-06-12 09:34:32',3);
/*!40000 ALTER TABLE `ordertb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactional_record`
--

DROP TABLE IF EXISTS `transactional_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactional_record` (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `xid` varchar(100) NOT NULL,
                                        `status` int DEFAULT NULL COMMENT '1. try 2 commit 3 cancel ',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactional_record`
--

LOCK TABLES `transactional_record` WRITE;
/*!40000 ALTER TABLE `transactional_record` DISABLE KEYS */;
INSERT INTO `transactional_record` VALUES (1,'192.168.10.105:8091:676475041254641664',3),(2,'192.168.10.105:8091:676475116819222528',3),(3,'192.168.10.105:8091:676476111481638912',3),(4,'192.168.10.105:8091:676477185533841408',3),(5,'192.168.10.105:8091:676477601533300736',3),(6,'192.168.10.105:8091:676478287754989568',3),(7,'192.168.10.105:8091:676478296600776704',3),(8,'192.168.10.105:8091:676479010890747904',3),(9,'192.168.10.105:8091:676479828062801920',3);
/*!40000 ALTER TABLE `transactional_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `undo_log`
--

DROP TABLE IF EXISTS `undo_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `undo_log` (
                            `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
                            `xid` varchar(100) NOT NULL COMMENT 'global transaction id',
                            `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
                            `rollback_info` longblob NOT NULL COMMENT 'rollback info',
                            `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
                            `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
                            `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
                            UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='TCC transaction mode undo table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `undo_log`
--

LOCK TABLES `undo_log` WRITE;
/*!40000 ALTER TABLE `undo_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `seata_tcc_storage`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `seata_tcc_storage` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `seata_tcc_storage`;

--
-- Table structure for table `storage`
--

DROP TABLE IF EXISTS `storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `product_id` bigint DEFAULT NULL,
                           `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `num` bigint DEFAULT NULL COMMENT '数量',
                           `create_time` datetime DEFAULT NULL,
                           `price` bigint DEFAULT NULL COMMENT '单价，单位分',
                           `frozen` bigint DEFAULT NULL COMMENT '冻结的库存',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage`
--

LOCK TABLES `storage` WRITE;
/*!40000 ALTER TABLE `storage` DISABLE KEYS */;
INSERT INTO `storage` VALUES (1,36,'苹果手机',9944,NULL,12,0);
/*!40000 ALTER TABLE `storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `undo_log`
--

DROP TABLE IF EXISTS `undo_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `undo_log` (
                            `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
                            `xid` varchar(100) NOT NULL COMMENT 'global transaction id',
                            `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
                            `rollback_info` longblob NOT NULL COMMENT 'rollback info',
                            `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
                            `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
                            `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
                            UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='TCC transaction mode undo table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `undo_log`
--

LOCK TABLES `undo_log` WRITE;
/*!40000 ALTER TABLE `undo_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-12 17:36:44
