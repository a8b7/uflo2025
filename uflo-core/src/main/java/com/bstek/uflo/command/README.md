# Command 包 (com.bstek.uflo.command)

## 概述

Command 包实现了命令模式，用于封装工作流引擎中的各种操作。命令模式允许将请求封装为对象，从而使可用不同的请求对客户端进行参数化，支持请求排队、记录日志等操作，并支持可撤销的操作。

## 包结构

- `com.bstek.uflo.command`
  - `impl` - 命令实现类和事务管理实现

## 核心接口与类

### Command 接口
定义所有命令的通用接口，所有具体命令都实现该接口。提供统一的执行方法。

### CommandService 接口
命令服务接口，负责执行各种命令，通常会处理事务和环境上下文。

### SpringTransactionCommandService
实现 CommandService 接口，集成 Spring 的事务管理机制，确保命令执行时的数据一致性。

## 设计模式

### 命令模式
- **目的**: 将请求封装为对象，从而可以使用不同的请求对客户端进行参数化
- **角色**:
  - Command: 命令接口，定义命令执行的规范
  - ConcreteCommand: 具体命令实现类，如部署流程命令、启动流程命令等
  - CommandService: 命令执行者，负责执行具体的命令
  - Client: 创建具体命令并设置命令的接收者

### 优势
1. **解耦**: 发送命令的对象与执行命令的对象解耦
2. **扩展性**: 可以轻松添加新的命令而无需修改现有代码
3. **事务管理**: 统一在 CommandService 层处理事务
4. **日志记录**: 命令可以记录执行状态，便于调试和审计
5. **支持撤销**: 可以实现命令的反向操作

## 主要命令功能

1. **流程定义管理命令**
   - DeployProcessCommand: 部署流程定义
   - DeleteProcessDefinitionCommand: 删除流程定义
   - GetProcessCommand: 获取流程定义

2. **流程实例管理命令**
   - StartProcessInstanceCommand: 启动流程实例
   - DeleteProcessInstanceCommand: 删除流程实例
   - GetProcessInstanceCommand: 获取流程实例
   - SuspendTaskCommand: 挂起任务
   - ResumeTaskCommand: 恢复任务

3. **任务管理命令**
   - StartTaskCommand: 开始任务
   - CompleteTaskCommand: 完成任务
   - ClaimTaskCommand: 签收任务
   - CancelTaskCommand: 取消任务
   - ForwardTaskCommand: 转办任务
   - WithdrawTaskCommand: 撤回任务
   - RollbackTaskCommand: 回退任务

4. **变量管理命令**
   - GetProcessInstanceVariableCommand: 获取流程实例变量
   - SaveProcessInstanceVariablesCommand: 保存流程实例变量
   - DeleteProcessVariableCommand: 删除流程变量

5. **查询命令**
   - GetTaskCommand: 获取任务
   - GetHistoryTaskCommand: 获取历史任务
   - GetHistoryProcessInstanceCommand: 获取历史流程实例
   - QueryListCommand: 查询列表
   - QueryCountCommand: 查询数量统计

6. **其他管理命令**
   - GetExpressionValueCommand: 获取表达式值
   - GetCalendarDefCommand: 获取日历定义
   - TaskReminder 相关命令: 任务提醒管理
   - Countersign 相关命令: 会签任务管理

## 事务处理

命令模式与 Spring 事务管理集成：
- SpringTransactionCommandService 负责在统一事务中执行命令
- 保证命令执行的一致性
- 支持事务的回滚和提交

## 扩展性

Command 包设计具有良好的扩展性：
- 新增命令只需实现 Command 接口
- 可以根据需要对命令进行组合
- 支持在命令执行前后添加拦截器或监听器