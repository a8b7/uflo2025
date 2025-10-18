package com.bstek.uflo.env.impl;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.expr.ExpressionContext;
import com.bstek.uflo.service.IdentityService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.TaskService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

/**
 * 现代化的上下文实现
 * 使用JPA EntityManager作为主要数据访问接口，向后兼容Session
 *
 * @author Claude
 * @since 2023-01-01
 */
public class ModernContextImpl implements Context {

    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;
    private CommandService commandService;
    private ApplicationContext applicationContext;
    private ProcessService processService;
    private TaskService taskService;
    private ExpressionContext expressionContext;
    private IdentityService identityService;

    @Override
    public Session getSession() {
        // 向后兼容，从EntityManager获取Hibernate Session
        return entityManager.unwrap(Session.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        }
        return sessionFactory;
    }

    @Override
    public CommandService getCommandService() {
        return commandService;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public ProcessService getProcessService() {
        return processService;
    }

    @Override
    public TaskService getTaskService() {
        return taskService;
    }

    @Override
    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    @Override
    public IdentityService getIdentityService() {
        return identityService;
    }

    // Setters
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setCommandService(CommandService commandService) {
        this.commandService = commandService;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setProcessService(ProcessService processService) {
        this.processService = processService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }
}