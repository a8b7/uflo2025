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

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import com.bstek.uflo.command.Command;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.calendar.CalendarDef;

/**
 * @author Jacky.gao
 * @since 2013年9月23日
 */
public class GetCalendarDefCommand implements Command<CalendarDef> {
    private long calendarId;

    public GetCalendarDefCommand(long calendarId) {
        this.calendarId = calendarId;
    }

    public CalendarDef execute(Context context) {
        EntityManager em = null;
        try {
            em = context.getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<CalendarDef> query = cb.createQuery(CalendarDef.class);
            Root<CalendarDef> root = query.from(CalendarDef.class);
            query.select(root).where(cb.equal(root.get("id"), calendarId));
            try {
                return em.createQuery(query).getSingleResult();
            } catch (Exception e) {
                return null;
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
