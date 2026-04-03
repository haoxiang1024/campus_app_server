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

 Date: 03/04/2026 15:54:51
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
-- Records of comment
-- ----------------------------

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
-- Records of lostfound
-- ----------------------------

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
-- Records of lostfound_type
-- ----------------------------

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户积分变动历史表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分商城商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_item
-- ----------------------------

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
INSERT INTO `user` VALUES (1, '小妹妹', '15839825103', '姣姣.羊舌@gmail.com', '$2a$12$Ikp/n4voUd3RAiwF9YFZr.hHM3j60HYtE37TMqGIquuaqYCyTo3YG', 'cf44d1eb-791c-4ad3-bbf4-eda887b06bbb.jpg', '女', 100, '2026-04-03 15:49:11', 1, 0, 'j3iWf2UVvHvWe8Gagt9eVjklvUvzCtzn@5zau.cn.rongnav.com;5zau.cn.rongcfg.com');
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
