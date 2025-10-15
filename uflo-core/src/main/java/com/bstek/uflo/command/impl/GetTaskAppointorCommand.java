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

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.task.TaskAppointor;

/**
 * @author Jacky.gao
 * @since 2013年8月19日
 */
public class GetTaskAppointorCommand implements Command<List<TaskAppointor>> {
	private String taskNodeName;
	private long processInstanceId;
	public GetTaskAppointorCommand(String taskNodeName,long processInstanceId){
		this.taskNodeName=taskNodeName;
		this.processInstanceId=processInstanceId;
	}
	public List<TaskAppointor> execute(Context context) {
		EntityManager em = null;
		try {
			em = context.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TaskAppointor> query = cb.createQuery(TaskAppointor.class);
		Root<TaskAppointor> root = query.from(TaskAppointor.class);

		List<Predicate> predicates = new java.util.ArrayList<>();
		predicates.add(cb.equal(root.get("processInstanceId"), processInstanceId));
		predicates.add(cb.equal(root.get("taskNodeName"), taskNodeName));

		query.where(predicates.toArray(new Predicate[0]));

		TypedQuery<TaskAppointor> typedQuery = em.createQuery(query);
		return typedQuery.getResultList();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}
}
