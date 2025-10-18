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

package com.bstek.uflo.console.config;

import com.bstek.uflo.config.UfloProperties;
import com.bstek.uflo.console.handler.impl.calendar.CalendarServletHandler;
import com.bstek.uflo.console.handler.impl.central.CentralServletHandler;
import com.bstek.uflo.console.handler.impl.designer.DesignerServletHandler;
import com.bstek.uflo.console.handler.impl.deploy.DeployServletHandler;
import com.bstek.uflo.console.handler.impl.diagram.DefaultTaskDiagramInfoProvider;
import com.bstek.uflo.console.handler.impl.diagram.ProcessDiagramServletHandler;
import com.bstek.uflo.console.handler.impl.list.AssigneeProviderListServletHandler;
import com.bstek.uflo.console.handler.impl.list.CalendarProviderListServletHandler;
import com.bstek.uflo.console.handler.impl.list.HandlerListServletHandler;
import com.bstek.uflo.console.handler.impl.res.ResourceLoaderServletHandler;
import com.bstek.uflo.console.handler.impl.todo.TodoServletHandler;
import com.bstek.uflo.console.provider.DefaultFileProcessProvider;
import com.bstek.uflo.console.provider.ProcessProviderUtils;
import com.bstek.uflo.service.HistoryService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.TaskService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * UFLO 控制台组件配置类
 * 替代原来的 uflo-console-context.xml 配置文件
 *
 * @author Claude
 * @since 2025-10-18
 */
@Configuration
public class UfloConsoleConfiguration {

    /**
     * 资源加载 Servlet 处理器
     */
    @Bean(name = "uflo.console.resourceLoaderServletHandler")
    @ConditionalOnMissingBean(name = "uflo.console.resourceLoaderServletHandler")
    public ResourceLoaderServletHandler resourceLoaderServletHandler() {
        return new ResourceLoaderServletHandler();
    }

    /**
     * 待办事项 Servlet 处理器
     */
    @Bean(name = "uflo.console.todoServletHandler")
    @ConditionalOnMissingBean(name = "uflo.console.todoServletHandler")
    public TodoServletHandler todoServletHandler(TaskService taskService, HistoryService historyService) {
        TodoServletHandler handler = new TodoServletHandler();
        handler.setTaskService(taskService);
        handler.setHistoryService(historyService);
        return handler;
    }

    /**
     * 设计器 Servlet 处理器
     */
    @Bean(name = "uflo.designerServletHandler")
    @ConditionalOnMissingBean(name = "uflo.designerServletHandler")
    public DesignerServletHandler designerServletHandler(ProcessService processService) {
        DesignerServletHandler handler = new DesignerServletHandler();
        handler.setProcessService(processService);
        return handler;
    }

    /**
     * 中央 Servlet 处理器
     */
    @Bean(name = "uflo.console.centralServletHandler")
    @ConditionalOnMissingBean(name = "uflo.console.centralServletHandler")
    public CentralServletHandler centralServletHandler(TaskService taskService, ProcessService processService, HistoryService historyService) {
        CentralServletHandler handler = new CentralServletHandler();
        handler.setTaskService(taskService);
        handler.setProcessService(processService);
        handler.setHistoryService(historyService);
        return handler;
    }

    /**
     * 默认任务图信息提供者
     */
    @Bean(name = "uflo.console.defaultTaskDiagramInfoProvider")
    @ConditionalOnMissingBean(name = "uflo.console.defaultTaskDiagramInfoProvider")
    public DefaultTaskDiagramInfoProvider defaultTaskDiagramInfoProvider(UfloProperties properties) {
        DefaultTaskDiagramInfoProvider provider = new DefaultTaskDiagramInfoProvider();
        provider.setDisableDefaultTaskDiagramInfoProvider(properties.isDisableDefaultTaskDiagramInfoProvider());
        return provider;
    }

    /**
     * 日历 Servlet 处理器
     */
    @Bean(name = "uflo.console.calendarServletHandler")
    @ConditionalOnMissingBean(name = "uflo.console.calendarServletHandler")
    public CalendarServletHandler calendarServletHandler(com.bstek.uflo.command.CommandService commandService) {
        CalendarServletHandler handler = new CalendarServletHandler();
        handler.setCommandService(commandService);
        return handler;
    }

    /**
     * 部署 Servlet 处理器
     */
    @Bean(name = "uflo.console.deployServletHandler")
    @ConditionalOnMissingBean(name = "uflo.console.deployServletHandler")
    public DeployServletHandler deployServletHandler(ProcessService processService, UfloProperties properties) {
        DeployServletHandler handler = new DeployServletHandler();
        handler.setProcessService(processService);
        handler.setDebug(properties.isDebug());
        return handler;
    }

    /**
     * 分配提供者列表 Servlet 处理器
     */
    @Bean(name = "uflo.console.assigneeProviderListServletHandler")
    @ConditionalOnMissingBean(name = "uflo.console.assigneeProviderListServletHandler")
    public AssigneeProviderListServletHandler assigneeProviderListServletHandler(UfloProperties properties) {
        AssigneeProviderListServletHandler handler = new AssigneeProviderListServletHandler();
        handler.setDebug(properties.isDebug());
        return handler;
    }

    /**
     * 日历提供者列表 Servlet 处理器
     */
    @Bean(name = "uflo.console.calendarProviderListServletHandler")
    @ConditionalOnMissingBean(name = "uflo.console.calendarProviderListServletHandler")
    public CalendarProviderListServletHandler calendarProviderListServletHandler(UfloProperties properties) {
        CalendarProviderListServletHandler handler = new CalendarProviderListServletHandler();
        handler.setDebug(properties.isDebug());
        return handler;
    }

    /**
     * 处理器列表 Servlet 处理器
     */
    @Bean(name = "uflo.console.handlerListServletHandler")
    @ConditionalOnMissingBean(name = "uflo.console.handlerListServletHandler")
    public HandlerListServletHandler handlerListServletHandler(UfloProperties properties) {
        HandlerListServletHandler handler = new HandlerListServletHandler();
        handler.setDebug(properties.isDebug());
        return handler;
    }

    /**
     * 默认文件流程提供者
     */
    @Bean(name = "uflo.defaultFileProcessProvider")
    @ConditionalOnMissingBean(name = "uflo.defaultFileProcessProvider")
    public DefaultFileProcessProvider defaultFileProcessProvider(UfloProperties properties) {
        DefaultFileProcessProvider provider = new DefaultFileProcessProvider();
        provider.setFileStoreDir(properties.getDefaultFileStoreDir());
        provider.setDisabled(properties.isDisableDefaultFileProcessProvider());
        return provider;
    }

    /**
     * 流程提供者工具类
     */
    @Bean(name = "uflo.processProviderUtils")
    @ConditionalOnMissingBean(name = "uflo.processProviderUtils")
    public ProcessProviderUtils processProviderUtils() {
        return new ProcessProviderUtils();
    }

    /**
     * 过程图 Servlet 处理器
     */
    @Bean
    @ConditionalOnMissingBean(type = "com.bstek.uflo.console.handler.impl.diagram.ProcessDiagramServletHandler")
    public ProcessDiagramServletHandler processDiagramServletHandler(ProcessService processService, TaskService taskService, HistoryService historyService, UfloProperties properties) {
        ProcessDiagramServletHandler handler = new ProcessDiagramServletHandler();
        handler.setProcessService(processService);
        handler.setTaskService(taskService);
        handler.setHistoryService(historyService);

        // 设置图表相关属性
        handler.setShowTime(properties.getDiagramShowTime());
        handler.setPassedNodeBgcolor(properties.getDiagramPassedNodeBgcolor());
        handler.setPassedNodeFontColor(properties.getDiagramPassedNodeFontColor());
        handler.setPassedNodeFontSize(properties.getDiagramPassedNodeFontSize());
        handler.setPassedNodeBorderColor(properties.getDiagramPassedNodeBorderColor());
        handler.setPassedConnectionColor(properties.getDiagramPassedConnectionColor());
        handler.setPassedConnectionFontColor(properties.getDiagramPassedConnectionFontColor());
        handler.setPassedConnectionFontSize(properties.getDiagramPassedConnectionFontSize());
        handler.setMultiCurrentNodeBgcolor(properties.getDiagramMultiCurrentNodeBgcolor());
        handler.setMultiCurrentNodeFontColor(properties.getDiagramMultiCurrentNodeFontColor());
        handler.setMultiCurrentNodeFontSize(properties.getDiagramMultiCurrentNodeFontSize());
        handler.setMultiCurrentNodeBorderColor(properties.getDiagramMultiCurrentNodeBorderColor());
        handler.setConnectionColor(properties.getDiagramConnectionColor());
        handler.setConnectionFontColor(properties.getDiagramConnectionFontColor());
        handler.setConnectionFontSize(properties.getDiagramConnectionFontSize());
        handler.setNodeBgcolor(properties.getDiagramNodeBgcolor());
        handler.setNodeFontColor(properties.getDiagramNodeFontColor());
        handler.setNodeFontSize(properties.getDiagramNodeFontSize());
        handler.setNodeBorderColor(properties.getDiagramNodeBorderColor());
        handler.setCurrentNodeBgcolor(properties.getDiagramCurrentNodeBgcolor());
        handler.setCurrentNodeFontColor(properties.getDiagramCurrentNodeFontColor());
        handler.setCurrentNodeFontSize(properties.getDiagramCurrentNodeFontSize());
        handler.setCurrentNodeBorderColor(properties.getDiagramCurrentNodeBorderColor());

        return handler;
    }
}