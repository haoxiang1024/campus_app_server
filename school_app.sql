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

 Date: 10/04/2026 15:39:22
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
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `state` int NULL DEFAULT 0 COMMENT '评论状态：0-正常，1-删除',
  `parent_id` int NULL DEFAULT 0 COMMENT '父评论ID',
  `reply_user_id` int NULL DEFAULT NULL COMMENT '被回复用户的ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '评论创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_comment_lostfound`(`lostfound_id` ASC) USING BTREE,
  INDEX `fk_comment_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_comment_parent`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_lostfound` FOREIGN KEY (`lostfound_id`) REFERENCES `lostfound` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (32, 1, 2, '1', 1, 0, 0, '2026-04-09 15:38:55');
INSERT INTO `comment` VALUES (33, 1, 21, '2', 1, 32, 2, '2026-04-09 15:39:13');
INSERT INTO `comment` VALUES (34, 1, 2, '345', 1, 33, 21, '2026-04-09 15:39:32');
INSERT INTO `comment` VALUES (35, 2, 1, '小姐', 2, 0, 0, '2026-04-10 14:46:51');
INSERT INTO `comment` VALUES (36, 5, 22, '😀', 1, 0, 0, '2026-04-10 15:14:43');
INSERT INTO `comment` VALUES (37, 5, 22, '😅', 1, 36, 22, '2026-04-10 15:14:50');
INSERT INTO `comment` VALUES (38, 1, 22, '😊', 1, 0, 0, '2026-04-10 15:15:03');
INSERT INTO `comment` VALUES (39, 4, 22, '不', 1, 0, 0, '2026-04-10 15:15:30');
INSERT INTO `comment` VALUES (40, 4, 22, '不', 1, 39, 22, '2026-04-10 15:15:34');
INSERT INTO `comment` VALUES (41, 16, 22, '🤣', 1, 0, 0, '2026-04-10 15:16:26');
INSERT INTO `comment` VALUES (42, 16, 22, '要', 1, 41, 22, '2026-04-10 15:16:30');
INSERT INTO `comment` VALUES (43, 1, 22, '她', 1, 38, 22, '2026-04-10 15:17:03');
INSERT INTO `comment` VALUES (44, 1, 22, '33号', 1, 32, 2, '2026-04-10 15:17:29');
INSERT INTO `comment` VALUES (45, 1, 22, '吗', 1, 32, 21, '2026-04-10 15:17:36');

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
  `points_cost` int NOT NULL DEFAULT 0 COMMENT '消耗积分',
  `verify_code` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '8位提货核验码',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-待核验, 1-已核验, 2-已取消',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `verify_time` datetime NULL DEFAULT NULL COMMENT '核验时间',
  `verify_admin_id` int NULL DEFAULT NULL COMMENT '核验管理员ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_verify_code`(`verify_code` ASC) USING BTREE,
  INDEX `fk_order_item`(`item_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_item` FOREIGN KEY (`item_id`) REFERENCES `shop_item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分兑换订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exchange_order
-- ----------------------------
INSERT INTO `exchange_order` VALUES (1, 'EX1775801696111', 21, 3, '优惠券', 50, '46E83EAB', 1, '2026-04-10 14:14:56', '2026-04-10 14:20:15', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '失物招领信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lostfound
-- ----------------------------
INSERT INTO `lostfound` VALUES (1, '丢失水杯', '94fc4dfb-d8e2-47aa-9f94-248cb9940ea2.jpg', '2026-04-04 15:25:21', '在西南科技大学(青义校区西区图书馆) 丢失了水杯，，联系电话：18682675515', '西南科技大学(青义校区西区图书馆)', '18682675515', '寻找中', 1, 4, 1, 'admin', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (2, '丢失水杯', '6e4365ed-13d8-4d23-ad0f-058e83d8bf05.jpg', '2026-04-04 15:25:21', '在西南科技大学-7食堂 丢失了水杯，，联系电话：17673297533', '西南科技大学-7食堂', '17673297533', '寻找中', 1, 4, 5, '阿宝', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (3, '丢失水杯', '4facd3e3-b938-4fb0-b83b-04d4993acea9.jpg', '2026-04-04 15:25:21', '在西南科技大学(青义校区东1教学楼) 丢失了水杯，，联系电话：17673297533', '西南科技大学(青义校区东1教学楼)', '17673297533', '寻找中', 1, 4, 5, '阿宝', '失物', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (4, '丢失手机', '68ea307e-1d55-4603-8a82-5c0444dfa9c6.jpg', '2026-04-04 15:25:21', '在西南科技大学东区-体育场 丢失了手机，，联系电话：14769903669', '西南科技大学东区-体育场', '14769903669', '寻找中', 1, 1, 3, '小豆豆', '失物', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (5, '丢失手机', '6f8251b0-ba5a-4b96-9a3f-822ba2340c72.jpg', '2026-04-04 15:25:21', '在西南科技大学(青义校区)-西7教学楼 丢失了手机，，联系电话：14769903669', '西南科技大学(青义校区)-西7教学楼', '14769903669', '寻找中', 1, 1, 3, '小豆豆', '失物', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (6, '丢失手机', '617d2bfd-27e8-4714-a10c-00692fd18ab3.jpg', '2026-04-04 15:25:21', '在西南科技大学(青义校区西区图书馆) 丢失了手机，，联系电话：18682675515', '西南科技大学(青义校区西区图书馆)', '18682675515', '寻找中', 1, 1, 1, 'admin', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (7, '丢失黑色签字笔', '315e3eb8-e623-47ea-beee-2e4d52be76f4.jpg', '2026-04-04 15:25:21', '在西南科技大学-7食堂 丢失了黑色签字笔，，联系电话：14769903669', '西南科技大学-7食堂', '14769903669', '寻找中', 1, 4, 3, '小豆豆', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (8, '丢失黑色签字笔', 'b340f516-ed05-4225-be15-d7ebeccc8690.jpg', '2026-04-04 15:25:21', '在西南科技大学(青义校区东1教学楼) 丢失了黑色签字笔，，联系电话：17788266423', '西南科技大学(青义校区东1教学楼)', '17788266423', '寻找中', 1, 4, 4, '小米', '失物', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (9, '丢失黑色签字笔', 'efac7436-34a1-4107-abc7-d17f69bcf2a2.jpg', '2026-04-04 15:25:21', '在西南科技大学东区-体育场 丢失了黑色签字笔，，联系电话：15181544770', '西南科技大学东区-体育场', '15181544770', '寻找中', 1, 4, 2, '小卷毛', '失物', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (10, '丢失耳机', 'e3c82710-9324-4eb5-8cb8-aee29d2abee9.jpg', '2026-04-04 15:25:21', '在西南科技大学(青义校区)-西7教学楼 丢失了耳机，，联系电话：15181544770', '西南科技大学(青义校区)-西7教学楼', '15181544770', '寻找中', 1, 1, 2, '小卷毛', '失物', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (11, '丢失耳机', '726c7791-73dc-4335-baa3-d28a109c2533.jpg', '2026-04-04 14:33:28', '在西南科技大学(青义校区西区图书馆) 丢失了耳机，，联系电话：15181544770', '西南科技大学(青义校区西区图书馆)', '15181544770', '寻找中', 0, 1, 2, '小卷毛', '失物', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (12, '丢失耳机', '4973a9bd-cb61-405d-8bf4-4db3df96dd88.jpg', '2026-04-04 14:33:28', '在西南科技大学-7食堂 丢失了耳机，，联系电话：14769903669', '西南科技大学-7食堂', '14769903669', '寻找中', 0, 1, 3, '小豆豆', '失物', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (13, '找到水杯', 'e623470d-4264-4936-8f7d-cd0a46ec588f.jpg', '2026-04-04 15:25:33', '在西南科技大学(青义校区东1教学楼) 找到了水杯，，联系电话：18682675515', '西南科技大学(青义校区东1教学楼)', '18682675515', '待认领', 1, 4, 1, 'admin', '招领', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (14, '找到水杯', '5634ae0a-ff5c-401e-a732-c5fff9899ab5.jpg', '2026-04-04 15:25:33', '在西南科技大学东区-体育场 找到了水杯，，联系电话：17673297533', '西南科技大学东区-体育场', '17673297533', '待认领', 1, 4, 5, '阿宝', '招领', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (15, '找到水杯', 'a2491010-97d2-44f3-a405-5c9918bc8195.jpg', '2026-04-04 15:25:33', '在西南科技大学(青义校区)-西7教学楼 找到了水杯，，联系电话：15181544770', '西南科技大学(青义校区)-西7教学楼', '15181544770', '待认领', 1, 4, 2, '小卷毛', '招领', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (16, '找到手机', 'f04171e2-43c9-4cda-9ef8-3c39ffce48e0.jpg', '2026-04-04 15:25:33', '在西南科技大学(青义校区西区图书馆) 找到了手机，，联系电话：15181544770', '西南科技大学(青义校区西区图书馆)', '15181544770', '待认领', 1, 1, 2, '小卷毛', '招领', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (17, '找到手机', '309170f6-8329-496d-a3a7-423b5fefa3c0.jpg', '2026-04-04 15:25:33', '在西南科技大学-7食堂 找到了手机，，联系电话：17673297533', '西南科技大学-7食堂', '17673297533', '待认领', 1, 1, 5, '阿宝', '招领', 104.699591, 31.543621);
INSERT INTO `lostfound` VALUES (18, '找到手机', '0cea2d56-d70e-4d9f-b76d-160585ede90d.jpg', '2026-04-04 15:25:33', '在西南科技大学(青义校区东1教学楼) 找到了手机，，联系电话：14769903669', '西南科技大学(青义校区东1教学楼)', '14769903669', '待认领', 1, 1, 3, '小豆豆', '招领', 104.706371, 31.541627);
INSERT INTO `lostfound` VALUES (19, '找到黑色签字笔', '7b3875e3-284c-4f45-bbf9-09dddd70d28f.jpg', '2026-04-04 15:25:33', '在西南科技大学东区-体育场 找到了黑色签字笔，，联系电话：15181544770', '西南科技大学东区-体育场', '15181544770', '待认领', 1, 4, 2, '小卷毛', '招领', 104.710198, 31.543931);
INSERT INTO `lostfound` VALUES (20, '找到黑色签字笔', '1103b4e2-a472-488e-b27b-5fb1c8ae51f8.jpg', '2026-04-04 15:25:33', '在西南科技大学(青义校区)-西7教学楼 找到了黑色签字笔，，联系电话：14769903669', '西南科技大学(青义校区)-西7教学楼', '14769903669', '待认领', 1, 4, 3, '小豆豆', '招领', 104.693323, 31.543984);
INSERT INTO `lostfound` VALUES (21, '找到黑色签字笔', '91768109-5c2c-4454-8a67-8ffa1dac99fc.jpg', '2026-04-04 15:25:33', '在西南科技大学(青义校区西区图书馆) 找到了黑色签字笔，，联系电话：17788266423', '西南科技大学(青义校区西区图书馆)', '17788266423', '待认领', 1, 4, 4, '小米', '招领', 104.694491, 31.540717);
INSERT INTO `lostfound` VALUES (22, '找到耳机', 'dd7ccca2-db95-4c4d-9c5f-67c75d8b1633.jpg', '2026-04-04 15:25:33', '在西南科技大学-7食堂 找到了耳机，，联系电话：17673297533', '西南科技大学-7食堂', '17673297533', '待认领', 1, 1, 5, '阿宝', '招领', 104.699591, 31.543621);
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
INSERT INTO `lostfound` VALUES (52, '2', 'b838b84d-afce-4195-a58e-a3e99d7b2a0c.jpg', '2026-04-04 15:56:03', '2', '中国四川省广安市广安区井河镇（黑沟院子）', '18682675515', '待审核', 0, 1, 1, NULL, '失物', 106.62948, 30.736891);
INSERT INTO `lostfound` VALUES (53, '123', '0e3b523e-8408-42a6-a4a4-aca18d075e46.jpg', '2026-04-10 13:57:38', '123', '中国四川省广安市广安区井河镇（皂角岩）', '18784212634', '已驳回', 0, 2, 21, NULL, '失物', 106.629542, 30.736848);
INSERT INTO `lostfound` VALUES (54, '223', 'a45e4931-fbc8-46df-b7b4-2c0f62df6a1a.jpg', '2026-04-10 13:57:38', '223', '中国四川省广安市广安区井河镇（双胜电动三轮车）', '18784212634', '已驳回', 0, 3, 21, NULL, '招领', 106.629542, 30.736848);
INSERT INTO `lostfound` VALUES (55, '小姐', 'b9f88e37-7084-4598-8a7c-d346104d474c.jpg', '2026-04-10 14:47:32', '1', '中国四川省广安市广安区井河镇', '18682675515', '已驳回', 0, 1, 1, NULL, '失物', 106.628973, 30.737259);
INSERT INTO `lostfound` VALUES (56, '12345', 'aebc3c64-1cfa-4d3c-bdb8-f3d981a92b6f.jpg', '2026-04-10 15:16:06', '11111', '中国四川省广安市广安区井河镇', '18682675516', '待审核', 0, 4, 22, NULL, '失物', 106.628973, 30.737259);
INSERT INTO `lostfound` VALUES (57, '5678', 'b9413166-38e9-4230-8e17-0340f7934c64.jpg', '2026-04-10 15:16:45', '5678', '中国四川省广安市广安区井河镇', '18682675516', '待审核', 0, 4, 22, NULL, '招领', 106.628973, 30.737259);

-- ----------------------------
-- Table structure for lostfound_type
-- ----------------------------
DROP TABLE IF EXISTS `lostfound_type`;
CREATE TABLE `lostfound_type`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '类型唯一标识ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '失物招领类型表' ROW_FORMAT = DYNAMIC;

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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_message_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_message_parent`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `fk_message_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '留言信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (7, 21, '那你', '2026-04-09 15:07:58', 1, 0, NULL);
INSERT INTO `message` VALUES (8, 21, '那你', '2026-04-09 15:08:01', 1, 7, 21);
INSERT INTO `message` VALUES (9, 1, '小姐', '2026-04-10 14:46:38', 2, 0, NULL);
INSERT INTO `message` VALUES (11, 22, '😭', '2026-04-10 15:18:08', 1, 0, NULL);
INSERT INTO `message` 