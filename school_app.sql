/*
 Navicat Premium Dump SQL

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80045 (8.0.45)
 Source Host           : localhost:3306
 Source Schema         : school_app

 Target Server Type    : MySQL
 Target Server Version : 80045 (8.0.45)
 File Encoding         : 65001

 Date: 03/04/2026 20:39:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评论唯一标识ID',
  `lostfound_id` int NOT NULL COMMENT '关联的失物招领信息ID',
  `user_id` int NOT NULL COMMENT '评论用户的ID',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论用户的昵称',
  `photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论用户的头像URL',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `state` int NULL DEFAULT 0 COMMENT '评论状态：0-正常，1-删除',
  `parent_id` int NULL DEFAULT 0 COMMENT '父评论ID',
  `reply_user_id` int NULL DEFAULT NULL COMMENT '被回复用户的ID',
  `reply_nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '被回复用户的昵称',
  `reject_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '驳回原因',
  `create_time` datetime NULL DEFAULT NULL COMMENT '评论创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exchange_order
-- ----------------------------
DROP TABLE IF EXISTS `exchange_order`;
CREATE TABLE `exchange_order`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `user_id` int NOT NULL COMMENT '用户ID',
  `item_id` int NOT NULL COMMENT '商品ID',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `item_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片路径',
  `points_cost` int NOT NULL DEFAULT 0 COMMENT '消耗积分',
  `verify_code` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '8位提货核验码',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-待核验, 1-已核验, 2-已取消',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `verify_time` datetime NULL DEFAULT NULL COMMENT '核验时间',
  `verify_admin_id` int NULL DEFAULT NULL COMMENT '核验管理员ID',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_verify_code`(`verify_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分兑换订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lostfound
-- ----------------------------
DROP TABLE IF EXISTS `lostfound`;
CREATE TABLE `lostfound`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '失物招领信息唯一标识ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '失物招领标题',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失物招领图片URL',
  `pub_date` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '失物招领详细描述',
  `place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '丢失/拾到地点',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态：0-未解决，1-已解决',
  `stick` int NULL DEFAULT 0 COMMENT '是否置顶：0-不置顶，1-置顶',
  `lostfoundtype_id` int NULL DEFAULT NULL COMMENT '失物招领类型ID',
  `user_id` int NULL DEFAULT NULL COMMENT '发布用户ID',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布用户昵称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失物招领类型名称',
  `longitude` double NULL DEFAULT NULL COMMENT '纬度',
  `latitude` double NULL DEFAULT NULL COMMENT '经度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '失物招领信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lostfound_type
-- ----------------------------
DROP TABLE IF EXISTS `lostfound_type`;
CREATE TABLE `lostfound_type`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '类型唯一标识ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '失物招领类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '留言ID',
  `user_id` int NOT NULL COMMENT '留言用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '留言内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '留言创建时间',
  `state` int NULL DEFAULT 0 COMMENT '留言状态',
  `parent_id` int NULL DEFAULT 0 COMMENT '父留言ID',
  `reply_user_id` int NULL DEFAULT NULL COMMENT '被回复用户ID',
  `reject_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '驳回原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '留言信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for point_history
-- ----------------------------
DROP TABLE IF EXISTS `point_history`;
CREATE TABLE `point_history`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` int NOT NULL COMMENT '关联用户ID',
  `type` int NOT NULL COMMENT '变动类型：1-发布奖励, 2-退还积分, 3-兑换消耗, 4-系统扣除',
  `points_changed` int NOT NULL COMMENT '变动数值(正负)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变动描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户积分变动历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sensitive_word
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_word`;
CREATE TABLE `sensitive_word`  (
  `word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shop_item
-- ----------------------------
DROP TABLE IF EXISTS `shop_item`;
CREATE TABLE `shop_item`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品描述',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片URL',
  `required_points` int NOT NULL COMMENT '兑换所需积分',
  `stock` int NOT NULL DEFAULT 0 COMMENT '商品库存',
  `status` int NULL DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分商城商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识ID',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户手机号码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录密码',
  `photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像URL',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户性别',
  `points` int NULL DEFAULT 0 COMMENT '用户当前积分余额',
  `reg_date` datetime NULL DEFAULT NULL COMMENT '用户注册时间',
  `state` int NULL DEFAULT 0 COMMENT '用户账户状态：0-正常，1-禁用',
  `role` int NULL DEFAULT 0 COMMENT '用户角色权限：0-普通用户，1-管理员',
  `im_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '即时通讯token',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
