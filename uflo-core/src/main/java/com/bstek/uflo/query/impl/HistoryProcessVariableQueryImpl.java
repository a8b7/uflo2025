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
package com.bstek.uflo.query.impl;

import java.util.ArrayList;
import java.util.List;

import com.bstek.uflo.model.HistoryProcessInstance;
import org.apache.commons.lang.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.QueryCountCommand;
import com.bstek.uflo.command.impl.QueryListCommand;
import com.bstek.uflo.model.HistoryVariable;
import com.bstek.uflo.query.HistoryProcessVariableQuery;
import com.bstek.uflo.query.QueryJob;

/**
 * @author Jacky.gao
 * @since 2013年11月23日
 */
public class HistoryProcessVariableQueryImpl implements QueryJob<HistoryVariable>,HistoryProcessVariableQuery {
	private long historyProcessInstanceId;
	private String key;
	private int firstResult;
	private int maxResults;
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private CommandService commandService;
	
	public HistoryProcessVariableQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}
	@SuppressWarnings("unchecked")
	public CriteriaQuery<HistoryVariable> buildQuery(EntityManager em, boolean queryCount) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HistoryVariable> query = cb.createQuery(HistoryVariable.class);

		Root<HistoryVariable> root = query.from(HistoryVariable.class);

		query.select(root);

		List<Predicate> predicates = buildPredicates(cb, root);
		if (!predicates.isEmpty()) {
			query.where(predicates.toArray(new Predicate[0]));
		}

		if (!queryCount) {
			List<jakarta.persistence.criteria.Order> orders = buildOrders(cb, root);
			if (!orders.isEmpty()) {
				query.orderBy(orders);
			}
		}

		return query;
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<HistoryVariable> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (historyProcessInstanceId > 0) {
			predicates.add(cb.equal(root.get("historyProcessInstanceId"), historyProcessInstanceId));
		}
		if (StringUtils.isNotEmpty(key)) {
			predicates.add(cb.equal(root.get("key"), key));
		}

		return predicates;
	}

	private List<jakarta.persistence.criteria.Order> buildOrders(CriteriaBuilder cb, Root<HistoryVariable> root) {
		List<jakarta.persistence.criteria.Order> orders = new ArrayList<>();

		for (String ascProperty : ascOrders) {
			orders.add(cb.asc(root.get(ascProperty)));
		}
		for (String descProperty : descOrders) {
			orders.add(cb.desc(root.get(descProperty)));
		}

		return orders;
	}

	@Override
	public int getFirstResult() {
		return firstResult;
	}

	@Override
	public int getMaxResults() {
		return maxResults;
	}
	
	public List<HistoryVariable> list() {
		return (List<HistoryVariable>) commandService.executeCommand(new QueryListCommand(this));
	}

	public int count() {
		return commandService.executeCommand(new QueryCountCommand(this));
	}

	public HistoryProcessVariableQuery historyProcessInstanceId(long historyProcessInstanceId) {
		this.historyProcessInstanceId=historyProcessInstanceId;
		return this;
	}

	public HistoryProcessVariableQuery key(String key) {
		this.key=key;
		return this;
	}

	public HistoryProcessVariableQuery page(int firstResult, int maxResults) {
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}

	public HistoryProcessVariableQuery addOrderAsc(String property) {
		ascOrders.add(property);
		return this;
	}

	public HistoryProcessVariableQuery addOrderDesc(String property) {
		descOrders.add(property);
		return this;
	}
	@Override
	public CriteriaQuery<Long> buildCountQuery(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);

		Root<HistoryVariable> root = query.from(HistoryVariable.class);

		query.select(cb.count(root));
		List<Predicate> predicates = buildPredicates(cb, root);
		if (!predicates.isEmpty()) {
			query.where(predicates.toArray(new Predicate[0]));

		}

		return query;
	}
}
