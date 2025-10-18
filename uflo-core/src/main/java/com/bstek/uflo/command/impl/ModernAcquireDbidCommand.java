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
package com.bstek.uflo.command.impl;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ContextProperty;
import com.bstek.uflo.repository.ContextPropertyRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 现代化的数据库ID获取命令
 * 使用JPA Repository，不再直接操作Hibernate Session
 *
 * @author Claude
 * @since 2023-01-01
 */
@Component
@Scope("prototype")
public class ModernAcquireDbidCommand implements Command<Long> {

    private static final String ID_KEY = "dbid";
    private final int blockSize;

    @Autowired
    private ContextPropertyRepository contextPropertyRepository;

    public ModernAcquireDbidCommand() {
        this(5000); // 默认块大小
    }

    public ModernAcquireDbidCommand(int blockSize) {
        this.blockSize = blockSize;
    }

    /**
     * 执行命令，获取数据库ID块
     * 事务由ModernCommandService管理，这里不需要事务注解
     */
    @Override
    public Long execute(Context context) {
        long nextId = 0;

        // 使用JPA Repository查询，自动处理锁和事务
        ContextProperty property = contextPropertyRepository.findByKeyWithLock(ID_KEY);

        if (property != null) {
            // 更新现有记录
            nextId = Long.parseLong(property.getValue());
            property.setValue(String.valueOf(nextId + blockSize));
            contextPropertyRepository.save(property);
        } else {
            // 创建新记录
            property = new ContextProperty();
            property.setKey(ID_KEY);
            property.setValue(String.valueOf(blockSize));
            contextPropertyRepository.save(property);
            nextId = 0;
        }

        return nextId + 1;
    }

    public int getBlockSize() {
        return blockSize;
    }
}