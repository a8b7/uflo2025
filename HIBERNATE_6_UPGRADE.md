# UFLO2 Hibernate 6.x 升级指南

## 问题描述

在升级到Hibernate 6.x后，应用启动时出现以下错误：
```
A component required a bean named 'sessionFactory' that could not be found.
```

## 根本原因

1. **配置缺失**：传统Spring XML配置中缺少`sessionFactory` bean定义
2. **版本兼容性**：Hibernate 6.x需要特定的配置和命名策略
3. **依赖注入变化**：Spring Boot 3.x中自动配置与传统Spring配置的差异

## 解决方案

### 1. 新增配置文件

#### HibernateConfig.java
- **位置**：`uflo-core/src/main/java/com/bstek/uflo/config/HibernateConfig.java`
- **作用**：配置SessionFactory和事务管理器
- **关键特性**：
  - 兼容Hibernate 6.x的配置属性
  - 自动扫描实体类包
  - 配置事务管理器

#### DataSourceConfig.java
- **位置**：`uflo-core/src/main/java/com/bstek/uflo/config/DataSourceConfig.java`
- **作用**：提供数据源配置
- **说明**：实际项目中应该配置生产环境数据源

### 2. 更新Spring配置

#### uflo-context-configs.xml
- **变更**：添加组件扫描配置
- **目的**：让Spring容器识别新的Java配置类

```xml
<!-- 扫描配置类 -->
<context:component-scan base-package="com.bstek.uflo.config"/>
```

## 配置参数说明

### Hibernate 6.x 关键配置

| 属性 | 值 | 说明 |
|------|-----|------|
| hibernate.dialect | org.hibernate.dialect.MySQLDialect | 数据库方言 |
| hibernate.physical_naming_strategy | CamelCaseToUnderscoresNamingStrategy | 物理命名策略 |
| hibernate.implicit_naming_strategy | ImplicitNamingStrategyLegacyJpaImpl | 隐式命名策略 |
| hibernate.hbm2ddl.auto | update | 自动更新表结构 |

## 生产环境配置建议

### 1. 数据源配置
建议使用HikariCP连接池：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/uflo_db
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

### 2. Hibernate优化配置
```properties
# 批处理优化
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# 连接池配置
spring.jpa.properties.hibernate.connection.provider_disables_autocommit=true

# 统计信息（生产环境建议关闭）
spring.jpa.properties.hibernate.generate_statistics=false
```

## 验证配置

运行测试类验证配置：
```bash
mvn test -Dtest=HibernateConfigTest
```

## 常见问题

### Q: 数据库方言如何选择？
A: 根据实际数据库选择对应方言：
- MySQL: `org.hibernate.dialect.MySQLDialect`
- Oracle: `org.hibernate.dialect.OracleDialect`
- SQL Server: `org.hibernate.dialect.SQLServerDialect`

### Q: 表结构自动创建的风险？
A: `hbm2ddl.auto=update`仅适用于开发环境，生产环境应使用数据库迁移工具如Flyway或Liquibase。

### Q: 如何监控Hibernate性能？
A: 启用统计信息并配置日志：
```properties
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.stat=debug
```

## 兼容性注意事项

1. **Jakarta EE 9+**：使用`jakarta.persistence`而非`javax.persistence`
2. **Java 17+**：确保使用Java 17或更高版本
3. **Spring Boot 3.x**：需要与Spring Boot 3.x兼容的依赖版本

## 下一步

1. 根据实际环境配置数据源
2. 调整Hibernate参数以满足性能需求
3. 添加数据库迁移脚本用于生产环境部署