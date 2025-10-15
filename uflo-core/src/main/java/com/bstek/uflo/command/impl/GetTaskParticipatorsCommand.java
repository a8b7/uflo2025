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
import com.bstek.uflo.model.task.TaskParticipator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @author Jacky.gao
 * @since 2013年9月10日
 */
public class GetTaskParticipatorsCommand implements Command<List<TaskParticipator>> {
	private long taskId;
	public GetTaskParticipatorsCommand(long taskId){
		this.taskId=taskId;
	}

	@SuppressWarnings("unchecked")
	public List<TaskParticipator> execute(Context context) {
		CriteriaBuilder cb = context.getSession().getCriteriaBuilder();
		CriteriaQuery<TaskParticipator> cq = cb.createQuery(TaskParticipator.class);
		Root<TaskParticipator> root = cq.from(TaskParticipator.class);

		Predicate predicate = cb.equal(root.get("taskId"), taskId);
		cq.where(predicate);

		return context.getSession().createQuery(cq).getResultList();
	}

}