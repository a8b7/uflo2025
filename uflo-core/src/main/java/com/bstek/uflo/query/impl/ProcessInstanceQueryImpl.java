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
import java.util.Date;
import java.util.List;

import com.bstek.uflo.model.HistoryVariable;
import org.apache.commons.lang.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.QueryCountCommand;
import com.bstek.uflo.command.impl.QueryListCommand;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.query.ProcessInstanceQuery;
import com.bstek.uflo.query.QueryJob;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class ProcessInstanceQueryImpl implements ProcessInstanceQuery,QueryJob<ProcessInstance> {
	private long processId;
	private long parentId=-1;
	private long rootId=-1;
	private int firstResult;
	private int maxResults;
	private String businessId;
	private String promoter;
	private Date createDateLessThen;
	private Date createDateLessThenOrEquals;
	private Date createDateGreaterThen;
	private Date createDateGreaterThenOrEquals;
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private CommandService commandService;
	public ProcessInstanceQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}
	@Override
	public CriteriaQuery<Long> buildCountQuery(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);

		Root<ProcessInstance> root = query.from(ProcessInstance.class);

		query.select(cb.count(root));
		List<Predicate> predicates = buildPredicates(cb, root);
		if (!predicates.isEmpty()) {
			query.where(predicates.toArray(new Predicate[0]));

		}

		return query;
	}
	@SuppressWarnings("unchecked")
	public CriteriaQuery<ProcessInstance> buildQuery(EntityManager em, boolean queryCount) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProcessInstance> query = cb.createQuery(ProcessInstance.class);

		Root<ProcessInstance> root = query.from(ProcessInstance.class);

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



	public List<ProcessInstance> list(){
		return (List<ProcessInstance>) commandService.executeCommand(new QueryListCommand(this));
	}
	
	public ProcessInstanceQuery promoter(String promoter) {
		this.promoter=promoter;
		return this;
	}
	
	public int count(){
		return commandService.executeCommand(new QueryCountCommand(this));		
	}
	
	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ProcessInstance> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (processId > 0) {
			predicates.add(cb.equal(root.get("processId"), processId));
		}
		if (parentId > -1) {
			predicates.add(cb.equal(root.get("parentId"), parentId));
		}
		if (rootId > -1) {
			predicates.add(cb.equal(root.get("rootId"), rootId));
		}
		if (StringUtils.isNotEmpty(businessId)) {
			predicates.add(cb.equal(root.get("businessId"), businessId));
		}
		if (StringUtils.isNotEmpty(promoter)) {
			predicates.add(cb.equal(root.get("promoter"), promoter));
		}
		if (createDateLessThen != null) {
			predicates.add(cb.lessThan(root.get("createDate"), createDateLessThen));
		}
		if (createDateGreaterThen != null) {
			predicates.add(cb.greaterThan(root.get("createDate"), createDateGreaterThen));
		}
		if (createDateLessThenOrEquals != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get("createDate"), createDateLessThenOrEquals));
		}
		if (createDateGreaterThenOrEquals != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get("createDate"), createDateGreaterThenOrEquals));
		}

		return predicates;
	}

	private List<jakarta.persistence.criteria.Order> buildOrders(CriteriaBuilder cb, Root<ProcessInstance> root) {
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
	
	public ProcessInstanceQuery businessId(String businessId) {
		this.businessId=businessId;
		return this;
	}
	
	public ProcessInstanceQuery processId(long processId){
		this.processId=processId;
		return this;
	}

	public ProcessInstanceQuery page(int firstResult, int maxResults){
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}

	public ProcessInstanceQuery addOrderAsc(String property){
		ascOrders.add(property);
		return this;
	}

	public ProcessInstanceQuery addOrderDesc(String property){
		descOrders.add(property);
		return this;
	}

	public ProcessInstanceQuery createDateLessThen(Date date){
		this.createDateLessThen=date;
		return this;
	}

	public ProcessInstanceQuery createDateLessThenOrEquals(Date date){
		this.createDateLessThenOrEquals=date;
		return this;
	}

	public ProcessInstanceQuery createDateGreaterThen(Date date){
		this.createDateGreaterThen=date;
		return this;
	}

	public ProcessInstanceQuery createDateGreaterThenOrEquals(Date date){
		this.createDateGreaterThenOrEquals=date;
		return this;
	}

	public ProcessInstanceQuery parentId(long parentId) {
		this.parentId=parentId;
		return this;
	}
	public ProcessInstanceQuery rootId(long rootId) {
		this.rootId=rootId;
		return this;
	}
}
