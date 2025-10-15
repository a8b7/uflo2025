package com.bstek.uflo.command.impl;

import java.util.List;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.variable.BlobVariable;
import com.bstek.uflo.model.variable.TextVariable;
import com.bstek.uflo.query.QueryJob;
import jakarta.persistence.EntityManager;

/**
 * @author Jacky.gao
 * @since 2013年8月14日
 */
public class QueryListCommand<T> implements Command<T> {
	private QueryJob<T> job;
	public QueryListCommand(QueryJob<T> job){
		this.job=job;
	}
	public T execute(Context context) {
		EntityManager em = null;
		try {
			em = context.getEntityManager();

			jakarta.persistence.criteria.CriteriaQuery<?> criteriaQuery = job.buildQuery(em, false);
			jakarta.persistence.TypedQuery<?> query = em.createQuery(criteriaQuery);

			// 处理分页
			int firstResult = job.getFirstResult();
			if (firstResult > 0) {
				query.setFirstResult(firstResult);
			}

			int maxResults = job.getMaxResults();
			if (maxResults > 0) {
				query.setMaxResults(maxResults);
			}

			List<?> list = query.getResultList();
			for (Object obj : list) {
				if (obj instanceof BlobVariable) {
					((BlobVariable) obj).initValue(context);
				}
				if (obj instanceof TextVariable) {
					((TextVariable) obj).initValue(context);
				}
			}
			return (T) list;
		} finally {
			if (em != null && em.isOpen()) {
				em.close(); // 重要：确保关闭 EntityManager
			}
		}
	}
}