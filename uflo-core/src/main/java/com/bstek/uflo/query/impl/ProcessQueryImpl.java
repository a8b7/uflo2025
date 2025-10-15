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
import org.apache.commons.lang.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Order;

import com.bstek.uflo.command.CommandService;
import com.bstek.uflo.command.impl.QueryCountCommand;
import com.bstek.uflo.command.impl.QueryListCommand;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.query.ProcessQuery;
import com.bstek.uflo.query.QueryJob;
import com.bstek.uflo.utils.EnvironmentUtils;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class ProcessQueryImpl implements ProcessQuery,QueryJob<ProcessDefinition>{
	private long id;
	private String name;
	private String key;
	private String categoryId;
	private String category;
	private String subject;
	private int version;
	private int firstResult;
	private int maxResults;
	private Date createDateLessThen;
	private Date createDateLessThenOrEquals;
	private Date createDateGreaterThen;
	private Date createDateGreaterThenOrEquals;
	private List<String> ascOrders=new ArrayList<String>();
	private List<String> descOrders=new ArrayList<String>();
	private CommandService commandService;
	public ProcessQueryImpl(CommandService commandService){
		this.commandService=commandService;
	}
	public CriteriaQuery<ProcessDefinition> buildQuery(EntityManager em, boolean queryCount) {
		CriteriaBuilder cb = em.getCriteriaBuilder();


		return buildDataQuery(cb);
	}

	@Override
	public CriteriaQuery<Long> buildCountQuery(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<ProcessDefinition> root = countQuery.from(ProcessDefinition.class);
		countQuery.select(cb.count(root));
		List<Predicate> predicates = buildPredicates(cb, root);
		if (!predicates.isEmpty()) {
			countQuery.where(predicates.toArray(new Predicate[0]));
		}
		return countQuery;
	}


	private CriteriaQuery<ProcessDefinition> buildDataQuery(CriteriaBuilder cb) {
		CriteriaQuery<ProcessDefinition> query = cb.createQuery(ProcessDefinition.class);
		Root<ProcessDefinition> root = query.from(ProcessDefinition.class);

		// 构建数据查询
		query.select(root);

		// 添加条件
		List<Predicate> predicates = buildPredicates(cb, root);
		if (!predicates.isEmpty()) {
			query.where(predicates.toArray(new Predicate[0]));
		}

		// 添加排序
		List<jakarta.persistence.criteria.Order> orders = buildOrders(cb, root);
		if (!orders.isEmpty()) {
			query.orderBy(orders);
		}

		return query;
	}

	@SuppressWarnings("unchecked")
	public List<ProcessDefinition> list() {
		return (List<ProcessDefinition>) commandService.executeCommand(new QueryListCommand<>(this));
	}

	public int count() {
		return commandService.executeCommand(new QueryCountCommand(this));
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ProcessDefinition> root){
		List<Predicate> predicates = new ArrayList<>();

		if(id>0){
			predicates.add(cb.equal(root.get("id"), id));
		}
		if(StringUtils.isNotEmpty(name)){
			predicates.add(cb.like(root.get("name"), name));
		}
		if(StringUtils.isNotEmpty(key)){
			predicates.add(cb.like(root.get("key"), key));
		}
		if(StringUtils.isNotEmpty(subject)){
			predicates.add(cb.like(root.get("subject"), subject));
		}
		if(createDateLessThen!=null){
			predicates.add(cb.lessThan(root.get("createDate"), createDateLessThen));
		}
		if(createDateGreaterThen!=null){
			predicates.add(cb.greaterThan(root.get("createDate"), createDateGreaterThen));
		}
		if(createDateLessThenOrEquals!=null){
			predicates.add(cb.lessThanOrEqualTo(root.get("createDate"), createDateLessThenOrEquals));
		}
		if(createDateGreaterThenOrEquals!=null){
			predicates.add(cb.greaterThanOrEqualTo(root.get("createDate"), createDateGreaterThenOrEquals));
		}
		if(StringUtils.isNotEmpty(categoryId)){
			predicates.add(cb.equal(root.get("categoryId"), categoryId));
		}else{
			categoryId=EnvironmentUtils.getEnvironment().getCategoryId();
			if(StringUtils.isNotEmpty(categoryId)){
				predicates.add(cb.equal(root.get("categoryId"), categoryId));
			}
		}
		if(StringUtils.isNotBlank(category)) {
			predicates.add(cb.equal(root.get("category"), category));
		}
		if(version>0){
			predicates.add(cb.equal(root.get("version"), version));
		}

		return predicates;
	}

	private List<jakarta.persistence.criteria.Order> buildOrders(CriteriaBuilder cb, Root<ProcessDefinition> root){
		List<jakarta.persistence.criteria.Order> orders = new ArrayList<>();

		for(String ascProperty:ascOrders){
			orders.add(cb.asc(root.get(ascProperty)));
		}
		for(String descProperty:descOrders){
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

	public ProcessQuery createDateGreaterThen(Date date) {
		this.createDateGreaterThen=date;
		return this;
	}
	public ProcessQuery createDateGreaterThenOrEquals(Date date) {
		this.createDateGreaterThenOrEquals=date;
		return this;
	}
	public ProcessQuery createDateLessThen(Date date) {
		this.createDateLessThen=date;
		return this;
	}
	public ProcessQuery createDateLessThenOrEquals(Date date) {
		this.createDateLessThenOrEquals=date;
		return this;
	}

	public ProcessQuery addOrderAsc(String property){
		ascOrders.add(property);
		return this;
	}

	public ProcessQuery addOrderDesc(String property){
		descOrders.add(property);
		return this;
	}

	public ProcessQuery id(long id) {
		this.id=id;
		return this;
	}

	public ProcessQuery categoryId(String categoryId) {
		this.categoryId=categoryId;
		return this;
	}

	@Override
	public ProcessQuery category(String category) {
		this.category=category;
		return this;
	}

	public ProcessQuery nameLike(String name) {
		this.name=name;
		return this;
	}

	public ProcessQuery subjectLike(String subject) {
		this.subject=subject;
		return this;
	}

	public ProcessQuery keyLike(String key) {
		this.key=key;
		return this;
	}

	public ProcessQuery version(int version) {
		this.version=version;
		return this;
	}

	public ProcessQuery page(int firstResult, int maxResults) {
		this.firstResult=firstResult;
		this.maxResults=maxResults;
		return this;
	}
}