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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessDefinition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * @author Jacky.gao
 * @since 2013年8月3日
 */
public class DeployProcessCommand implements Command<ProcessDefinition> {
    private ProcessDefinition process;
    private boolean update = false;

    public DeployProcessCommand(ProcessDefinition process, boolean update) {
        this.process = process;
        this.update = update;
    }

    public ProcessDefinition execute(Context context) {
        Session session = context.getSession();
        EntityManager em = null;
        try {
            em = context.getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProcessDefinition> query = cb.createQuery(ProcessDefinition.class);
            Root<ProcessDefinition> root = query.from(ProcessDefinition.class);

            String key = process.getKey();
            if (!update && StringUtils.isNotEmpty(key)) {
                query.select(root).where(cb.equal(root.get("key"), key));
                int size = em.createQuery(query).getResultList().size();
                if (size > 0) {
                    throw new IllegalArgumentException("Process definition " + process.getName() + "'s key " + key + " is not the only one!");
                }
            }
            int newVersion = 1;
            if (!update) {
                CriteriaQuery<ProcessDefinition> versionQuery = cb.createQuery(ProcessDefinition.class);
                Root<ProcessDefinition> versionRoot = versionQuery.from(ProcessDefinition.class);
                versionQuery.select(versionRoot).where(cb.equal(versionRoot.get("name"), process.getName()));
                versionQuery.orderBy(cb.desc(versionRoot.get("version")));

                List<ProcessDefinition> processes = em.createQuery(versionQuery).getResultList();
                if (processes.size() > 0) {
                    newVersion = processes.get(0).getVersion() + 1;
                    process.setVersion(newVersion);
                } else {
                    process.setVersion(newVersion);
                }
            }
            if (StringUtils.isEmpty(key)) {
                key = process.getName() + "-" + newVersion;
                process.setKey(key);
            }
            if (update) {
                session.update(process);
            } else {
                session.save(process);
            }
            return process;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
