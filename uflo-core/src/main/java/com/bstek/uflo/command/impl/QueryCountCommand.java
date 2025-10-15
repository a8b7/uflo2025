package com.bstek.uflo.command.impl;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.query.QueryJob;
import jakarta.persistence.EntityManager;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class QueryCountCommand implements Command<Integer> {
	private QueryJob<?> job;
	public QueryCountCommand(QueryJob<?> job){
		this.job=job;
	}
	public Integer execute(Context context) {
		EntityManager em = null;
		try {
			em = context.getEntityManager();

			jakarta.persistence.criteria.CriteriaQuery<Long> criteriaQuery = job.buildCountQuery(em);
			jakarta.persistence.TypedQuery<Long> query = em.createQuery(criteriaQuery);
			Long count = query.getSingleResult();
			return count != null ? count.intValue() : 0;
			// 执行数据库操作
			// ...
		} finally {
			if (em != null && em.isOpen()) {
				em.close(); // 重要：确保关闭 EntityManager
			}
		}
	}
}