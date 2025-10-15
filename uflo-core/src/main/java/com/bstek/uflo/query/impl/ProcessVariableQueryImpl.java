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

import com.bstek.uflo.model.ProcessInstance;
import org.apache.commons.lang.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.QueryCountCommand;
import com.bstek.uflo.command.impl.QueryListCommand;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.query.ProcessVariableQuery;
import com.bstek.uflo.query.QueryJob;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class ProcessVariableQueryImpl implements ProcessVariableQuery, QueryJob<Variable> {
	private long processInstanceId;
	private long rootProcessInstanceId;
	private String key;
	private int firstResult;
	private int maxResults;
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private CommandService commandService;
	public ProcessVariableQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}
	public List<Variable> list() {
		return (List<Variable>) commandService.executeCommand(new QueryListCommand(this));
	}
	
	public int count() {
		return commandService.executeCommand(new QueryCountCommand(this));
	}
	@SuppressWarnings("unchecked")
	public CriteriaQuery<Variable> buildQuery(EntityManager em, boolean queryCount) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Variable> query = cb.createQuery(Variable.class);

		Root<Variable> root = query.from(Variable.class);
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
	@Override
	public CriteriaQuery<Long> buildCountQuery(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Variable> root = countQuery.from(Variable.class);
		countQuery.select(cb.count(root));
		List<Predicate> predicates = buildPredicates(cb, root);
		if (!predicates.isEmpty()) {
			countQuery.where(predicates.toArray(new Predicate[0]));
		}
		return countQuery;
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Variable> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (processInstanceId > 0) {
			predicates.add(cb.equal(root.get("processInstanceId"), processInstanceId));
		}
		if (rootProcessInstanceId > 0) {
			predicates.add(cb.equal(root.get("rootProcessInstanceId"), rootProcessInstanceId));
		}
		if (StringUtils.isNotEmpty(key)) {
			predicates.add(cb.equal(root.get("key"), key));
		}

		return predicates;
	}

	private List<jakarta.persistence.criteria.Order> buildOrders(CriteriaBuilder cb, Root<Variable> root) {
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
	
	
	public ProcessVariableQuery addOrderAsc(String property){
		ascOrders.add(property);
		return this;
	}

	public ProcessVariableQuery addOrderDesc(String property){
		descOrders.add(property);
		return this;
	}
	
	public ProcessVariableQuery processInstanceId(long processInstanceId) {
		this.processInstanceId=processInstanceId;
		return this;
	}

	public ProcessVariableQuery rootprocessInstanceId(long rootProcessInstanceId) {
		this.rootProcessInstanceId=rootProcessInstanceId;
		return this;
	}

	public ProcessVariableQuery key(String key) {
		this.key=key;
		return this;
	}

	public ProcessVariableQuery page(int firstResult, int maxResults) {
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}


}
