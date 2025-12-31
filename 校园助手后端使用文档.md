# 校园助手后端使用文档

## 技术栈：springboot+mybatis+mysql(本地运行)

1. 打开school-server\school文件夹,运行school_app.sql文件

2. 打开school-server\school\src\main\resources\application.yml,修改数据库连接配置下面是参考信息

3. ```
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/school_app?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
      username: 你自己的用户名
      password: 你自己的密码
    ```

4. 运行SchoolApplication,浏览器输入http://localhost:8081/school 看到welcome即运行成功





