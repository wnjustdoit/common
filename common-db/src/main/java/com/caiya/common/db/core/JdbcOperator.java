package com.caiya.common.db.core;

import com.caiya.common.db.core.sql.*;
import com.caiya.common.db.object.OperationType;
import com.caiya.common.db.object.Page;
import com.caiya.common.db.support.*;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JdbcOperator.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/23
 **/
public class JdbcOperator {

    private static final Logger logger = LoggerFactory.getLogger(JdbcOperator.class);

    private final SimpleJdbcOperator simpleJdbcOperator;

    private final NamedJdbcOperator namedJdbcOperator;

    public JdbcOperator(DataSource dataSource) {
        this.simpleJdbcOperator = new SimpleJdbcOperator(dataSource);
        this.namedJdbcOperator = new NamedJdbcOperator(dataSource);
    }

    public int save(Object entity) {
        String tableName = SqlAnnotationUtil.getTableName(entity.getClass());
        Map<String, String> columnValueMap = SqlAnnotationUtil.getColumnValueMap(entity, OperationType.INSERT);
        List<String> fields = new ArrayList<>(columnValueMap.keySet());
        String sqlTemplate = SqlBuilder.createInsert(tableName)
                .withInsensitiveField()
                .withNamed()
                .withFields(fields)
                .build();
        return namedJdbcOperator.insert(sqlTemplate, columnValueMap);
    }

    public int updateByPrimaryKey(Object entity) {
        String tableName = SqlAnnotationUtil.getTableName(entity.getClass());
        Map<String, String> columnValueMap = SqlAnnotationUtil.getColumnValueMap(entity, OperationType.UPDATE);
        List<String> fields = new ArrayList<>(columnValueMap.keySet());
        String primaryKeyName = SqlAnnotationUtil.getPrimaryKeyColumnName(entity.getClass());
        fields.remove(primaryKeyName);
        String sqlTemplate = SqlBuilder.createUpdate(tableName)
                .withInsensitiveField()
                .withNamed()
                .withFields(fields)
                .buildSqlBuilder()
                .where(NamedTemplateCondition.newCondition(primaryKeyName))
                .build();
        return namedJdbcOperator.update(sqlTemplate, columnValueMap);
    }

    public <T> int deleteByPrimaryKey(T primaryKeyValue, Class<?> beanType) {
        String tableName = SqlAnnotationUtil.getTableName(beanType);
        String primaryKeyName = SqlAnnotationUtil.getPrimaryKeyColumnName(beanType);
        String sqlTemplate = SqlBuilder.createDelete(tableName)
                .withInsensitiveField()
                .withNamed()
                .buildSqlBuilder()
                .where(NamedTemplateCondition.newCondition(primaryKeyName))
                .build();
        return namedJdbcOperator.delete(sqlTemplate, ArrayUtils.toMap(new Object[][]{{primaryKeyName, primaryKeyValue}}));
    }

    public <T> Map<String, Object> queryForObjectByPrimaryKey(T primaryKeyValue, Class<?> beanType) {
        String tableName = SqlAnnotationUtil.getTableName(beanType);
        Map<String, String> columnFieldMap = SqlAnnotationUtil.getColumnFieldMap(beanType, OperationType.SELECT);
        List<String> fields = new ArrayList<>(columnFieldMap.keySet());
        String primaryKeyName = SqlAnnotationUtil.getPrimaryKeyColumnName(beanType);
        String sqlTemplate = SqlBuilder.createSelect(tableName)
                .withInsensitiveField()
                .withNamed()
                .withFields(fields)
                .buildSqlBuilder()
                .where(NamedTemplateCondition.newCondition(primaryKeyName))
                .build();
        return namedJdbcOperator.queryForObject(sqlTemplate, ArrayUtils.toMap(new Object[][]{{primaryKeyName, primaryKeyValue}}));
    }

    public <T extends Page> List<Map> queryForListByConditions(T entity, Order... order) {
        String tableName = SqlAnnotationUtil.getTableName(entity.getClass());
        Map<String, String> columnFieldMap = SqlAnnotationUtil.getColumnFieldMap(entity.getClass(), OperationType.SELECT);
        List<String> fields = new ArrayList<>(columnFieldMap.keySet());
        Map<String, String> columnValueMap = SqlAnnotationUtil.getColumnValueMap(entity, OperationType.SELECT);
        List<AbstractCondition<NamedTemplateCondition>> conditions = new ArrayList<>();
        for (Map.Entry<String, String> entry : columnValueMap.entrySet()) {
            conditions.add(new NamedTemplateCondition(entry.getKey()));
        }
        SqlBuilder sqlbuilder = SqlBuilder.createSelect(tableName)
                .withInsensitiveField()
                .withNamed()
                .withFields(fields)
                .buildSqlBuilder()
                .where(conditions.toArray(new AbstractCondition[0]));
        if (ArrayUtils.isNotEmpty(order)) {
            for (int i = 0; i < order.length; i++) {
                if (i == 0) {
                    sqlbuilder.order(order[i]);
                    continue;
                }
                sqlbuilder.orderMore(order[i]);
            }
        }
        sqlbuilder.page(entity.getPageNo() == null ? 1 : entity.getPageNo(), entity.getPageSize() == null ? 10 : entity.getPageSize());
        String sqlTemplate = sqlbuilder.build();
        return namedJdbcOperator.queryForList(sqlTemplate, columnValueMap);
    }

    public <T extends Page> int count(T entity) {
        String tableName = SqlAnnotationUtil.getTableName(entity.getClass());
        Map<String, String> columnValueMap = SqlAnnotationUtil.getColumnValueMap(entity, OperationType.SELECT);
        List<AbstractCondition<NamedTemplateCondition>> conditions = new ArrayList<>();
        for (Map.Entry<String, String> entry : columnValueMap.entrySet()) {
            conditions.add(new NamedTemplateCondition(entry.getKey()));
        }
        String sqlTemplate = SqlBuilder.createSelect(tableName)
                .withInsensitiveField()
                .withNamed()
                .withCount()
                .buildSqlBuilder()
                .where(conditions.toArray(new AbstractCondition[0]))
                .build();
        return Integer.parseInt(String.valueOf(namedJdbcOperator.queryForSingleColumnValue(sqlTemplate, columnValueMap)));
    }


}
