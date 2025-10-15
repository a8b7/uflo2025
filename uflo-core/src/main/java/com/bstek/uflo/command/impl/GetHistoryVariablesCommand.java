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
import com.bstek.uflo.model.HistoryVariable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * @author Jacky.gao
 * @since 2013年9月28日
 */
public class GetHistoryVariablesCommand implements Command<List<HistoryVariable>> {
	private long historyProcessInstanceId;
	public GetHistoryVariablesCommand(long historyProcessInstanceId){
		this.historyProcessInstanceId=historyProcessInstanceId;
	}
	public List<HistoryVariable> execute(Context context) {
		EntityManager em = null;
		try {
			em = context.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HistoryVariable> query = cb.createQuery(HistoryVariable.class);
		Root<HistoryVariable> root = query.from(HistoryVariable.class);
		query.select(root).where(cb.equal(root.get("historyProcessInstanceId"), historyProcessInstanceId));
		List<HistoryVariable> vars = em.createQuery(query).getResultList();
		for(HistoryVariable var:vars){
			var.init(context);
		}
		return vars;
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}
}
