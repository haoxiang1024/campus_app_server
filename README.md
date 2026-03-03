




# 🏫 智慧校园：失物招领与综合服务平台

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Status](https://img.shields.io/badge/Status-Completed-success.svg)

本项目是一个基于 **Android** 和 **Spring Boot** 的前后端分离架构的校园综合服务平台。核心业务涵盖**失物招领、即时通讯（IM）、校园社区互动及后台数据管理**。

本项目代码规范、结构清晰、业务闭环完整，内置了敏感词过滤算法与 JWT 鉴权机制，**非常适合作为本科毕业设计、课程设计或高质量的实战学习参考**。

---

## ✨ 核心功能 (Features)

### 📱 移动端 (Android App)
* **🗝️ 完善的用户体系**：支持邮箱验证码注册、登录、密码找回，集成 JWT 无状态鉴权。
* **📦 失物招领大厅**：支持物品分类浏览、关键词搜索、历史搜索记录保存。
* **📷 图文发布系统**：支持拍照或相册选择、图片本地压缩上传、表单信息录入。
* **💬 融云即时通讯**：集成第三方融云 IM SDK，支持买卖双方/失主与拾到者实时单聊，保障隐私。
* **🙌 社区互动**：支持对失物招领帖子进行评论、留言互动。
* **⚙️ 系统级特性**：内置多语言切换、字体大小调节、XUpdate 版本自动更新、AgentWeb 协议展示。

### 💻 服务端与后台 (Spring Boot Server)
* **🔒 安全鉴权**：基于 JWT Token 的全链路 API 请求拦截与校验。
* **🛡️ 绿网行动**：内置基于 DFA（确定有穷自动机）算法的敏感词过滤系统，净化校园社区环境。
* **✉️ 邮件服务**：真实接入 JavaMailSender，实现注册与重置密码的邮件验证码下发。
* **📊 后台管理系统**：内置基于 Bootstrap + jQuery 的 Web 后台页面，提供用户管理、帖子统计与敏感词维护。

---

## 🛠️ 技术栈 (Tech Stack)

### 前端开发 (Frontend)
* **语言**：Java (Android SDK)
* **UI 框架**：XUI (一套优雅的 Android UI 框架)
* **网络请求**：Retrofit2 + OkHttp3 + Gson
* **即时通讯**：融云 RongCloud IM SDK
* **其他组件**：AgentWeb (WebView 增强)、XUpdate (应用更新)

### 后端开发 (Backend)
* **核心框架**：Spring Boot 2.x
* **持久层**：MyBatis + MySQL 8.0
* **安全与认证**：JWT (JSON Web Token)
* **工具库**：Hutool、JavaMail、Aho-Corasick (敏感词算法)
* **Web 后台管理**：HTML5 + Bootstrap 4 + SweetAlert2

---

## 📂 项目结构 (Project Structure)

```text
Campus-App/
├── campus_app_android/         # Android 客户端代码
│   ├── app/src/main/java/      # 核心 Java 业务逻辑
│   ├── app/src/main/res/       # 布局、图片、多语言等资源文件
│   └── app/build.gradle        # 依赖与多渠道打包配置
│
└── campus_app_server/          # Spring Boot 服务端代码
    ├── src/main/java/          # Controller, Service, Mapper 控制器与服务
    ├── src/main/resources/     # application.yml 配置及 MyBatis Mapper XML
    └── src/main/resources/static/admin # Web 端后台管理系统静态资源

```

---

## 🚀 快速开始 (Quick Start)

### 1. 环境准备

* **JDK**：Java 8 或以上
* **数据库**：MySQL 5.7 或 8.0
* **IDE**：Android Studio (用于客户端)、IntelliJ IDEA (用于服务端)

### 2. 服务端运行

1. 创建 MySQL 数据库 `school_app`，字符集推荐 `utf8mb4`。
2. 导入根目录下的 SQL 脚本：`source school_app.sql;`。
3. 使用 IDEA 打开 `campus_app_server` 目录。
4. 修改 `src/main/resources/application.yml` 中的数据库账号密码、邮箱授权码（用于发送验证码）以及融云的 AppKey/Secret。
5. 运行 `SchoolApplication.java` 启动后端服务。

> **访问后台管理面板**：`http://localhost:8080/admin/index.html`

### 3. 客户端运行

1. 使用 Android Studio 打开 `campus_app_android` 目录。
2. 找到 `app/src/main/assets/url.properties` 文件。
3. 将 `baseUrl` 修改为你的服务器 IP 地址（例如：`http://192.168.1.100:8080/`）。*注意：如果在真机/模拟器运行，请不要使用 localhost。*
4. Sync Gradle 并点击 Run 运行到真机或模拟器。

---

## 💡 答辩/设计亮点 (Highlights for Graduation Thesis)

如果你将本项目用于毕业设计，可以重点阐述以下亮点：

1. 摒弃了传统的 Session，采用 **JWT 无状态鉴权**，符合现代前后端分离架构标准。
2. 接入 **DFA 敏感词过滤算法**，体现了对内容安全与算法落地的思考。
3. 利用第三方 **长连接 IM SDK** 实现了低延迟的聊天功能，解决了短轮询带来的服务器压力问题。
4. 拥有从移动端展示到 Web 端后台管理的一整套 **闭环业务能力**。

---

