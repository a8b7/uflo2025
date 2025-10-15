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

import java.util.Date;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.apache.commons.lang.StringUtils;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.deploy.ProcessDeployer;
import com.bstek.uflo.deploy.parse.impl.ProcessParser;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.Blob;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.utils.EnvironmentUtils;



/**
 * @author Jacky.gao
 * @since 2013年8月2日
 */
public class GetProcessCommand implements Command<ProcessDefinition> {
	private long processId;
	private String processName;
	private int version;
	private String categoryId;
	public GetProcessCommand(long processId,String processName,int version,String categoryId){
		this.processId=processId;
		this.processName=processName;
		this.version=version;
		this.categoryId=categoryId;
	}
	@SuppressWarnings("unchecked")
	public ProcessDefinition execute(Context context) {
		Session session=context.getSession();
		if(processId>0){
			ProcessDefinition p=(ProcessDefinition)session.get(ProcessDefinition.class, processId);
			return parseProcess(p.getId(),p.getVersion(),p.getName(),session);
		}else if(StringUtils.isNotEmpty(processName)){
			// 使用 JPA Criteria API 替换 Hibernate Criteria API
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<ProcessDefinition> cq = cb.createQuery(ProcessDefinition.class);
			Root<ProcessDefinition> root = cq.from(ProcessDefinition.class);

			// 构建查询条件
			Predicate namePredicate = cb.equal(root.get("name"), processName);
			cq.where(namePredicate);
			cq.orderBy(cb.desc(root.get("version")));

			if(categoryId==null){
				categoryId=EnvironmentUtils.getEnvironment().getCategoryId();
			}
			Predicate categoryPredicate = null;
			if(StringUtils.isNotEmpty(categoryId)){
				categoryPredicate = cb.equal(root.get("categoryId"), categoryId);
				cq.where(cb.and(namePredicate, categoryPredicate));
			}

			if(version>0){
				Predicate versionPredicate = cb.equal(root.get("version"), version);
				if(StringUtils.isNotEmpty(categoryId)){
					cq.where(cb.and(namePredicate, categoryPredicate, versionPredicate));
				} else {
					cq.where(cb.and(namePredicate, versionPredicate));
				}
				List<ProcessDefinition> processes=session.createQuery(cq).getResultList();
				if(processes.size()>0){
					ProcessDefinition p=processes.get(0);
					return parseProcess(p.getId(),p.getVersion(),p.getName(),session);
				}
			}else{
				List<ProcessDefinition> processes=session.createQuery(cq).getResultList();
				for(ProcessDefinition process:processes){
					Date effectDate=process.getEffectDate();
					if(effectDate==null){
						return parseProcess(process.getId(),process.getVersion(),process.getName(),session);
					}else{
						if((new Date()).getTime()>effectDate.getTime()){
							return parseProcess(process.getId(),process.getVersion(),process.getName(),session);
						}
					}
				}
			}
		}
		return null;
	}

	private ProcessDefinition parseProcess(long processId,int version,String processName,Session session){
		String hql="from "+Blob.class.getName()+" where processId=:processId and name=:name";
		Blob blob=(Blob)session.createQuery(hql, Blob.class)
				.setParameter("processId",processId)
				.setParameter("name",processName+ProcessDeployer.PROCESS_EXTENSION_NAME)
				.uniqueResult();
		try {
			ProcessDefinition process=ProcessParser.parseProcess(blob.getBlobValue(),processId,true);
			process.setId(processId);
			process.setVersion(version);
			return process;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}