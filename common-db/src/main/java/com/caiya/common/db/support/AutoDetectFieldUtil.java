package com.caiya.common.db.support;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 自动探测字段映射工具类
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/22
 **/
public final class AutoDetectFieldUtil {


    public static Map<String, String> getFieldMap(Class<?> clazz, String[] columnNames) {
        Map<String, String> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (Arrays.asList(columnNames).contains(fieldName)) {
                fieldMap.put(fieldName, fieldName);
                continue;
            }
            String snakeCaseFieldName = PropertyNamingStrategy.SNAKE_CASE.nameForField(fieldName);
            if (Arrays.asList(columnNames).contains(snakeCaseFieldName)) {
                fieldMap.put(fieldName, snakeCaseFieldName);
                continue;
            }
            String lowerCaseFieldName = PropertyNamingStrategy.LOWER_CASE.nameForField(fieldName);
            if (Arrays.asList(columnNames).contains(lowerCaseFieldName)) {
                fieldMap.put(fieldName, lowerCaseFieldName);
                continue;
            }
        }
        return fieldMap;
    }

    private AutoDetectFieldUtil(){}


}
