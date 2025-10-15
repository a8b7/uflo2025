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
package com.bstek.uflo.expr.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl3.MapContext;

/**
 * @author Jacky.gao
 * @since 2013年8月15日
 */
public class ProcessMapContext extends MapContext implements java.io.Serializable{
	private static final long serialVersionUID = -6102470247244550707L;
	// 使用自己的Map来存储数据，避免JEXL3 API变化的问题
	private Map<String,Object> internalMap=new HashMap<>();

	public Map<String,Object> getMap(){
		return internalMap;
	}

	@Override
	public void set(String name, Object value) {
		internalMap.put(name, value);
		super.set(name, value);
	}

	@Override
	public Object get(String name) {
		return internalMap.get(name);
	}

	@Override
	public boolean has(String name) {
		return internalMap.containsKey(name);
	}
}
