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
package com.bstek.uflo.utils;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.ModernAcquireDbidCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 现代化的ID生成器
 * 使用Spring Boot的依赖注入和配置管理，不再实现ApplicationContextAware
 *
 * @author Claude
 * @since 2023-01-01
 */
@Component
public class ModernIDGenerator {

    private static final Log log = LogFactory.getLog(ModernIDGenerator.class);
    private static final Random random = new Random();

    @Autowired
    private CommandService commandService;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${uflo.idBlockSize:5000}")
    private int blockSize;

    @Value("${uflo.idMaxAttempts:5}")
    private int maxAttempts;

    private long nextId;
    private long lastId = -1;

    /**
     * 生成下一个ID
     * 不再需要事务注解，由CommandService统一管理事务
     */
    public synchronized long nextId() {
        if (lastId < nextId) {
            for (int attempts = maxAttempts; attempts > 0; attempts--) {
                try {
                    ModernAcquireDbidCommand command = applicationContext.getBean(ModernAcquireDbidCommand.class);
                    nextId = commandService.executeCommandInNewTransaction(command);
                    lastId = nextId + blockSize - 1;
                    break;
                } catch (StaleStateException e) {
                    attempts--;
                    if (attempts == 0) {
                        throw new IllegalStateException("couldn't acquire block of ids, tried " + maxAttempts + " times");
                    }
                    // 如果还有尝试次数，先等待一段时间
                    int millis = 20 + random.nextInt(200);
                    log.debug("optimistic locking failure while trying to acquire id block.  retrying in " + millis + " millis");
                    try {
                        Thread.sleep(millis);
                    } catch (InterruptedException e1) {
                        log.debug("waiting after id block locking failure got interrupted");
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return nextId++;
    }

    /**
     * 获取实例（向后兼容）
     */
    public static ModernIDGenerator getInstance() {
        // 在Spring Boot环境中，通过依赖注入获取实例
        // 这个方法主要用于向后兼容
        throw new UnsupportedOperationException("Use dependency injection to get ModernIDGenerator instance");
    }

    // Getters and Setters
    public void setCommandService(CommandService commandService) {
        this.commandService = commandService;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }
}