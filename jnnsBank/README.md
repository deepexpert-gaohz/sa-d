# AMS-4.0

## 组织结构

``` lua
ams
├── account-api -- 账户管理模块接口
├── account-service  -- 账户管理模块实现
├── ams-ui -- 暂未使用
├── ams-web -- 后端服务的组装，配置文件存放，前端代码
├── annual-api -- 年检模块接口
├── annual-service -- 年检模块实现
├── api-common -- 所有接口依赖的公共模块
├── apply-api -- 预约模块接口
├── apply-service -- 预约模块实现
├── common -- 所有工程依赖的公共模块（工具类、异常处理、初始化基类等）
├── customer-api -- 客户管理模块接口
├── customer-service -- 客户管理模块实现
├── db-entity -- 数据库相关公共模块（一般被service工程所依赖）
├── image-api -- 影像管理模块实现
├── image-service -- 影像管理模块实现
├── kyc-api -- 客户尽调(工商)模块接口
├── kyc-service -- 客户尽调(工商)模块实现
├── pbc-api -- 人行模块接口
├── pbc-service -- 人行模块实现
├── poi -- excel相关
├── system-api -- 系统基础管理模块实现
├── system-service -- 系统基础管理模块实现
├── ws-api -- 系统对外提供的标准接口接口（Rest/WebService）
└── ws-service -- 系统对外提供的标准接口实现（Rest/WebService）
```

## 技术选型

### 后端技术

技术 | 说明 | 官网
----|----|----
Spring Boot | 容器+MVC框架 | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
Spring Security | 认证和授权框架 | [https://spring.io/projects/spring-security](https://spring.io/projects/spring-security)
Spring Session | Session 管理 | [https://spring.io/projects/spring-session](https://spring.io/projects/spring-session)
Spring Data JPA（Hibernate实现） | ORM框架  | [https://spring.io/projects/spring-data-jpa](https://spring.io/projects/spring-data-jpa)
Swagger-UI | 文档生产工具 | [https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui)
Hibernator-Validator | 验证框架 | [http://hibernate.org/validator/](http://hibernate.org/validator/)
Druid | 数据库连接池 | [https://github.com/alibaba/druid](https://github.com/alibaba/druid)
Lombok | 简化对象封装工具 | [https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok)

### 前端技术

技术 | 说明 | 官网
----|----|----
 Layui | 前端框架 | [https://www.layui.com/](https://www.layui.com/)

## 环境搭建

### 开发工具

工具 | 说明 | 官网
----|----|----
IDEA | 开发IDE | [https://www.jetbrains.com/idea/download](https://www.jetbrains.com/idea/download)
X-shell | Linux远程连接工具 | [https://www.netsarang.com/zh/all-downloads/](https://www.netsarang.com/zh/all-downloads/)
Navicat | 数据库连接工具 | [https://www.navicat.com.cn/](https://www.navicat.com.cn/)

### 开发环境

工具 | 版本号 | 下载
----|----|----
JDK | 1.8 | [https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
Mysql | 5.7 | [https://dev.mysql.com/downloads/mysql/5.7.html#downloads](https://dev.mysql.com/downloads/mysql/5.7.html#downloads)

### 搭建步骤

> 本地环境搭建

- 克隆源代码到本地，使用IDEA或Eclipse打开，并完成编译;
- 在mysql中新建ams数据库
- 修改ams-web工程中的数据库配置文件（ams-web/src/main/resources/application-db.yml）
- 启动项目：直接运行com.ideatech.ams.AmsApp的main方法即可

## AMS-4.0相关接口文档

> 具体详见ShowDoc:

- [http://open.ezhanghu.cn/web/#/3?page_id=14](http://open.ezhanghu.cn/web/#/3?page_id=14)

Copyright (c) 2018-2019 IDEATECH