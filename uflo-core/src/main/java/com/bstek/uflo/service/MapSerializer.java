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
package com.bstek.uflo.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 基于Jackson的Map序列化和反序列化工具类
 * 用于替代原有的JAXB实现，支持JDK17
 *
 * @author UFLO2 Team
 */
public class MapSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将Map对象序列化为JSON字符串
     *
     * @param map 要序列化的Map对象
     * @return JSON字符串
     * @throws JsonProcessingException 序列化异常
     */
    public static <K, V> String serialize(Map<K, V> map) throws JsonProcessingException {
        if (map == null) {
            return null;
        }
        return objectMapper.writeValueAsString(map);
    }

    /**
     * 将JSON字符串反序列化为Map对象
     *
     * @param json JSON字符串
     * @return Map对象
     * @throws IOException 反序列化异常
     */
    public static <K, V> Map<K, V> deserialize(String json) throws IOException {
        if (json == null || json.trim().isEmpty()) {
            return new HashMap<K, V>();
        }
        return objectMapper.readValue(json, new TypeReference<Map<K, V>>() {});
    }

    /**
     * 将Map对象转换为另一个Map对象（深拷贝）
     *
     * @param original 原始Map对象
     * @return 新的Map对象
     * @throws JsonProcessingException 序列化异常
     */
    public static <K, V> Map<K, V> cloneMap(Map<K, V> original) throws JsonProcessingException {
        if (original == null) {
            return null;
        }
        try {
            String json = serialize(original);
            return deserialize(json);
        } catch (IOException e) {
            throw new JsonProcessingException("Failed to clone map", e) {};
        }
    }
}