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

 Date: 04/04/2026 14:55:37
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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_comment_lostfound`(`lostfound_id` ASC) USING BTREE,
  INDEX `fk_comment_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_comment_parent`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_lostfound` FOREIGN KEY (`lostfound_id`) REFERENCES `lostfound` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_parent` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------

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
  INDEX `idx_verify_code`(`verify_code` ASC) USING BTREE,
  INDEX `fk_order_item`(`item_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_item` FOREIGN KEY (`item_id`) REFERENCES `shop_item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分兑换订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exchange_order
-- ----------------------------

-- ----------------------------
-- Table structure for lostfound
-- ----------------------------
DROP TABLE IF EXISTS `lostfound`;
CREATE TABLE `lostfound`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '失物招领信息唯一标识ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '失物招领标题',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失物招领图片URL',
  `pub_date` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '发布时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '失物招领详细描述',
  `place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '丢失/拾到地点',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态：待审核 寻找中 已找到 待认领 已认领 已驳回',
  `stick` int NULL DEFAULT 0 COMMENT '是否置顶：0-不置顶，1-置顶',
  `lostfoundtype_id` int NULL DEFAULT NULL COMMENT '失物招领类型ID',
  `user_id` int NULL DEFAULT NULL COMMENT '发布用户ID',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布用户昵称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失物招领类型名称',
  `longitude` double NULL DEFAULT NULL COMMENT '纬度',
  `latitude` double NULL DEFAULT NULL COMMENT '经度',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lostfound_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_lostfound_type`(`lostfoundtype_id` ASC) USING BTREE,
  CONSTRAINT `fk_lostfound_type` FOREIGN KEY (`lostfoundtype_id`) REFERENCES `lostfound_type` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_lostfound_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '失物招领信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lostfound
-- ----------------------------
INSERT INTO `lostfound` VALUES (1, '丢失水杯', '94fc4dfb-d8e2-47aa-9f94-248cb9940ea2.jpg', '2026-04-04 14:34:53', '在西南科技大学(青义校区西区图书馆) 丢失了水杯，，联系电话：18682675515', '西南科技大学(青义校区西区图书馆)', '18682675515', '寻找中', 0, 4, 1, 'admin', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (2, '丢失水杯', '6e4365ed-13d8-4d23-ad0f-058e83d8bf05.jpg', '2026-04-04 14:34:53', '在西南科技大学-7食堂 丢失了水杯，，联系电话：17673297533', '西南科技大学-7食堂', '17673297533', '寻找中', 0, 4, 5, '阿宝', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (3, '丢失水杯', '4facd3e3-b938-4fb0-b83b-04d4993acea9.jpg', '2026-04-04 14:34:53', '在西南科技大学(青义校区东1教学楼) 丢失了水杯，，联系电话：17673297533', '西南科技大学(青义校区东1教学楼)', '17673297533', '寻找中', 0, 4, 5, '阿宝', '失物', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (4, '丢失手机', '68ea307e-1d55-4603-8a82-5c0444dfa9c6.jpg', '2026-04-04 14:33:28', '在西南科技大学东区-体育场 丢失了手机，，联系电话：14769903669', '西南科技大学东区-体育场', '14769903669', '寻找中', 0, 1, 3, '小豆豆', '失物', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (5, '丢失手机', '6f8251b0-ba5a-4b96-9a3f-822ba2340c72.jpg', '2026-04-04 14:33:28', '在西南科技大学(青义校区)-西7教学楼 丢失了手机，，联系电话：14769903669', '西南科技大学(青义校区)-西7教学楼', '14769903669', '寻找中', 0, 1, 3, '小豆豆', '失物', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (6, '丢失手机', '617d2bfd-27e8-4714-a10c-00692fd18ab3.jpg', '2026-04-04 14:33:28', '在西南科技大学(青义校区西区图书馆) 丢失了手机，，联系电话：18682675515', '西南科技大学(青义校区西区图书馆)', '18682675515', '寻找中', 0, 1, 1, 'admin', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (7, '丢失黑色签字笔', '315e3eb8-e623-47ea-beee-2e4d52be76f4.jpg', '2026-04-04 14:34:53', '在西南科技大学-7食堂 丢失了黑色签字笔，，联系电话：14769903669', '西南科技大学-7食堂', '14769903669', '寻找中', 0, 4, 3, '小豆豆', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (8, '丢失黑色签字笔', 'b340f516-ed05-4225-be15-d7ebeccc8690.jpg', '2026-04-04 14:34:53', '在西南科技大学(青义校区东1教学楼) 丢失了黑色签字笔，，联系电话：17788266423', '西南科技大学(青义校区东1教学楼)', '17788266423', '寻找中', 0, 4, 4, '小米', '失物', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (9, '丢失黑色签字笔', 'efac7436-34a1-4107-abc7-d17f69bcf2a2.jpg', '2026-04-04 14:34:53', '在西南科技大学东区-体育场 丢失了黑色签字笔，，联系电话：15181544770', '西南科技大学东区-体育场', '15181544770', '寻找中', 0, 4, 2, '小卷毛', '失物', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (10, '丢失耳机', 'e3c82710-9324-4eb5-8cb8-aee29d2abee9.jpg', '2026-04-04 14:33:28', '在西南科技大学(青义校区)-西7教学楼 丢失了耳机，，联系电话：15181544770', '西南科技大学(青义校区)-西7教学楼', '15181544770', '寻找中', 0, 1, 2, '小卷毛', '失物', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (11, '丢失耳机', '726c7791-73dc-4335-baa3-d28a109c2533.jpg', '2026-04-04 14:33:28', '在西南科技大学(青义校区西区图书馆) 丢失了耳机，，联系电话：15181544770', '西南科技大学(青义校区西区图书馆)', '15181544770', '寻找中', 0, 1, 2, '小卷毛', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (12, '丢失耳机', '4973a9bd-cb61-405d-8bf4-4db3df96dd88.jpg', '2026-04-04 14:33:28', '在西南科技大学-7食堂 丢失了耳机，，联系电话：14769903669', '西南科技大学-7食堂', '14769903669', '寻找中', 0, 1, 3, '小豆豆', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (13, '找到水杯', 'e623470d-4264-4936-8f7d-cd0a46ec588f.jpg', '2026-04-04 14:34:53', '在西南科技大学(青义校区东1教学楼) 找到了水杯，，联系电话：18682675515', '西南科技大学(青义校区东1教学楼)', '18682675515', '待认领', 0, 4, 1, 'admin', '招领', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (14, '找到水杯', '5634ae0a-ff5c-401e-a732-c5fff9899ab5.jpg', '2026-04-04 14:34:53', '在西南科技大学东区-体育场 找到了水杯，，联系电话：17673297533', '西南科技大学东区-体育场', '17673297533', '待认领', 0, 4, 5, '阿宝', '招领', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (15, '找到水杯', 'a2491010-97d2-44f3-a405-5c9918bc8195.jpg', '2026-04-04 14:34:53', '在西南科技大学(青义校区)-西7教学楼 找到了水杯，，联系电话：15181544770', '西南科技大学(青义校区)-西7教学楼', '15181544770', '待认领', 0, 4, 2, '小卷毛', '招领', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (16, '找到手机', 'f04171e2-43c9-4cda-9ef8-3c39ffce48e0.jpg', '2026-04-04 14:33:28', '在西南科技大学(青义校区西区图书馆) 找到了手机，，联系电话：15181544770', '西南科技大学(青义校区西区图书馆)', '15181544770', '待认领', 0, 1, 2, '小卷毛', '招领', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (17, '找到手机', '309170f6-8329-496d-a3a7-423b5fefa3c0.jpg', '2026-04-04 14:33:28', '在西南科技大学-7食堂 找到了手机，，联系电话：17673297533', '西南科技大学-7食堂', '17673297533', '待认领', 0, 1, 5, '阿宝', '招领', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (18, '找到手机', '0cea2d56-d70e-4d9f-b76d-160585ede90d.jpg', '2026-04-04 14:33:28', '在西南科技大学(青义校区东1教学楼) 找到了手机，，联系电话：14769903669', '西南科技大学(青义校区东1教学楼)', '14769903669', '待认领', 0, 1, 3, '小豆豆', '招领', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (19, '找到黑色签字笔', '7b3875e3-284c-4f45-bbf9-09dddd70d28f.jpg', '2026-04-04 14:34:53', '在西南科技大学东区-体育场 找到了黑色签字笔，，联系电话：15181544770', '西南科技大学东区-体育场', '15181544770', '待认领', 0, 4, 2, '小卷毛', '招领', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (20, '找到黑色签字笔', '1103b4e2-a472-488e-b27b-5fb1c8ae51f8.jpg', '2026-04-04 14:34:53', '在西南科技大学(青义校区)-西7教学楼 找到了黑色签字笔，，联系电话：14769903669', '西南科技大学(青义校区)-西7教学楼', '14769903669', '待认领', 0, 4, 3, '小豆豆', '招领', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (21, '找到黑色签字笔', '91768109-5c2c-4454-8a67-8ffa1dac99fc.jpg', '2026-04-04 14:34:53', '在西南科技大学(青义校区西区图书馆) 找到了黑色签字笔，，联系电话：17788266423', '西南科技大学(青义校区西区图书馆)', '17788266423', '待认领', 0, 4, 4, '小米', '招领', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (22, '找到耳机', 'dd7ccca2-db95-4c4d-9c5f-67c75d8b1633.jpg', '2026-04-04 14:33:28', '在西南科技大学-7食堂 找到了耳机，，联系电话：17673297533', '西南科技大学-7食堂', '17673297533', '待认领', 0, 1, 5, '阿宝', '招领', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (23, '找到耳机', '9019f424-e8e1-44c2-abf8-e5dcda3db6b2.jpg', '2026-04-04 14:33:28', '在西南科技大学(青义校区东1教学楼) 找到了耳机，，联系电话：18682675515', '西南科技大学(青义校区东1教学楼)', '18682675515', '待认领', 0, 1, 1, 'admin', '招领', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (24, '找到耳机', '6c20f59e-e12c-474b-bebb-d96464455d4c.jpg', '2026-04-04 14:33:28', '在西南科技大学东区-体育场 找到了耳机，，联系电话：14769903669', '西南科技大学东区-体育场', '14769903669', '待认领', 0, 1, 3, '小豆豆', '招领', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (25, '丢失手机', '2e3e4122-8cb8-408e-b010-cc257063cdcc.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区)-西7教学楼 丢失了手机，联系电话：14769903669', '西南科技大学(青义校区)-西7教学楼', '14769903669', '寻找中', 0, 1, 3, '小豆豆', '失物', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (26, '找到手机', '26402124-0f11-4e97-8d3b-6e0f75335e07.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区东1教学楼) 找到了手机，联系电话：14769903669', '西南科技大学(青义校区东1教学楼)', '14769903669', '待认领', 0, 1, 3, '小豆豆', '招领', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (27, '丢失手机', 'a8fc4532-3255-4848-b59e-d5fc231b1fcf.jpg', '2026-04-04 14:50:30', '在 西南科技大学-7食堂 丢失了手机，联系电话：17673297533', '西南科技大学-7食堂', '17673297533', '寻找中', 0, 1, 5, '阿宝', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (28, '找到耳机', 'cb8d54e6-9ce4-42ab-85b1-91f78ea9c387.jpg', '2026-04-04 14:50:30', '在 西南科技大学-7食堂 找到了耳机，联系电话：15181544770', '西南科技大学-7食堂', '15181544770', '待认领', 0, 1, 2, '小卷毛', '招领', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (29, '丢失耳机', '2e1ddc2d-49d4-4961-ab0f-647001af288e.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区东1教学楼) 丢失了耳机，联系电话：18682675515', '西南科技大学(青义校区东1教学楼)', '18682675515', '寻找中', 0, 1, 1, 'admin', '失物', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (30, '找到耳机', '5fc0b4e6-991f-4f00-a28b-f5ff74eda337.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区西区图书馆) 找到了耳机，联系电话：14769903669', '西南科技大学(青义校区西区图书馆)', '14769903669', '待认领', 0, 1, 3, '小豆豆', '招领', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (31, '找到身份证', 'ecb95e8b-1f81-4cc9-aebe-3bf06d60528c.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区西区图书馆) 找到了身份证，联系电话：15181544770', '西南科技大学(青义校区西区图书馆)', '15181544770', '待认领', 0, 2, 2, '小卷毛', '招领', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (32, '丢失身份证', '0f649e2d-20c8-4815-93d5-97d951f086e0.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区东1教学楼) 丢失了身份证，联系电话：17788266423', '西南科技大学(青义校区东1教学楼)', '17788266423', '寻找中', 0, 2, 4, '小米', '失物', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (33, '丢失身份证', 'd9258ef3-32fb-4e4b-b5c0-c6327d4cb161.jpg', '2026-04-04 14:50:30', '在 西南科技大学-7食堂 丢失了身份证，联系电话：17673297533', '西南科技大学-7食堂', '17673297533', '寻找中', 0, 2, 5, '阿宝', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (34, '丢失校园卡', '66f8986f-a892-4814-9f72-a1f70f3f23b1.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区西区图书馆) 丢失了校园卡，联系电话：14769903669', '西南科技大学(青义校区西区图书馆)', '14769903669', '寻找中', 0, 2, 3, '小豆豆', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (35, '丢失校园卡', '54410a92-84de-4ac9-be98-046d39304b39.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区西区图书馆) 丢失了校园卡，联系电话：14769903669', '西南科技大学(青义校区西区图书馆)', '14769903669', '寻找中', 0, 2, 3, '小豆豆', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (36, '找到高等数学', 'ad78e80e-0b4c-4d63-9dc0-50ce74840bd3.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区西区图书馆) 找到了高等数学，联系电话：17788266423', '西南科技大学(青义校区西区图书馆)', '17788266423', '待认领', 0, 3, 4, '小米', '招领', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (37, '丢失高等数学', '25f46892-c3d5-45b2-a328-3643896713d9.jpg', '2026-04-04 14:50:30', '在 西南科技大学-7食堂 丢失了高等数学，联系电话：15181544770', '西南科技大学-7食堂', '15181544770', '寻找中', 0, 3, 2, '小卷毛', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (38, '丢失高等数学', 'b61e9a94-18d0-4a03-b1c4-ed24567567e9.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区西区图书馆) 丢失了高等数学，联系电话：14769903669', '西南科技大学(青义校区西区图书馆)', '14769903669', '寻找中', 0, 3, 3, '小豆豆', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (39, '找到英语词典', '50c9595e-2838-4e80-91db-0cb85b4cc2dc.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区)-西7教学楼 找到了英语词典，联系电话：18682675515', '西南科技大学(青义校区)-西7教学楼', '18682675515', '待认领', 0, 3, 1, 'admin', '招领', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (40, '找到英语词典', '804fc92b-62ed-415a-ace3-e9a46be50680.jpg', '2026-04-04 14:50:30', '在 西南科技大学-7食堂 找到了英语词典，联系电话：17788266423', '西南科技大学-7食堂', '17788266423', '待认领', 0, 3, 4, '小米', '招领', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (41, '找到水杯', '001ab033-7396-4a9c-a867-9d3f31af5a0f.jpg', '2026-04-04 14:50:30', '在 西南科技大学-7食堂 找到了水杯，联系电话：14769903669', '西南科技大学-7食堂', '14769903669', '待认领', 0, 4, 3, '小豆豆', '招领', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (42, '丢失水杯', '17f0e1af-fc4e-4214-91c9-a7f0b97a85a1.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区西区图书馆) 丢失了水杯，联系电话：14769903669', '西南科技大学(青义校区西区图书馆)', '14769903669', '寻找中', 0, 4, 3, '小豆豆', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (43, '丢失水杯', 'ff04f9d4-456f-4d90-96f5-a579be810587.jpg', '2026-04-04 14:50:30', '在 西南科技大学东区-体育场 丢失了水杯，联系电话：17673297533', '西南科技大学东区-体育场', '17673297533', '寻找中', 0, 4, 5, '阿宝', '失物', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (44, '丢失黑色签字笔', 'f4d50b03-009c-4eae-a06b-f609f33c92c0.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区东1教学楼) 丢失了黑色签字笔，联系电话：14769903669', '西南科技大学(青义校区东1教学楼)', '14769903669', '寻找中', 0, 4, 3, '小豆豆', '失物', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (45, '丢失黑色签字笔', '84d05028-430b-43d0-8e60-f0eb4908aef0.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区东1教学楼) 丢失了黑色签字笔，联系电话：18682675515', '西南科技大学(青义校区东1教学楼)', '18682675515', '寻找中', 0, 4, 1, 'admin', '失物', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (46, '找到黑色签字笔', 'c8c94d77-5828-498b-bd6e-39b2486ac448.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区)-西7教学楼 找到了黑色签字笔，联系电话：17788266423', '西南科技大学(青义校区)-西7教学楼', '17788266423', '待认领', 0, 4, 4, '小米', '招领', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (47, '找到钥匙', 'c1cabf62-41e6-494e-8460-d4af34639c99.jpg', '2026-04-04 14:50:30', '在 西南科技大学-7食堂 找到了钥匙，联系电话：15181544770', '西南科技大学-7食堂', '15181544770', '待认领', 0, 5, 2, '小卷毛', '招领', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (48, '丢失钥匙', 'e0f47934-4b0a-47dd-b05f-71b331e1ca8d.jpg', '2026-04-04 14:50:30', '在 西南科技大学-7食堂 丢失了钥匙，联系电话：18682675515', '西南科技大学-7食堂', '18682675515', '寻找中', 0, 5, 1, 'admin', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (49, '找到钥匙', 'de69822f-602f-48c1-93c3-486c261d590e.jpg', '2026-04-04 14:50:30', '在 西南科技大学-7食堂 找到了钥匙，联系电话：15181544770', '西南科技大学-7食堂', '15181544770', '待认领', 0, 5, 2, '小卷毛', '招领', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (50, '丢失雨伞', 'e1764a01-790c-4a00-9491-6eb136efa35a.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区西区图书馆) 丢失了雨伞，联系电话：15181544770', '西南科技大学(青义校区西区图书馆)', '15181544770', '寻找中', 0, 5, 2, '小卷毛', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (51, '找到雨伞', '41b15283-ff87-4e9b-ab4b-f1c5d3e20f81.jpg', '2026-04-04 14:50:30', '在 西南科技大学(青义校区)-西7教学楼 找到了雨伞，联系电话：14769903669', '西南科技大学(青义校区)-西7教学楼', '14769903669', '待认领', 0, 5, 3, '小豆豆', '招领', 104.693323, 31.543984);

-- ----------------------------
-- Table structure for lostfound_type
-- ----------------------------
DROP TABLE IF EXISTS `lostfound_type`;
CREATE TABLE `lostfound_type`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '类型唯一标识ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '失物招领类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lostfound_type
-- ----------------------------
INSERT INTO `lostfound_type` VALUES (1, '电子产品');
INSERT INTO `lostfound_type` VALUES (2, '证件');
INSERT INTO `lostfound_type` VALUES (3, '书籍');
INSERT INTO `lostfound_type` VALUES (4, '日用品');
INSERT INTO `lostfound_type` VALUES (5, '其他');

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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_message_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_message_parent`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `fk_message_parent` FOREIGN KEY (`parent_id`) REFERENCES `message` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_message_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '留言信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------

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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_history_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_history_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户积分变动历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of point_history
-- ----------------------------

-- ----------------------------
-- Table structure for sensitive_word
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_word`;
CREATE TABLE `sensitive_word`  (
  `word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sensitive_word
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分商城商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_item
-- ----------------------------
INSERT INTO `shop_item` VALUES (1, '文具套装', NULL, '0dfa7974-366c-41a4-ab67-ea665253a871.jpg', 500, 20, 1, NULL, NULL);
INSERT INTO `shop_item` VALUES (2, '小词典', NULL, '03e4ca48-a041-47a4-98a3-97c294b01e55.jpg', 50, 10, 1, NULL, NULL);
INSERT INTO `shop_item` VALUES (3, '优惠券', NULL, 'af187150-4cff-4411-9c3e-a048d61c7380.jpg', 50, 100, 1, NULL, NULL);
INSERT INTO `shop_item` VALUES (4, '高等数学第7版上下册同济大学', NULL, '8acfd498-b16a-4f51-b143-64e4aa840156.jpg', 500, 10, 1, NULL, NULL);
INSERT INTO `shop_item` VALUES (5, '水杯', NULL, '36aab817-d58e-48dc-9565-c7d8c559f178.jpg', 1000, 20, 1, NULL, NULL);

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

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '18682675515', '3502777299@qq.com', '$2a$12$Ikp/n4voUd3RAiwF9YFZr.hHM3j60HYtE37TMqGIquuaqYCyTo3YG', '957644e3-f740-4fd9-8a2d-cebf0c48b782.jpg', '女', 100, '2026-04-03 15:49:11', 1, 1, 'j3iWf2UVvHvWe8Gagt9eVjklvUvzCtzn@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (2, '小卷毛', '15181544770', '智宸.陆@yahoo.com', '$2a$12$Lc.uwEMhuHykVt0Jhu504eM4dV.Kuwx2JHrH5ucePk0g1hMnzOyQO', '3bb432c6-4e75-49b3-bba2-3e94984b8074.jpg', '男', 100, '2026-04-03 15:49:15', 1, 0, 'REsGtneW7IrWe8Gagt9eVpNwbQaxjfOS@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (3, '小豆豆', '14769903669', '炫明.幸@yahoo.com', '$2a$12$r38YVOyy8FUGzoAJPsS.NOkojgEo6vF5LN3mI9zzXtkyu4AU4vHMm', '855f056a-e4ab-4946-9bdd-00af77ffcce7.jpg', '男', 100, '2026-04-03 15:49:18', 1, 0, 'uEv3PrRXazPWe8Gagt9eVnWIsQdsHTPv@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (4, '小米', '17788266423', '雨泽.康@gmail.com', '$2a$12$Ehcvkxw5rxa9ZLGbIcfrvOsTMqUejdD7xvGm6tgVFM771u1lpU/uS', 'ea8419f7-a7ca-47c1-801c-307c62c54c91.jpg', '女', 100, '2026-04-03 15:49:21', 1, 0, 'ec4uURaQfdvWe8Gagt9eVgxBXo/dWFHs@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (5, '阿宝', '17673297533', '静.逄@yahoo.com', '$2a$12$VOxsIOilu9YwJDxyGt5dxO1UF27hkG15HyRvbbpj0jFr9mVFaZiH.', '8d6efdfe-75c5-4af3-8b03-1dab67c02f1b.jpg', '女', 100, '2026-04-03 15:49:22', 1, 0, 'JxJ3mLKujoXWe8Gagt9eVjO/eqFerHiS@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (6, '小葡萄', '17742030782', '梓晨.鲁@hotmail.com', '$2a$12$kVggIOHaYTmFCo61h9IXc.STtnC5lY.2R.Q/t3HUSSoWBmPUqaOgm', '7790af45-30ad-4577-8b52-68fb43115342.jpg', '女', 100, '2026-04-03 15:49:25', 1, 0, 'AyJOFRDS87XWe8Gagt9eVuGXqAU4lvtH@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (7, '小娃', '14709633758', '擎苍.空@yahoo.com', '$2a$12$7T2gD.fcxaX8lqlNuSZ1XOUTcu0oMEF4K40Mf4WlmSWZxXVaVRDZm', '89fea17e-7ff9-4d0a-80f7-2af7ca0eaca2.jpg', '女', 100, '2026-04-03 15:49:28', 1, 0, 'NRyEieMSTHjWe8Gagt9eVuDxbWRLUmvQ@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (8, '小铃铛', '18147050474', '明辉.闫@yahoo.com', '$2a$12$IbSFuyRu/ZztNepLO2jC2e3g1Cwn4sToU5wG3CUL/Sp7hPpXFX3W2', 'bafb77b5-243f-4ec0-815a-8d6a5b6c500e.jpg', '女', 100, '2026-04-03 15:49:31', 1, 0, 'iqIOlquMIk/We8Gagt9eVmzBo/ZCKETN@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (9, '阿宝', '15894563165', '君浩.印@hotmail.com', '$2a$12$0UGeAfERte0BxsgZXkcW3Omz3mTuCGYq8E7dA6eKpc4EtFUk50P/O', '88fdb2ef-5f43-4188-887f-e685c03019d9.jpg', '女', 100, '2026-04-03 15:49:34', 1, 0, 'NW2eRjU5pxjWe8Gagt9eVu++opK2lOB5@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (10, '小鹿', '15926158116', '苑博.宁@hotmail.com', '$2a$12$WX58xWIjXpz34UPeeAvQA.LFMMoYHkpMCMnt7VFth/DgctKzMifjW', 'c4a346af-eed8-4d1d-8a9e-c7ca0ad01999.jpg', '女', 100, '2026-04-03 15:49:36', 1, 0, 'YENqSwn9E1F506fu7H8tdJO5+1kF19Sg@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (11, '小陀螺', '17677683529', '浩宇.松@gmail.com', '$2a$12$QhZMAvVJ7I8rzc7uRuyuYeA8LMQPHdyRDlLbSkUQEIWst2w8JKmRq', 'cbff736c-adaf-41ff-901e-acb7f204070f.jpg', '女', 100, '2026-04-03 15:49:38', 1, 0, 'nitDIF06LUB506fu7H8tdEQSUzqR6q/9@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (12, '多多', '14579175249', '凯瑞.靳@gmail.com', '$2a$12$REZoyRCkbnDh5WHCKcQ13uIDU5PEq8Z0DpVSKbB8rguGfHu9Q17Wi', 'c3c72e12-466b-4d04-8e4c-cdf58fb65f16.jpg', '女', 100, '2026-04-03 15:49:40', 1, 0, 'WKAiJ8lN/Rd506fu7H8tdGmIk93l9IZG@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (13, '小汪汪', '14750397104', '皓轩.吕@yahoo.com', '$2a$12$qyg8Dp.EKS3V.NlJHEwQqOwAcfhdyzeUd/hlOaS0FVYhbGIppc2YS', '58d46398-e674-4e77-84c5-dc8ccad14684.jpg', '女', 100, '2026-04-03 15:49:43', 1, 0, 'zfLaqHMtLfp506fu7H8tdORzvcBZ9Kb9@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (14, '小飞花', '17109699777', '鹤轩.闵@gmail.com', '$2a$12$PDmYywCivPtcEkcnOXpO.eL2BOcJM76m/Uha30sT5/HPsfHbDR6Wm', 'e5be2928-acd7-4cd5-8d6f-8a60dac1b975.jpg', '女', 100, '2026-04-03 15:49:46', 1, 0, 'nC+ndUQBdUB506fu7H8tdN+W80Rr3P0i@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (15, '小太阳', '15229652553', '伟宸.景@hotmail.com', '$2a$12$zcAoJTCvmX0NzGBREDlfo.dA5Wy7jBNz8inOXA1oxtlXAv/usrpR.', '8e56070c-3d4c-4b2f-baba-02160c1bf224.jpg', '男', 100, '2026-04-03 15:49:48', 1, 0, 'vpLd0CgOq9J506fu7H8tdAd2IJhlTpQs@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (16, '小太阳', '15274789455', '俊驰.庄@gmail.com', '$2a$12$Qlo7AQ5o9p96LIAUMjGzYeG3YUDykjuPQ4UZAoUlEBsN.mEofnqSS', '98b3ee2c-18a7-47af-af38-e82e613648be.jpg', '男', 100, '2026-04-03 15:49:51', 1, 0, 't7W3dgBVYpl506fu7H8tdFDmJESJT6qd@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (17, '蜜蜂', '17209399858', '烨华.皇甫@yahoo.com', '$2a$12$P08qUbOVIlhL/j6ke9A3mOeTKtSLavEHbKbryDcB8jkfuDiGhjFTS', 'a7bc0acd-df58-4b6f-81e3-b5b8b8c1682a.jpg', '男', 100, '2026-04-03 15:49:54', 1, 0, '74XJDb3vhWV506fu7H8tdML58jI4N93d@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (18, '小霸王', '17014149379', '思源.明@gmail.com', '$2a$12$LiAET5LXUggA1pvxkeUPOuJuFVLQJGl9blI2EXBfhJmDXrgNeL8fq', 'df245dc1-ff44-4f68-89ea-8a78028bd14b.jpg', '男', 100, '2026-04-03 15:49:56', 1, 0, 'cKE6emFrK91506fu7H8tdMg7m+U0ezz+@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (19, '小狮子', '15652071538', '擎苍.张@gmail.com', '$2a$12$VP7rJIyEOVW7cW90VuuIfuLm7htzwTh4l9r8neCiXt1dC26Flg2ai', '26514bc2-f722-4199-99c1-41e4ea27f066.jpg', '男', 100, '2026-04-03 15:49:59', 1, 0, '9q3wDyTRkfV506fu7H8tdPClMe2xXyWY@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
INSERT INTO `user` VALUES (20, '小丹', '17323403644', '伟祺.晋@gmail.com', '$2a$12$RBGSumg3U9oqd8tOGkiNx.V1iCp5bHnFEJXc0iGj31Dd3r2f6aHQi', '93a09e1e-011f-414c-9a7e-fc3add606829.jpg', '男', 100, '2026-04-03 15:52:05', 1, 0, '9/QXZdkVLal506fu7H8tdHP2IJwdBzNU@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');

SET FOREIGN_KEY_CHECKS = 1;
