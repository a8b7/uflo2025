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
package com.bstek.uflo.repository;

import com.bstek.uflo.model.ContextProperty;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 上下文属性数据访问层
 * 使用Spring Data JPA，提供现代化的数据访问接口
 *
 * @author Claude
 * @since 2023-01-01
 */
@Repository
public interface ContextPropertyRepository extends JpaRepository<ContextProperty, Long> {

    /**
     * 根据键查找属性，使用悲观锁
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ContextProperty p WHERE p.key = :key")
    ContextProperty findByKeyWithLock(@Param("key") String key);

    /**
     * 根据键查找属性
     */
    ContextProperty findByKey(String key);

    /**
     * 检查键是否存在
     */
    boolean existsByKey(String key);
}