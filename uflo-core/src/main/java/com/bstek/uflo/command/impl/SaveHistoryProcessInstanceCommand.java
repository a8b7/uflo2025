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

import java.util.Date;

import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.HistoryProcessInstance;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.ProcessInstanceState;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * @author Jacky.gao
 * @since 2013年7月31日
 */
public class SaveHistoryProcessInstanceCommand implements Command<HistoryProcessInstance> {
    private ProcessInstance processInstance;

    public SaveHistoryProcessInstanceCommand(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public HistoryProcessInstance execute(Context context) {
        Session session = context.getSession();
        HistoryProcessInstance hisProcessInstance = null;
        if (processInstance.getState().equals(ProcessInstanceState.Start)) {
            hisProcessInstance = new HistoryProcessInstance();
            hisProcessInstance.setId(processInstance.getHistoryProcessInstanceId());
            hisProcessInstance.setCreateDate(processInstance.getCreateDate());
            hisProcessInstance.setProcessId(processInstance.getProcessId());
            hisProcessInstance.setProcessInstanceId(processInstance.getId());
            hisProcessInstance.setTag(processInstance.getTag());
            hisProcessInstance.setBusinessId(processInstance.getBusinessId());
            hisProcessInstance.setPromoter(processInstance.getPromoter());
            hisProcessInstance.setSubject(processInstance.getSubject());
            session.save(hisProcessInstance);
        }
        if (processInstance.getState().equals(ProcessInstanceState.End)) {

            EntityManager em = null;
            try {
                em = context.getEntityManager();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<HistoryProcessInstance> query = cb.createQuery(HistoryProcessInstance.class);
                Root<HistoryProcessInstance> root = query.from(HistoryProcessInstance.class);
                query.select(root).where(cb.equal(root.get("processInstanceId"), processInstance.getId()));
                hisProcessInstance = em.createQuery(query).getSingleResult();
                hisProcessInstance.setEndDate(new Date());
                session.update(hisProcessInstance);
            } catch (Exception e) {
                // 处理未找到记录的情况
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
        }
        return hisProcessInstance;
    }

}
