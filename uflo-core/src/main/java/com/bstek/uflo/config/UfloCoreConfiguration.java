/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package com.bstek.uflo.config;

import com.bstek.uflo.UfloPropertyPlaceholderConfigurer;
import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.service.ModernCommandService;
import com.bstek.uflo.deploy.impl.DefaultProcessDeployer;
import com.bstek.uflo.deploy.validate.impl.ProcessValidator;
import com.bstek.uflo.env.impl.MemoryProcessCache;
import com.bstek.uflo.expr.impl.ExpressionContextImpl;
import com.bstek.uflo.heartbeat.InstanceDetection;
import com.bstek.uflo.process.assign.impl.DeptAssigneeProvider;
import com.bstek.uflo.process.assign.impl.UserAssigneeProvider;
import com.bstek.uflo.process.node.calendar.BusinessCalendar;
import com.bstek.uflo.process.node.reminder.impl.UfloCalendarProvider;
import com.bstek.uflo.service.impl.*;
import com.bstek.uflo.utils.EnvironmentUtils;
import com.bstek.uflo.utils.IDGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import jakarta.persistence.EntityManagerFactory;

/**
 * UFLO核心组件配置类
 * 替代原来的uflo-context-configs.xml配置文件
 *
 * @author Claude
 * @since 2025-10-18
 */
@Configuration
public class UfloCoreConfiguration {

    /**
     * 流程验证器
     */
    @Bean(name = "uflo.processValidator")
    @ConditionalOnMissingBean(name = "uflo.processValidator")
    public ProcessValidator processValidator() {
        return new ProcessValidator();
    }

    /**
     * 环境工具类
     */
    @Bean(name = "uflo.environmentUtils")
    @ConditionalOnMissingBean(name = "uflo.environmentUtils")
    public EnvironmentUtils environmentUtils() {
        return new EnvironmentUtils();
    }

    /**
     * 流程缓存
     */
    @Bean(name = "uflo.processCache")
    @ConditionalOnMissingBean(name = "uflo.processCache")
    public MemoryProcessCache processCache() {
        return new MemoryProcessCache();
    }

    /**
     * 命令服务 - 使用现代化的Spring事务管理
     * 移除Bean定义，由UfloAutoConfiguration中的ModernCommandService提供
     */

    /**
     * 日历服务
     */
    @Bean(name = "uflo.calendarService")
    @ConditionalOnMissingBean(name = "uflo.calendarService")
    public CalendarServiceImpl calendarService(CommandService commandService) {
        CalendarServiceImpl calendarService = new CalendarServiceImpl();
        calendarService.setCommandService(commandService);
        return calendarService;
    }

    /**
     * UFLO日历提供者
     */
    @Bean(name = "uflo.ufloCalendarProvider")
    @ConditionalOnMissingBean(name = "uflo.ufloCalendarProvider")
    public UfloCalendarProvider ufloCalendarProvider(CalendarServiceImpl calendarService) {
        UfloCalendarProvider calendarProvider = new UfloCalendarProvider();
        calendarProvider.setCalendarService(calendarService);
        return calendarProvider;
    }

    /**
     * 属性占位符配置器
     */
    @Bean(name = "uflo.props")
    @ConditionalOnMissingBean(name = "uflo.props")
    public UfloPropertyPlaceholderConfigurer props() {
        UfloPropertyPlaceholderConfigurer configurer = new UfloPropertyPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    /**
     * 流程部署器
     */
    @Bean(name = "uflo.processDeployer")
    @ConditionalOnMissingBean(name = "uflo.processDeployer")
    public DefaultProcessDeployer processDeployer(CommandService commandService, ProcessValidator processValidator) {
        DefaultProcessDeployer deployer = new DefaultProcessDeployer();
        deployer.setCommandService(commandService);
        deployer.setProcessValidator(processValidator);
        return deployer;
    }

    /**
     * 业务日历
     */
    @Bean(name = "uflo.businessCalendar")
    @ConditionalOnMissingBean(name = "uflo.businessCalendar")
    public BusinessCalendar businessCalendar(UfloProperties properties) {
        BusinessCalendar businessCalendar = new BusinessCalendar();
        businessCalendar.setBusinessDayHours(properties.getBusinessDayHours());
        return businessCalendar;
    }

    /**
     * 流程服务
     */
    @Bean(name = "uflo.processService")
    @ConditionalOnMissingBean(name = "uflo.processService")
    public DefaultProcessService processService(CommandService commandService, DefaultProcessDeployer processDeployer) {
        DefaultProcessService processService = new DefaultProcessService();
        processService.setCommandService(commandService);
        processService.setProcessDeployer(processDeployer);
        return processService;
    }

    /**
     * 任务服务
     */
    @Bean(name = "uflo.taskService")
    @ConditionalOnMissingBean(name = "uflo.taskService")
    public DefaultTaskService taskService(CommandService commandService, DefaultProcessService processService, DefaultHistoryService historyService) {
        DefaultTaskService taskService = new DefaultTaskService();
        taskService.setCommandService(commandService);
        taskService.setProcessService(processService);
        taskService.setHistoryService(historyService);
        return taskService;
    }

    /**
     * 历史服务
     */
    @Bean(name = "uflo.historyService")
    @ConditionalOnMissingBean(name = "uflo.historyService")
    public DefaultHistoryService historyService(CommandService commandService) {
        DefaultHistoryService historyService = new DefaultHistoryService();
        historyService.setCommandService(commandService);
        return historyService;
    }

    /**
     * 表达式上下文
     */
    @Bean(name = "uflo.expressionContext")
    @ConditionalOnMissingBean(name = "uflo.expressionContext")
    public ExpressionContextImpl expressionContext(EnvironmentUtils environmentUtils, DefaultProcessService processService) {
        ExpressionContextImpl expressionContext = new ExpressionContextImpl();
        expressionContext.setProcessService(processService);
        return expressionContext;
    }

    /**
     * 身份服务
     */
    @Bean(name = "uflo.identityService")
    @ConditionalOnMissingBean(name = "uflo.identityService")
    public DefaultIdentityService identityService() {
        return new DefaultIdentityService();
    }

    /**
     * 部门分配提供者
     */
    @Bean(name = "uflo.deptAssigneeProvider")
    @ConditionalOnMissingBean(name = "uflo.deptAssigneeProvider")
    public DeptAssigneeProvider deptAssigneeProvider(DefaultIdentityService identityService, UfloProperties properties) {
        DeptAssigneeProvider deptAssigneeProvider = new DeptAssigneeProvider();
        deptAssigneeProvider.setIdentityService(identityService);
        deptAssigneeProvider.setDisabledDeptAssigneeProvider(properties.isDisabledDeptAssigneeProvider());
        return deptAssigneeProvider;
    }

    /**
     * 用户分配提供者
     */
    @Bean(name = "uflo.userAssigneeProvider")
    @ConditionalOnMissingBean(name = "uflo.userAssigneeProvider")
    public UserAssigneeProvider userAssigneeProvider(DefaultIdentityService identityService, UfloProperties properties) {
        UserAssigneeProvider userAssigneeProvider = new UserAssigneeProvider();
        userAssigneeProvider.setIdentityService(identityService);
        userAssigneeProvider.setDisabledUserAssigneeProvider(properties.isDisabledUserAssigneeProvider());
        return userAssigneeProvider;
    }

    /**
     * 调度服务
     */
    @Bean(name = "uflo.schedulerService")
    @ConditionalOnMissingBean(name = "uflo.schedulerService")
    public SchedulerServiceImpl schedulerService(DefaultTaskService taskService, DefaultProcessService processService,
                                                 UfloProperties properties) {
        SchedulerServiceImpl schedulerService = new SchedulerServiceImpl();
        schedulerService.setTaskService(taskService);
        schedulerService.setProcessService(processService);
        schedulerService.setThreadCount(properties.getJobThreadCount());
        schedulerService.setMakeSchedulerThreadDaemon(properties.isMakeSchedulerThreadDaemon());
        schedulerService.setEnableScanReminderJob(properties.isEnableScanReminderJob());
        return schedulerService;
    }

    /**
     * 实例检测
     */
    @Bean(name = "uflo.instanceDetection")
    @ConditionalOnMissingBean(name = "uflo.instanceDetection")
    public InstanceDetection instanceDetection(EnvironmentUtils environmentUtils, SchedulerServiceImpl schedulerService,
                                               UfloProperties properties) {
        InstanceDetection instanceDetection = new InstanceDetection();
        instanceDetection.setDisableScheduler(properties.isDisableScheduler());
        instanceDetection.setInstanceNames(properties.getClusterInstanceNames());
        instanceDetection.setSchedulerService(schedulerService);
        return instanceDetection;
    }

    /**
     * ID生成器
     */
    @Bean(name = "uflo.idGenerator")
    @ConditionalOnMissingBean(name = "uflo.idGenerator")
    public IDGenerator idGenerator(CommandService commandService, UfloProperties properties) {
        IDGenerator idGenerator = new IDGenerator();
        idGenerator.setCommandService(commandService);
        idGenerator.setBlockSize(properties.getIdBlockSize());
        return idGenerator;
    }

    // 解析器相关 Bean
    @Bean(name = "uflo.processParser")
    @ConditionalOnMissingBean(name = "uflo.processParser")
    public com.bstek.uflo.deploy.parse.impl.ProcessParser processParser() {
        return new com.bstek.uflo.deploy.parse.impl.ProcessParser();
    }

    @Bean(name = "uflo.startParser")
    @ConditionalOnMissingBean(name = "uflo.startParser")
    public com.bstek.uflo.deploy.parse.impl.StartParser startParser() {
        return new com.bstek.uflo.deploy.parse.impl.StartParser();
    }

    @Bean(name = "uflo.taskParser")
    @ConditionalOnMissingBean(name = "uflo.taskParser")
    public com.bstek.uflo.deploy.parse.impl.TaskParser taskParser() {
        return new com.bstek.uflo.deploy.parse.impl.TaskParser();
    }

    @Bean(name = "uflo.foreachParser")
    @ConditionalOnMissingBean(name = "uflo.foreachParser")
    public com.bstek.uflo.deploy.parse.impl.ForeachParser foreachParser() {
        return new com.bstek.uflo.deploy.parse.impl.ForeachParser();
    }

    @Bean(name = "uflo.decisionParser")
    @ConditionalOnMissingBean(name = "uflo.decisionParser")
    public com.bstek.uflo.deploy.parse.impl.DecisionParser decisionParser() {
        return new com.bstek.uflo.deploy.parse.impl.DecisionParser();
    }

    @Bean(name = "uflo.actionParser")
    @ConditionalOnMissingBean(name = "uflo.actionParser")
    public com.bstek.uflo.deploy.parse.impl.ActionParser actionParser() {
        return new com.bstek.uflo.deploy.parse.impl.ActionParser();
    }

    @Bean(name = "uflo.swimlaneParser")
    @ConditionalOnMissingBean(name = "uflo.swimlaneParser")
    public com.bstek.uflo.deploy.parse.impl.SwimlaneParser swimlaneParser() {
        return new com.bstek.uflo.deploy.parse.impl.SwimlaneParser();
    }

    @Bean(name = "uflo.subprocessParser")
    @ConditionalOnMissingBean(name = "uflo.subprocessParser")
    public com.bstek.uflo.deploy.parse.impl.SubprocessParser subprocessParser() {
        return new com.bstek.uflo.deploy.parse.impl.SubprocessParser();
    }

    @Bean(name = "uflo.joinParser")
    @ConditionalOnMissingBean(name = "uflo.joinParser")
    public com.bstek.uflo.deploy.parse.impl.JoinParser joinParser() {
        return new com.bstek.uflo.deploy.parse.impl.JoinParser();
    }

    @Bean(name = "uflo.endParser")
    @ConditionalOnMissingBean(name = "uflo.endParser")
    public com.bstek.uflo.deploy.parse.impl.EndParser endParser() {
        return new com.bstek.uflo.deploy.parse.impl.EndParser();
    }

    @Bean(name = "uflo.sequenceFlowParser")
    @ConditionalOnMissingBean(name = "uflo.sequenceFlowParser")
    public com.bstek.uflo.deploy.parse.impl.SequenceFlowParser sequenceFlowParser() {
        return new com.bstek.uflo.deploy.parse.impl.SequenceFlowParser();
    }

    @Bean(name = "uflo.forkParser")
    @ConditionalOnMissingBean(name = "uflo.forkParser")
    public com.bstek.uflo.deploy.parse.impl.ForkParser forkParser() {
        return new com.bstek.uflo.deploy.parse.impl.ForkParser();
    }
}