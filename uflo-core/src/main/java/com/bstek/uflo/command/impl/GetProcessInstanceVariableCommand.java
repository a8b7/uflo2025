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

import org.hibernate.Session;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.variable.BlobVariable;
import com.bstek.uflo.model.variable.TextVariable;
import com.bstek.uflo.model.variable.Variable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * @author Jacky.gao
 * @since 2013年8月2日
 */
public class GetProcessInstanceVariableCommand implements Command<Variable> {
    private ProcessInstance processInstance;
    private String key;

    public GetProcessInstanceVariableCommand(String key, ProcessInstance processInstance) {
        this.processInstance = processInstance;
        this.key = key;
    }

    public Variable execute(Context context) {
        return getVariable(context, processInstance);
    }

    private Variable getVariable(Context context, ProcessInstance pi) {
        Session session = context.getSession();
        EntityManager em = null;
        try {
            em = context.getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Variable> query = cb.createQuery(Variable.class);
            Root<Variable> root = query.from(Variable.class);
            query.select(root).where(
                    cb.and(
                            cb.equal(root.get("processInstanceId"), pi.getId()),
                            cb.equal(root.get("key"), key)
                    )
            );
            List<Variable> vars = em.createQuery(query).getResultList();
            if (vars.size() == 0) {
                if (pi.getParentId() > 0) {
                    ProcessInstance parentInstance = (ProcessInstance) session.get(ProcessInstance.class, pi.getParentId());
                    return getVariable(context, parentInstance);
                } else {
                    return null;
                }
            } else {
                for (Variable var : vars) {
                    if (var instanceof BlobVariable) {
                        ((BlobVariable) var).initValue(context);
                    }
                    if (var instanceof TextVariable) {
                        ((TextVariable) var).initValue(context);
                    }
                }
                return vars.get(0);
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
