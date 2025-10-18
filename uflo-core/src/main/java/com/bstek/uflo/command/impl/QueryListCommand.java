package com.bstek.uflo.command.impl;

import java.util.List;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.env.impl.ContextImpl;
import com.bstek.uflo.env.impl.ModernContextImpl;
import com.bstek.uflo.model.variable.BlobVariable;
import com.bstek.uflo.model.variable.TextVariable;
import com.bstek.uflo.query.QueryJob;
import jakarta.persistence.EntityManager;

/**
 * 查询列表命令
 * 根据上下文类型智能管理EntityManager生命周期
 * 
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
		boolean shouldClose = false;
		
		try {
			em = context.getEntityManager();
			
			// 根据上下文类型判断是否需要手动管理EntityManager生命周期
			// ContextImpl每次创建新的EntityManager，需要手动关闭
			// ModernContextImpl返回由Spring管理的EntityManager，不应手动关闭
			shouldClose = (context instanceof ContextImpl);
			
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
			// 仅在使用传统ContextImpl时关闭EntityManager
			if (shouldClose && em != null && em.isOpen()) {
				em.close();
			}
		}
	}
}