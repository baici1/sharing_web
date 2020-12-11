/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : sharing_web

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 11/12/2020 17:15:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for m_admin
-- ----------------------------
DROP TABLE IF EXISTS `m_admin`;
CREATE TABLE `m_admin`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(0) NOT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `UK_USERNAME`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_admin
-- ----------------------------
INSERT INTO `m_admin` VALUES (1, 'admin', '1442068370@qq.com', '96e79218965eb72c92a549dd5a330112', 0, '2020-08-31 10:45:22');

-- ----------------------------
-- Table structure for m_blog
-- ----------------------------
DROP TABLE IF EXISTS `m_blog`;
CREATE TABLE `m_blog`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NOT NULL,
  `category_id` bigint(0) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `status` tinyint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_blog
-- ----------------------------
INSERT INTO `m_blog` VALUES (1, 1, 2, '生活就像海洋', '这里是摘要', '链接', '123456', '资源分享的内容：啦啦啦啦啦啦啦啦啦啦', '2020-05-21 22:08:42', 0);
INSERT INTO `m_blog` VALUES (2, 2, 2, '最值得学习', '啦啦啦啦啦啦', 'gflajglkknfd', '123456', '资源分享的内容：啦啦啦啦啦啦啦啦啦啦', '2020-05-28 09:36:38', 0);
INSERT INTO `m_blog` VALUES (3, 3, 1, '索引', 'lisalisalisa', 'mememememem', '123456', '资源分享的内容：啦啦啦啦啦啦啦啦啦啦', '2020-05-28 09:36:45', 0);
INSERT INTO `m_blog` VALUES (4, 1, 1, '没用', 'lisalisalisa', 'mememememem', '123456', '资源分享的内容：啦啦啦啦啦啦啦啦啦啦\n# 我真帅', '2020-05-28 09:36:45', 0);
INSERT INTO `m_blog` VALUES (15, 3, 1, '启明星', '启明星开发组', 'https://www.ctguqmx.com/article/', '123456', '# 启明星\n# 开发组', '2020-11-26 19:46:32', 0);

-- ----------------------------
-- Table structure for m_category
-- ----------------------------
DROP TABLE IF EXISTS `m_category`;
CREATE TABLE `m_category`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_category
-- ----------------------------
INSERT INTO `m_category` VALUES (1, 'MySQL', 0);
INSERT INTO `m_category` VALUES (2, 'Java', 0);

-- ----------------------------
-- Table structure for m_comment
-- ----------------------------
DROP TABLE IF EXISTS `m_comment`;
CREATE TABLE `m_comment`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `blog_id` bigint(0) NOT NULL,
  `user_id` bigint(0) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contents` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `status` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_comment
-- ----------------------------
INSERT INTO `m_comment` VALUES (1, 1, 3, NULL, '不错不错', '2020-08-21 10:45:22', 0);
INSERT INTO `m_comment` VALUES (2, 2, 3, NULL, '不错不错', '2020-08-22 10:45:22', 0);
INSERT INTO `m_comment` VALUES (3, 2, 1, NULL, '不错不错', '2020-08-23 10:45:22', 0);
INSERT INTO `m_comment` VALUES (5, 3, 2, NULL, '不错不错', '2020-08-25 10:45:22', 0);
INSERT INTO `m_comment` VALUES (6, 4, 1, 'yay', 'ff', '2020-11-24 22:31:05', 0);
INSERT INTO `m_comment` VALUES (7, 4, 1, 'yay', 'h', '2020-11-24 22:32:36', 0);

-- ----------------------------
-- Table structure for m_user
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(0) NOT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `last_login` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `UK_USERNAME`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_user
-- ----------------------------
INSERT INTO `m_user` VALUES (1, 'JUYI', '女', 'https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3348271096,2240890581&fm=111&gp=0.jpg', '111111111@qq.com', '96e79218965eb72c92a549dd5a330112', 0, '2020-04-20 10:44:01', NULL);
INSERT INTO `m_user` VALUES (2, 'mama', '女', 'https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3348271096,2240890581&fm=111&gp=0.jpg', '3040747630@qq.com', '96e79218965eb72c92a549dd5a330112', 0, '2020-08-20 10:44:01', NULL);
INSERT INTO `m_user` VALUES (3, 'yay', '女', 'https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3348271096,2240890581&fm=111&gp=0.jpg', '249337001@qq.com', 'e10adc3949ba59abbe56e057f20f883e', 0, '2020-08-20 10:44:01', NULL);

SET FOREIGN_KEY_CHECKS = 1;
