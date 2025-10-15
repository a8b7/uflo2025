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
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.model.task.TaskType;
import com.bstek.uflo.query.HistoryTaskQuery;
import com.bstek.uflo.query.QueryJob;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class HistoryTaskQueryImpl implements HistoryTaskQuery,QueryJob<HistoryTask>{
	private String url;
	private String assignee;
	private String owner;
	private String businessId;
	private long processInstanceId;
	private long historyProcessInstanceId;
	private long rootProcessInstanceId;
	private Date createDateLessThen;
	private Date createDateLessThenOrEquals;
	private Date createDateGreaterThen;
	private Date createDateGreaterThenOrEquals;
	private Date endDateLessThen;
	private Date endDateLessThenOrEquals;
	private Date endDateGreaterThen;
	private Date endDateGreaterThenOrEquals;
	private Boolean countersign;
	private TaskType type;
	private long processId;
	private long taskId;
	private String name;
	private String nodeName;
	private int firstResult;
	private int maxResults;
	private List<TaskState> states=new ArrayList<TaskState>();
	private List<TaskState> prevstates=new ArrayList<TaskState>();
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private CommandService commandService;
	public HistoryTaskQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}
	public List<HistoryTask> list() {
		return (List<HistoryTask>) commandService.executeCommand(new QueryListCommand(this));
	}

	public int count() {
		return commandService.executeCommand(new QueryCountCommand(this));
	}
	@Override
	public CriteriaQuery<Long> buildCountQuery(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);

		Root<HistoryTask> root = query.from(HistoryTask.class);

		query.select(cb.count(root));
		List<Predicate> predicates = buildPredicates(cb, root);
		if (!predicates.isEmpty()) {
			query.where(predicates.toArray(new Predicate[0]));

		}

		return query;
	}
	@SuppressWarnings("unchecked")
	public CriteriaQuery<HistoryTask> buildQuery(EntityManager em, boolean queryCount) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HistoryTask> query = cb.createQuery(HistoryTask.class);
		Root<HistoryTask> root = query.from(HistoryTask.class);

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

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<HistoryTask> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (processId > 0) {
			predicates.add(cb.equal(root.get("processId"), processId));
		}
		if (StringUtils.isNotEmpty(owner)) {
			predicates.add(cb.equal(root.get("owner"), owner));
		}
		if (StringUtils.isNotEmpty(businessId)) {
			predicates.add(cb.equal(root.get("businessId"), businessId));
		}
		if (StringUtils.isNotEmpty(assignee)) {
			predicates.add(cb.equal(root.get("assignee"), assignee));
		}
		if (countersign != null) {
			// 注意：HistoryTask实体中的countersign字段可能需要调整
			// 类似TaskQueryImpl的处理，可能基于type字段判断
			if (countersign) {
				predicates.add(cb.equal(root.get("type"), TaskType.Countersign));
			} else {
				predicates.add(cb.notEqual(root.get("type"), TaskType.Countersign));
			}
		}
		if (type != null) {
			predicates.add(cb.equal(root.get("type"), type));
		}
		if (processInstanceId > 0) {
			predicates.add(cb.equal(root.get("processInstanceId"), processInstanceId));
		}
		if (rootProcessInstanceId > 0) {
			predicates.add(cb.equal(root.get("rootProcessInstanceId"), rootProcessInstanceId));
		}
		if (taskId > 0) {
			predicates.add(cb.equal(root.get("taskId"), taskId));
		}
		if (historyProcessInstanceId > 0) {
			predicates.add(cb.equal(root.get("historyProcessInstanceId"), historyProcessInstanceId));
		}
		if (StringUtils.isNotEmpty(url)) {
			predicates.add(cb.like(root.get("url"), url));
		}
		if (StringUtils.isNotEmpty(name)) {
			predicates.add(cb.like(root.get("taskName"), name));
		}
		if (StringUtils.isNotEmpty(nodeName)) {
			predicates.add(cb.like(root.get("nodeName"), nodeName));
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
		if (endDateLessThen != null) {
			predicates.add(cb.lessThan(root.get("endDate"), endDateLessThen));
		}
		if (endDateGreaterThen != null) {
			predicates.add(cb.greaterThan(root.get("endDate"), endDateGreaterThen));
		}
		if (endDateLessThenOrEquals != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), endDateLessThenOrEquals));
		}
		if (endDateGreaterThenOrEquals != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get("endDate"), endDateGreaterThenOrEquals));
		}

		// 处理多个状态条件 - 注意这里的逻辑与TaskQueryImpl不同
		// 在原代码中，这里是AND逻辑，而不是OR逻辑
		for (TaskState state : states) {
			predicates.add(cb.equal(root.get("state"), state));
		}
		for (TaskState state : prevstates) {
			predicates.add(cb.equal(root.get("prevState"), state));
		}

		return predicates;
	}

	private List<jakarta.persistence.criteria.Order> buildOrders(CriteriaBuilder cb, Root<HistoryTask> root) {
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
	
	public HistoryTaskQuery addOrderAsc(String property){
		ascOrders.add(property);
		return this;
	}

	public HistoryTaskQuery addOrderDesc(String property){
		descOrders.add(property);
		return this;
	}
	
	public HistoryTaskQuery assignee(String assignee) {
		this.assignee=assignee;
		return this;
	}

	public HistoryTaskQuery owner(String owner) {
		this.owner=owner;
		return this;
	}

	public HistoryTaskQuery addTaskState(TaskState state) {
		states.add(state);
		return this;
	}

	public HistoryTaskQuery addPrevTaskState(TaskState state) {
		prevstates.add(state);
		return this;
	}

	public HistoryTaskQuery processInstanceId(long processInstanceId) {
		this.processInstanceId=processInstanceId;
		return this;
	}
	
	
	public HistoryTaskQuery rootProcessInstanceId(long rootProcessInstanceId) {
		this.rootProcessInstanceId=rootProcessInstanceId;
		return this;
	}
	
	public HistoryTaskQuery taskId(long taskId) {
		this.taskId=taskId;
		return this;
	}

	public HistoryTaskQuery createDateLessThen(Date createDateLessThen) {
		this.createDateLessThen=createDateLessThen;
		return this;
	}

	public HistoryTaskQuery createDateLessThenOrEquals(Date createDateLessThenOrEquals) {
		this.createDateLessThenOrEquals=createDateLessThenOrEquals;
		return this;
	}

	public HistoryTaskQuery createDateGreaterThen(Date createDateGreaterThen) {
		this.createDateGreaterThen=createDateGreaterThen;
		return this;
	}

	public HistoryTaskQuery createDateGreaterThenOrEquals(Date createDateGreaterThenOrEquals) {
		this.createDateGreaterThenOrEquals=createDateGreaterThenOrEquals;
		return this;
	}
	public HistoryTaskQuery endDateLessThen(Date endDateLessThen) {
		this.endDateLessThen=endDateLessThen;
		return this;
	}
	
	public HistoryTaskQuery endDateLessThenOrEquals(Date endDateLessThenOrEquals) {
		this.endDateLessThenOrEquals=endDateLessThenOrEquals;
		return this;
	}
	
	public HistoryTaskQuery endDateGreaterThen(Date endDateGreaterThen) {
		this.endDateGreaterThen=endDateGreaterThen;
		return this;
	}
	
	public HistoryTaskQuery endDateGreaterThenOrEquals(Date endDateGreaterThenOrEquals) {
		this.endDateGreaterThenOrEquals=endDateGreaterThenOrEquals;
		return this;
	}

	public HistoryTaskQuery urlLike(String url) {
		this.url=url;
		return this;
	}
	public HistoryTaskQuery businessId(String businessId) {
		this.businessId=businessId;
		return this;
	}

	public HistoryTaskQuery countersign(boolean countersign) {
		this.countersign=countersign;
		return this;
	}
	

	public HistoryTaskQuery taskType(TaskType type) {
		this.type=type;
		return this;
	}
	
	public HistoryTaskQuery processId(long processId) {
		this.processId=processId;
		return this;
	}
	public HistoryTaskQuery historyProcessInstanceId(long historyProcessInstanceId) {
		this.historyProcessInstanceId=historyProcessInstanceId;
		return this;
	}

	public HistoryTaskQuery nameLike(String name) {
		this.name=name;
		return this;
	}
	
	public HistoryTaskQuery nodeName(String nodeName) {
		this.nodeName=nodeName;
		return this;
	}
	public HistoryTaskQuery page(int firstResult, int maxResults) {
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}
}
