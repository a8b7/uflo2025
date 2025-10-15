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

import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.variable.Variable;
import org.apache.commons.lang.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.CollectionJoin;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.QueryCountCommand;
import com.bstek.uflo.command.impl.QueryListCommand;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.model.task.TaskType;
import com.bstek.uflo.query.QueryJob;
import com.bstek.uflo.query.TaskQuery;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class TaskQueryImpl implements TaskQuery,QueryJob<Task>{
	private String url;
	private String businessId;
	private String owner;
	private long processInstanceId;
	private Date createDateLessThen;
	private Date createDateLessThenOrEquals;
	private Date createDateGreaterThen;
	private Date createDateGreaterThenOrEquals;
	private long rootProcessInstanceId;
	private Date dueDateLessThen;
	private Date dueDateLessThenOrEquals;
	private Date dueDateGreaterThen;
	private Date dueDateGreaterThenOrEquals;
	private Boolean countersign;
	private TaskType type;
	private String name;
	private String subject;
	private String nodeName;
	private Integer progress;
	private String priority;
	private int firstResult;
	private int maxResults;
	private List<TaskState> states=new ArrayList<TaskState>();
	private List<TaskState> prevstates=new ArrayList<TaskState>();
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private List<String> participators=new ArrayList<String>();
	private List<String> assignees=new ArrayList<String>();
	private List<Long> processIds=new ArrayList<Long>();
	private CommandService commandService;
	public TaskQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}
	public List<Task> list() {
		return (List<Task>) commandService.executeCommand(new QueryListCommand(this));
	}

	public int count() {
		return commandService.executeCommand(new QueryCountCommand(this));
	}

	@Override
	public CriteriaQuery<Long> buildCountQuery(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Task> root = countQuery.from(Task.class);
		countQuery.select(cb.count(root));
		List<Predicate> predicates = buildPredicates(cb, root, countQuery);
		if (!predicates.isEmpty()) {
			countQuery.where(predicates.toArray(new Predicate[0]));
		}
		return countQuery;
	}
	@SuppressWarnings("unchecked")
	public CriteriaQuery<Task> buildQuery(EntityManager em, boolean queryCount) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);

		Root<Task> root = query.from(Task.class);


		query.select(root);

		List<Predicate> predicates = buildPredicates(cb, root, query);
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

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Task> root, CriteriaQuery<?> query) {
		List<Predicate> predicates = new ArrayList<>();

		if (processIds.size() > 0) {
			if (processIds.size() == 1) {
				predicates.add(cb.equal(root.get("processId"), processIds.get(0)));
			} else {
				predicates.add(root.get("processId").in(processIds));
			}
		}
		if (StringUtils.isNotEmpty(owner)) {
			predicates.add(cb.equal(root.get("owner"), owner));
		}
		if (assignees.size() > 0) {
			if (assignees.size() == 1) {
				predicates.add(cb.equal(root.get("assignee"), assignees.get(0)));
			} else {
				predicates.add(root.get("assignee").in(assignees));
			}
		}
		if (countersign != null) {
			// 注意：Task实体中没有countersign字段，这里可能需要调整
			// 可能是基于type字段或者countersignCount字段来判断
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
		if (StringUtils.isNotEmpty(url)) {
			predicates.add(cb.like(root.get("url"), url));
		}
		if (StringUtils.isNotEmpty(subject)) {
			predicates.add(cb.like(root.get("subject"), subject));
		}
		if (StringUtils.isNotEmpty(name)) {
			predicates.add(cb.like(root.get("taskName"), name));
		}
		if (StringUtils.isNotEmpty(nodeName)) {
			predicates.add(cb.equal(root.get("nodeName"), nodeName));
		}
		if (StringUtils.isNotEmpty(businessId)) {
			predicates.add(cb.equal(root.get("businessId"), businessId));
		}
		if (StringUtils.isNotEmpty(priority)) {
			predicates.add(cb.equal(root.get("priority"), priority));
		}
		if (progress != null) {
			predicates.add(cb.equal(root.get("progress"), progress));
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
		if (dueDateLessThen != null) {
			predicates.add(cb.lessThan(root.get("duedate"), dueDateLessThen));
		}
		if (dueDateGreaterThen != null) {
			predicates.add(cb.greaterThan(root.get("duedate"), dueDateGreaterThen));
		}
		if (dueDateLessThenOrEquals != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get("duedate"), dueDateLessThenOrEquals));
		}
		if (dueDateGreaterThenOrEquals != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get("duedate"), dueDateGreaterThenOrEquals));
		}

		// 处理多个状态的OR条件
		if (states.size() == 1) {
			predicates.add(cb.equal(root.get("state"), states.get(0)));
		} else if (states.size() > 1) {
			Predicate[] statePredicates = states.stream()
				.map(state -> cb.equal(root.get("state"), state))
				.toArray(Predicate[]::new);
			predicates.add(cb.or(statePredicates));
		}

		// 处理多个前置状态的OR条件
		if (prevstates.size() == 1) {
			predicates.add(cb.equal(root.get("prevState"), prevstates.get(0)));
		} else if (prevstates.size() > 1) {
			Predicate[] prevStatePredicates = prevstates.stream()
				.map(state -> cb.equal(root.get("prevState"), state))
				.toArray(Predicate[]::new);
			predicates.add(cb.or(prevStatePredicates));
		}

		// 处理参与者查询 - 使用EXISTS子查询
		if (participators.size() > 0) {
			Subquery<Long> subquery = query.subquery(Long.class);
			Root<com.bstek.uflo.model.task.TaskParticipator> participatorRoot = subquery.from(com.bstek.uflo.model.task.TaskParticipator.class);
			subquery.select(participatorRoot.get("id"));

			Predicate participatorPredicate = cb.equal(participatorRoot.get("taskId"), root.get("id"));

			if (participators.size() == 1) {
				participatorPredicate = cb.and(participatorPredicate,
					cb.equal(participatorRoot.get("user"), participators.get(0)));
			} else {
				participatorPredicate = cb.and(participatorPredicate,
					participatorRoot.get("user").in(participators));
			}

			subquery.where(participatorPredicate);
			predicates.add(cb.exists(subquery));
		}

		return predicates;
	}

	private List<jakarta.persistence.criteria.Order> buildOrders(CriteriaBuilder cb, Root<Task> root) {
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

	public TaskQuery addAssignee(String assignee) {
		assignees.add(assignee);
		return this;
	}

	public TaskQuery businessId(String businessId) {
		this.businessId=businessId;
		return this;
	}

	public TaskQuery addOrderAsc(String property){
		ascOrders.add(property);
		return this;
	}

	public TaskQuery addOrderDesc(String property){
		descOrders.add(property);
		return this;
	}

	public TaskQuery assignee(String assignee) {
		assignees.add(assignee);
		return this;
	}

	public TaskQuery owner(String owner) {
		this.owner=owner;
		return this;
	}

	public TaskQuery addTaskState(TaskState state) {
		states.add(state);
		return this;
	}

	public TaskQuery addPrevTaskState(TaskState state) {
		prevstates.add(state);
		return this;
	}

	public TaskQuery processInstanceId(long processInstanceId) {
		this.processInstanceId=processInstanceId;
		return this;
	}

	public TaskQuery createDateLessThen(Date createDateLessThen) {
		this.createDateLessThen=createDateLessThen;
		return this;
	}

	public TaskQuery createDateLessThenOrEquals(Date createDateLessThenOrEquals) {
		this.createDateLessThenOrEquals=createDateLessThenOrEquals;
		return this;
	}

	public TaskQuery createDateGreaterThen(Date createDateGreaterThen) {
		this.createDateGreaterThen=createDateGreaterThen;
		return this;
	}

	public TaskQuery createDateGreaterThenOrEquals(Date createDateGreaterThenOrEquals) {
		this.createDateGreaterThenOrEquals=createDateGreaterThenOrEquals;
		return this;
	}
	public TaskQuery dueDateLessThen(Date dueDateLessThen) {
		this.dueDateLessThen=dueDateLessThen;
		return this;
	}

	public TaskQuery dueDateLessThenOrEquals(Date dueDateLessThenOrEquals) {
		this.dueDateLessThenOrEquals=dueDateLessThenOrEquals;
		return this;
	}

	public TaskQuery dueDateGreaterThen(Date dueDateGreaterThen) {
		this.dueDateGreaterThen=dueDateGreaterThen;
		return this;
	}

	public TaskQuery dueDateGreaterThenOrEquals(Date dueDateGreaterThenOrEquals) {
		this.dueDateGreaterThenOrEquals=dueDateGreaterThenOrEquals;
		return this;
	}

	public TaskQuery urlLike(String url) {
		this.url=url;
		return this;
	}

	public TaskQuery subjectLike(String subject) {
		this.subject=subject;
		return this;
	}

	public TaskQuery countersign(boolean countersign) {
		this.countersign=countersign;
		return this;
	}


	public TaskQuery taskType(TaskType type) {
		this.type=type;
		return this;
	}

	public TaskQuery addParticipator(String user) {
		participators.add(user);
		return this;
	}

	public TaskQuery processId(long processId) {
		this.processIds.add(processId);
		return this;
	}
	public TaskQuery addProcessId(long processId) {
		this.processIds.add(processId);
		return this;
	}
	public TaskQuery rootProcessInstanceId(long rootProcessInstanceId) {
		this.rootProcessInstanceId=rootProcessInstanceId;
		return this;
	}

	public TaskQuery nameLike(String name) {
		this.name=name;
		return this;
	}

	@Override
	public TaskQuery priority(String priority) {
		this.priority=priority;
		return this;
	}

	@Override
	public TaskQuery progress(int progress) {
		this.progress=progress;
		return this;
	}

	public TaskQuery nodeName(String nodeName) {
		this.nodeName=nodeName;
		return this;
	}

	public TaskQuery page(int firstResult, int maxResults) {
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}
}