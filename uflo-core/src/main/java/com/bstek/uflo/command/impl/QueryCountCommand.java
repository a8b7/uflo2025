package com.bstek.uflo.command.impl;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.env.impl.ContextImpl;
import com.bstek.uflo.env.impl.ModernContextImpl;
import com.bstek.uflo.query.QueryJob;
import jakarta.persistence.EntityManager;

/**
 * 查询计数命令
 * 根据上下文类型智能管理EntityManager生命周期
 * 
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
		boolean shouldClose = false;
		
		try {
			em = context.getEntityManager();
			
			// 根据上下文类型判断是否需要手动管理EntityManager生命周期
			// ContextImpl每次创建新的EntityManager，需要手动关闭
			// ModernContextImpl返回由Spring管理的EntityManager，不应手动关闭
			shouldClose = (context instanceof ContextImpl);
			
			jakarta.persistence.criteria.CriteriaQuery<Long> criteriaQuery = job.buildCountQuery(em);
			jakarta.persistence.TypedQuery<Long> query = em.createQuery(criteriaQuery);
			Long count = query.getSingleResult();
			return count != null ? count.intValue() : 0;
		} finally {
			// 仅在使用传统ContextImpl时关闭EntityManager
			if (shouldClose && em != null && em.isOpen()) {
				em.close();
			}
		}
	}
}