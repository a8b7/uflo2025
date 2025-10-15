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

import org.apache.commons.lang.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.QueryCountCommand;
import com.bstek.uflo.command.impl.QueryListCommand;
import com.bstek.uflo.model.HistoryProcessInstance;
import com.bstek.uflo.query.HistoryProcessInstanceQuery;
import com.bstek.uflo.query.QueryJob;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class HistoryProcessInstanceQueryImpl implements HistoryProcessInstanceQuery,QueryJob<HistoryProcessInstance> {
	private long processId;
	private int firstResult;
	private int maxResults;
	private String tag;
	private String promoter;
	private String businessId;
	private Date createDateLessThen;
	private Date createDateLessThenOrEquals;
	private Date createDateGreaterThen;
	private Date createDateGreaterThenOrEquals;
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private CommandService commandService;

	public HistoryProcessInstanceQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}

	@SuppressWarnings("unchecked")
	public CriteriaQuery<HistoryProcessInstance> buildQuery(EntityManager em, boolean queryCount) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HistoryProcessInstance> query = cb.createQuery(HistoryProcessInstance.class);
		Root<HistoryProcessInstance> root = query.from(HistoryProcessInstance.class);

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
		CriteriaQuery<Long> query = cb.createQuery(Long.class);

		Root<HistoryProcessInstance> root = query.from(HistoryProcessInstance.class);

		query.select(cb.count(root));
		List<Predicate> predicates = buildPredicates(cb, root);
		if (!predicates.isEmpty()) {
			query.where(predicates.toArray(new Predicate[0]));

		}

		return query;
	}

	public List<HistoryProcessInstance> list(){
		return (List<HistoryProcessInstance>) commandService.executeCommand(new QueryListCommand(this));
	}

	public int count(){
		return commandService.executeCommand(new QueryCountCommand(this));
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<HistoryProcessInstance> root) {
		List<Predicate> predicates = new ArrayList<>();

		// 历史流程实例必须有结束日期
		predicates.add(cb.isNotNull(root.get("endDate")));

		if (processId > 0) {
			predicates.add(cb.equal(root.get("processId"), processId));
		}
		if (StringUtils.isNotEmpty(businessId)) {
			predicates.add(cb.equal(root.get("businessId"), businessId));
		}
		if (StringUtils.isNotEmpty(tag)) {
			predicates.add(cb.equal(root.get("tag"), tag));
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

	private List<jakarta.persistence.criteria.Order> buildOrders(CriteriaBuilder cb, Root<HistoryProcessInstance> root) {
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

	public HistoryProcessInstanceQuery processId(long processId){
		this.processId=processId;
		return this;
	}

	public HistoryProcessInstanceQuery page(int firstResult, int maxResults){
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}

	public HistoryProcessInstanceQuery addOrderAsc(String property){
		ascOrders.add(property);
		return this;
	}
	public HistoryProcessInstanceQuery businessId(String businessId){
		this.businessId=businessId;
		return this;
	}
	public HistoryProcessInstanceQuery promoter(String promoter){
		this.promoter=promoter;
		return this;
	}
	public HistoryProcessInstanceQuery tag(String tag){
		this.tag=tag;
		return this;
	}
	public HistoryProcessInstanceQuery addOrderDesc(String property){
		descOrders.add(property);
		return this;
	}

	public HistoryProcessInstanceQuery createDateLessThen(Date date){
		this.createDateLessThen=date;
		return this;
	}

	public HistoryProcessInstanceQuery createDateLessThenOrEquals(Date date){
		this.createDateLessThenOrEquals=date;
		return this;
	}

	public HistoryProcessInstanceQuery createDateGreaterThen(Date date){
		this.createDateGreaterThen=date;
		return this;
	}

	public HistoryProcessInstanceQuery createDateGreaterThenOrEquals(Date date){
		this.createDateGreaterThenOrEquals=date;
		return this;
	}
}