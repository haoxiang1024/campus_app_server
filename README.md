# 校园失物招领系统服务端 (Campus Lost & Found Server)

![Java](https://img.shields.io/badge/Java-1.8+-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x-brightgreen.svg)
![MyBatis](https://img.shields.io/badge/MyBatis-3.x-red.svg)
![MySQL](https://img.shields.io/badge/MySQL-5.7%2B-orange.svg)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)

本项目是一个基于 Spring Boot 构建的**校园失物招领系统**后端服务，作为本科毕业设计的核心支撑部分。本服务主要负责处理核心业务逻辑，为 Android 客户端提供稳定可靠的 RESTful API 接口，同时内置了一套轻量级的 Web 后台管理系统，方便管理员进行平台数据维护与数据可视化分析。

---

## 🛠️ 技术栈选型

**后端核心引擎**
- **核心框架**: Spring Boot (Java)
- **持久层框架**: MyBatis
- **数据库**: MySQL
- **项目构建**: Maven
- **安全与鉴权**: JWT (JSON Web Token) 与自定义全局拦截器

**第三方服务接入**
- **即时通讯**: 融云 IM API (`RongCloudApi`)
- **通知服务**: 邮件发送 (`JavaMailSender`)
- **安全过滤**: 自定义敏感词加载与过滤算法

**内置后台管理 (Web Admin)**
- **前端技术**: HTML5, CSS3, JavaScript
- **UI 组件库**: Bootstrap
- **数据可视化**: ECharts
- **弹窗交互**: SweetAlert2

---

## ✨ 核心业务模块

### 1. 身份认证与安全
- **无状态鉴权**: 基于 JWT 实现安全、高效的 API 访问控制。
- **多重验证**: 封装了邮件验证功能，保障用户注册及重要操作的真实性。
- **内容合规**: 内置敏感词过滤工具 (`SensitiveWordUtil`)，自动屏蔽违规内容，维护校园社区健康。

### 2. 失物招领模块 (Lost & Found)
- **信息流转**: 支持寻物启事与招领启事的发布、修改、图片上传与状态追踪。
- **动态分类**: 灵活的物品分类管理 (`LostFoundType`)。
- **智能检索**: 记录用户搜索信息 (`SearchInfo`)，支持高效的关键信息查询。

### 3. 互动与社交
- **社区评论**: 用户可对具体的失物招领贴进行留言探讨 (`CommentController`)。
- **即时私信**: 整合融云 IM 接口，实现用户间的实时单聊沟通 (`MessageController`)。

### 4. 积分商城体系 (Point & Shop)
- **行为激励**: 用户的特定交互行为（如发布、找回等）可获取积分，系统完整记录积分流向 (`PointHistory`)。
- **商品兑换**: 提供积分商品展示 (`ShopItem`) 与兑换下单机制 (`ExchangeOrder`)。

### 5. Web 后台管理面板
系统 `static/admin/` 目录下内置了完整的管理员操作界面：
- **数据仪表盘**: 基于 ECharts 的平台注册量、发布量等数据统计与可视化。
- **内容与用户审查**: 失物招领信息管理、评论管理与违规用户封禁/解封。
- **订单处理**: 积分商城的兑换订单发货与核销管理。

---

## 📁 核心项目结构

```text
campus_app_server/
├── src/main/java/com/school/
│   ├── config/          # 全局配置 (JWT, MyBatis, 拦截器, 敏感词加载)
│   ├── controller/      # API 路由控制层 (RESTful 接口)
│   ├── entity/          # 数据库实体类及 VO/DTO 数据传输模型
│   ├── mapper/          # MyBatis 数据库访问接口 (DAO)
│   ├── services/        # 核心业务逻辑层 (包含融云等第三方API调用)
│   └── utils/           # 通用工具类库 (加密, JWT, IP解析, 文件处理等)
├── src/main/resources/
│   ├── mapper/          # MyBatis SQL 映射 XML 文件
│   ├── static/admin/    # 内置 Web 后台管理系统前端资源
│   ├── static/pages/    # 外部展示或协议页面 (如隐私政策等)
│   └── application.yml  # Spring Boot 核心环境配置文件
└── pom.xml              # Maven 依赖配置管理