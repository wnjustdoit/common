package com.caiya.common.db.support;

import com.caiya.common.db.core.annotation.SQLField;
import com.caiya.common.db.core.annotation.TableName;
import com.caiya.common.db.object.OperationType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL注解工具类
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/22
 **/
public final class SqlAnnotationUtil {

    public static Map<String, String> getFieldColumnMap(Class<?> clazz, OperationType operationType) {
        return getAnnotationMapping(clazz, true, operationType);
    }

    public static Map<String, String> getColumnFieldMap(Class<?> clazz, OperationType operationType) {
        return getAnnotationMapping(clazz, false, operationType);
    }

    private static Map<String, String> getAnnotationMapping(Class<?> clazz, boolean fieldKey, OperationType operationType) {
        Map<String, String> result = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            SQLField sqlField = field.getAnnotation(SQLField.class);
            if (sqlField != null) {
                if (operationType != null && ArrayUtils.isNotEmpty(sqlField.include())) {
                    if (!Arrays.asList(sqlField.include()).contains(operationType)) {
                        continue;
                    }
                }
                if (operationType != null && ArrayUtils.isNotEmpty(sqlField.exclude())) {
                    if (Arrays.asList(sqlField.exclude()).contains(operationType)) {
                        continue;
                    }
                }
                String sqlFieldName = sqlField.name();
                if (StringUtils.isEmpty(sqlFieldName)) {
                    result.put(field.getName(), field.getName());
                } else {
                    if (fieldKey) {
                        result.put(field.getName(), sqlFieldName);
                    } else {
                        result.put(sqlFieldName, field.getName());
                    }
                }
            }
        }

        return result;
    }

    public static Map<String, String> getFieldValueMap(Object bean, OperationType operationType, boolean... nullValueEnabled) {
        return getKeyValueMap(bean, operationType, true, nullValueEnabled);
    }

    public static Map<String, String> getColumnValueMap(Object bean, OperationType operationType, boolean... nullValueEnabled) {
        return getKeyValueMap(bean, operationType, false, nullValueEnabled);
    }

    private static Map<String, String> getKeyValueMap(Object bean, OperationType operationType, boolean fieldNotColumn, boolean... nullValueEnabled) {
        Map<String, String> beanMap = BeanUtil.beanToMap(bean);
        Map<String, String> fieldMap = getFieldColumnMap(bean.getClass(), operationType);

        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : beanMap.entrySet()) {
            // 根据注解字段过滤
            if (!fieldMap.containsKey(entry.getKey())) {
                continue;
            }
            // 根据空值过滤
            if (entry.getValue() == null && (ArrayUtils.isEmpty(nullValueEnabled) || !nullValueEnabled[0])) {
                continue;
            }
            String key = fieldNotColumn ? entry.getKey() : fieldMap.get(entry.getKey());
            result.put(key, entry.getValue());
        }

        return result;
    }

    public static String getPrimaryKeyColumnName(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            SQLField sqlField = field.getAnnotation(SQLField.class);
            if (sqlField != null && sqlField.primary()) {
                return StringUtils.isNotEmpty(sqlField.name()) ? sqlField.name() : field.getName();
            }
        }
        return null;
    }

    public static String getTableName(Class<?> clazz) {
        TableName tableName = clazz.getAnnotation(TableName.class);
        if (tableName != null && StringUtils.isNotEmpty(tableName.name())) {
            return tableName.name();
        }
        return null;
    }


    private SqlAnnotationUtil() {
    }

}
