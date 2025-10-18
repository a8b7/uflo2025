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

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.service.ModernCommandService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * UFLO工作流引擎自动配置类
 *
 * @author Claude
 * @since 2023-01-01
 */
@AutoConfiguration
@EnableConfigurationProperties(UfloProperties.class)
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.bstek.uflo.repository")
@ComponentScan(basePackages = "com.bstek.uflo")
@Import(UfloCoreConfiguration.class)
public class UfloAutoConfiguration {

    

    /**
     * 现代化的命令服务，使用Spring事务管理
     * 作为主要的CommandService Bean，避免与SpringTransactionCommandService的循环依赖
     */
    @Bean(name = "uflo.commandService")
    @ConditionalOnMissingBean(CommandService.class)
    public ModernCommandService ufloCommandService() {
        return new ModernCommandService();
    }


}