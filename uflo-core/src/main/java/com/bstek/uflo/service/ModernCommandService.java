package com.bstek.uflo.service;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.env.impl.ModernContextImpl;
import com.bstek.uflo.expr.ExpressionContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
// import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 现代化的命令服务实现
 * 使用Spring Boot的声明式事务管理，不再手动管理事务
 *
 * @author Claude
 * @since 2023-01-01
 */
public class ModernCommandService implements CommandService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 在当前事务中执行命令
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public <T> T executeCommand(Command<T> command) {
        Context context = createCommandContext();
        return command.execute(context);
    }

    /**
     * 在新事务中执行命令
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T executeCommandInNewTransaction(Command<T> command) {
        Context context = createCommandContext();
        return command.execute(context);
    }

    /**
     * 创建命令执行上下文
     */
    private Context createCommandContext() {
        ModernContextImpl context = new ModernContextImpl();
        context.setEntityManager(entityManager);
        context.setEntityManagerFactory(entityManagerFactory);
        context.setCommandService(this);
        context.setApplicationContext(applicationContext);

        // 注入依赖服务
        try {
            context.setProcessService((ProcessService) applicationContext.getBean(ProcessService.BEAN_ID));
            context.setTaskService((TaskService) applicationContext.getBean(TaskService.BEAN_ID));
            context.setExpressionContext((ExpressionContext) applicationContext.getBean(ExpressionContext.BEAN_ID));
            context.setIdentityService((IdentityService) applicationContext.getBean(IdentityService.BEAN_ID));
        } catch (Exception e) {
            // 处理依赖注入失败的情况
            throw new RuntimeException("Failed to inject UFLO services", e);
        }

        return context;
    }
}