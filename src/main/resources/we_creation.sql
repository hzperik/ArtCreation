/*
Navicat MySQL Data Transfer

Source Server         : my2
Source Server Version : 80037
Source Host           : localhost:3306
Source Database       : we_creation

Target Server Type    : MYSQL
Target Server Version : 80037
File Encoding         : 65001

Date: 2025-09-05 11:12:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cgys`
-- ----------------------------
DROP TABLE IF EXISTS `cgys`;
CREATE TABLE `cgys` (
  `cgy_id` int NOT NULL AUTO_INCREMENT,
  `cgy_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`cgy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of cgys
-- ----------------------------
INSERT INTO `cgys` VALUES ('1', '故事');
INSERT INTO `cgys` VALUES ('2', '博文');
INSERT INTO `cgys` VALUES ('3', '诗歌');
INSERT INTO `cgys` VALUES ('4', '图片');
INSERT INTO `cgys` VALUES ('5', '祝福语');

-- ----------------------------
-- Table structure for `creations`
-- ----------------------------
DROP TABLE IF EXISTS `creations`;
CREATE TABLE `creations` (
  `s_id` int NOT NULL AUTO_INCREMENT,
  `s_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `s_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `s_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `s_click` int DEFAULT NULL,
  `s_userid` int DEFAULT NULL,
  `s_cgy` int DEFAULT NULL,
  `s_keyword` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `s_createtime` datetime DEFAULT NULL,
  `s_imgfile` LONGBLOB DEFAULT NULL,
  PRIMARY KEY (`s_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of creations
-- ----------------------------

-- ----------------------------
-- Table structure for `favs`
-- ----------------------------
DROP TABLE IF EXISTS `favs`;
CREATE TABLE `favs` (
  `fav_id` int NOT NULL AUTO_INCREMENT,
  `fav_ask` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fav_answer` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fav_user_id` int DEFAULT NULL,
  `fav_createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`fav_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of favs
-- ----------------------------

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `u_id` int NOT NULL AUTO_INCREMENT,
  `u_nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `u_loginname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `u_loginpwd` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `u_score` int DEFAULT NULL,
  `u_email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
