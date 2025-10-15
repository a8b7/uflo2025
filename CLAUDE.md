# UFLO2 项目开发指南

## 项目概述

UFLO2 是一个纯Java工作流引擎，架构于Spring、Hibernate之上，提供丰富的业务流程流转功能。该项目采用现代化的Web流程设计器，支持可视化流程设计与制作。
### 当前任务 
处理 Hibernate 6.x 升级后的兼容问题 ！

### 核心特性
- 纯Java实现，架构于Spring + Hibernate
- 支持并行、动态并行、串行、会签等流程模式
- 基于Web的流程模板设计器
- 支持单机或集群部署
- 完整的任务管理和监控功能

## 项目结构

```
uflo-master/
├── uflo-parent/          # Maven父项目配置
├── uflo-core/            # 流程引擎核心模块
├── uflo-console/         # 服务端控制台模块
├── uflo-console-js/      # 前端控制台模块
├── README.md            # 项目说明文档
├── CHANGELOG.md         # 版本变更记录
└── LICENSE.txt          # 开源协议
```

### 模块说明

#### uflo-parent
- Maven父项目配置
- 统一版本管理和依赖管理
- 构建插件配置

#### uflo-core
- 流程引擎核心实现
- 流程定义、实例管理
- 任务调度、查询服务
- 数据模型和业务逻辑

#### uflo-console
- 服务端控制台实现
- RESTful API接口
- 流程部署和管理
- Servlet配置

#### uflo-console-js
- 前端控制台实现
- React + Redux架构
- 流程设计器界面
- 任务管理界面

## 技术栈

### 后端技术
- **Java 17+**: 主要开发语言
- **Spring 5**: 依赖注入和框架基础
- **Hibernate 6.1.7.Final**: 数据持久化
- **Maven**: 项目构建和依赖管理
- **Quartz 2.2.1**: 任务调度
- **Apache Commons**: 工具类库

### 前端技术
- **React 16.3.1**: UI组件框架
- **Redux 5.0.7**: 状态管理
- **jQuery 3.3.1**: DOM操作和Ajax
- **Bootstrap 3.3.6**: UI组件库
- **Webpack 4**: 模块打包工具
- **Babel**: JavaScript编译器

### 构建工具
- **Maven**: Java项目构建
- **Webpack**: 前端资源打包
- **Node.js + npm**: 前端依赖管理

## 开发环境配置

### 必需环境
- JDK 1.7 或更高版本
- Maven 3.0+
- Node.js 6.0+
- MySQL/Oracle/SQLServer 数据库

### 本地开发配置

#### 1. 克隆项目
```bash
git clone https://github.com/youseries/uflo.git
cd uflo
```

#### 2. 构建Java项目
```bash
mvn clean install
```

#### 3. 构建前端项目
```bash
cd uflo-console-js
npm install
npm run build
```

#### 4. 数据库配置
- 根据实际环境配置数据库连接
- 运行时会自动创建表结构

## 核心API说明

### 流程服务 (ProcessService)
- `getProcessById(long processId)`: 根据ID获取流程定义
- `getProcessByKey(String key)`: 根据Key获取流程定义
- `startProcessById(long processId, String businessId, Map<String, Object> variables)`: 启动流程实例

### 任务服务 (TaskService)
- `saveTask(String taskId, Map<String, Object> variables)`: 保存任务
- `completeTask(String taskId, Map<String, Object> variables)`: 完成任务
- `startTask(String taskId, Map<String, Object> variables)`: 开始任务

### 查询服务 (Query Service)
- `ProcessQuery`: 流程定义查询
- `ProcessInstanceQuery`: 流程实例查询
- `TaskQuery`: 任务查询
- `HistoryProcessInstanceQuery`: 历史流程实例查询

## 开发规范

### Java开发规范
- 遵循阿里巴巴Java开发手册
- 使用统一的代码格式化配置
- 必须编写单元测试
- 异常处理要明确具体类型

### 前端开发规范
- 使用ES6+语法
- 组件采用函数式写法
- Redux状态管理规范
- CSS类命名使用BEM规范

### 数据库规范
- 表名使用小写字母和下划线
- 字段名使用驼峰命名法
- 必须有主键和创建时间字段
- 外键关联要建立索引

## 构建和部署

### 开发环境构建
```bash
# 构建整个项目
mvn clean install

# 单独构建核心模块
mvn clean install -pl uflo-core

# 构建前端资源
cd uflo-console-js && npm run build
```

### 生产环境部署
1. 将uflo-core.jar加入项目classpath
2. 配置Spring环境，引入uflo-context-configs.xml
3. 配置数据库连接
4. 将uflo-console.war部署到应用服务器
5. 配置前端资源路径

### 集群部署配置
- 实现CacheService接口，配置分布式缓存
- 配置数据库集群
- 配置负载均衡

## 扩展开发

### 自定义任务节点
1. 继承TaskNode类
2. 实现execute方法
3. 在Spring中注册为Bean

### 自定义流程监听器
1. 实现ProcessListener接口
2. 实现相应监听方法
3. 在流程定义中配置监听器

### 自定义身份验证
1. 实现IdentityService接口
2. 实现用户和角色查询方法
3. 配置到Spring环境中

## 常见问题

### Q: 如何自定义任务分配策略？
A: 实现AssigneeProvider接口，在Spring中注册为Bean。

### Q: 如何集成到现有Spring项目？
A: 在Spring配置文件中引入uflo-context-configs.xml，配置数据源。

### Q: 前端资源如何构建？
A: 在uflo-console-js目录下运行npm install和npm run build。

### Q: 如何调试流程执行？
A: 启用debug日志，查看com.bstek.uflo包下的日志输出。
