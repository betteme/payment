/**
 *
 */
package com.block.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    /**
     * 将JSON字符串转换为对象
     *
     * @param json      JSON字符串
     * @param valueType 对象类型
     *
     * @return 对象
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param value 对象
     *
     * @return JSOn字符串
     */
    public static String toJson(Object value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
