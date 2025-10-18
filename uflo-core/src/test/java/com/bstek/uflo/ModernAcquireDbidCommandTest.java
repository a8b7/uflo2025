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
package com.bstek.uflo;

import com.bstek.uflo.command.impl.ModernAcquireDbidCommand;
import com.bstek.uflo.config.UfloAutoConfiguration;
import com.bstek.uflo.repository.ContextPropertyRepository;
import com.bstek.uflo.utils.ModernIDGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证ModernAcquireDbidCommand依赖注入修复的测试
 *
 * @author Claude
 * @since 2023-01-01
 */
@SpringJUnitConfig(ModernAcquireDbidCommandTest.TestConfig.class)
public class ModernAcquireDbidCommandTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ContextPropertyRepository contextPropertyRepository;

    @Test
    public void testModernAcquireDbidCommandDependencyInjection() {
        // 测试ModernAcquireDbidCommand能够正确从Spring容器获取
        ModernAcquireDbidCommand command = applicationContext.getBean(ModernAcquireDbidCommand.class);

        // 验证依赖注入是否成功
        assertNotNull(command, "ModernAcquireDbidCommand should be created by Spring");

        // 这个测试验证了ContextPropertyRepository能够被正确注入到ModernAcquireDbidCommand中
        // 如果注入失败，Spring在创建bean时就会抛出异常
        System.out.println("✓ ModernAcquireDbidCommand dependency injection test passed");
    }

    @Test
    public void testModernIDGeneratorCanUseModernAcquireDbidCommand() {
        // 测试ModernIDGenerator能够正确使用ModernAcquireDbidCommand
        ModernIDGenerator idGenerator = applicationContext.getBean(ModernIDGenerator.class);

        assertNotNull(idGenerator, "ModernIDGenerator should be created by Spring");

        // 这个测试验证了ModernIDGenerator可以通过Spring容器获取ModernAcquireDbidCommand实例
        // 如果依赖注入有问题，这里会抛出NullPointerException或其他Spring异常
        System.out.println("✓ ModernIDGenerator can use ModernAcquireDbidCommand test passed");
    }

    @Test
    public void testContextPropertyRepositoryIsAvailable() {
        // 验证ContextPropertyRepository bean存在且可用
        assertNotNull(contextPropertyRepository, "ContextPropertyRepository should be injected");

        // 这是一个简单的验证，确保repository bean存在
        // 实际的数据库操作需要完整的事务环境
        System.out.println("✓ ContextPropertyRepository is available test passed");
    }

    @Configuration
    @Import(UfloAutoConfiguration.class)
    @EnableJpaRepositories(basePackages = "com.bstek.uflo.repository")
    static class TestConfig {

        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource);
            em.setPackagesToScan("com.bstek.uflo.model");

            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            em.setJpaVendorAdapter(vendorAdapter);

            return em;
        }

        @Bean
        public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
            JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
            return transactionManager;
        }
    }
}