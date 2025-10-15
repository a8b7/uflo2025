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

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.HistoryActivity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * @author Jacky.gao
 * @since 2013年9月12日
 */
public class GetHistoryActivitiyCommand implements Command<List<HistoryActivity>> {
    private long instanceId;
    private boolean isProcessInstanceId;

    public GetHistoryActivitiyCommand(long instanceId, boolean isProcessInstanceId) {
        this.instanceId = instanceId;
        this.isProcessInstanceId = isProcessInstanceId;
    }

    public List<HistoryActivity> execute(Context context) {
        EntityManager em = null;
        try {
            em = context.getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<HistoryActivity> query = cb.createQuery(HistoryActivity.class);
            Root<HistoryActivity> root = query.from(HistoryActivity.class);

            if (isProcessInstanceId) {
                query.select(root).where(cb.equal(root.get("rootProcessInstanceId"), instanceId));
            } else {
                query.select(root).where(cb.equal(root.get("historyProcessInstanceId"), instanceId));
            }
            query.orderBy(cb.desc(root.get("endDate")));
            return em.createQuery(query).getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
