package com.caiya.common.db.core.sql;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Sql builder utility.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/18
 **/
public class SqlBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SqlBuilder.class);

    /**
     * 默认每页大小
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最终构建完成的sql
     */
    private StringBuilder sql = new StringBuilder();

    /**
     * 是否命名化构建sql，格式:":[name]"。默认不使用字段命名，即："?"
     */
    private boolean named;
    /**
     * 字段命名映射
     */
    private Map<String, String> fieldMap;

    public SqlBuilder(boolean... named) {
        if (ArrayUtils.isNotEmpty(named))
            this.named = named[0];
    }

    public SqlBuilder(String sql, boolean... named) {
        this(named);
        this.sql = new StringBuilder(sql);
    }

    public SqlBuilder(StringBuilder sql, boolean... named) {
        this(named);
        this.sql = sql;
    }

    public SqlBuilder(StringBuilder sql, boolean named, Map<String, String> fieldMap) {
        this.sql = sql;
        this.named = named;
        this.fieldMap = fieldMap;
    }

    public static SqlBuilder create(boolean... named) {
        return new SqlBuilder(named);
    }

    public static SqlBuilder create(String sql, boolean... named) {
        return new SqlBuilder(sql, named);
    }

    public static SqlBuilder create(StringBuilder sql, boolean... named) {
        return new SqlBuilder(sql, named);
    }

    public static <T> Insert<T> createInsert(String tableName) {
        return new Insert<>(tableName);
    }

    public static Select createSelect(String tableName) {
        return new Select(tableName);
    }

    public static <T> Update<T> createUpdate(String tableName) {
        return new Update<>(tableName);
    }

    public static Delete createDelete(String tableName) {
        return new Delete(tableName);
    }

    @SuppressWarnings("unchecked")
    private static <T> List<String> extractFields(T object) {
        Map<String, ?> beanMap;
        if (object instanceof Map) {
            beanMap = (Map<String, ?>) object;
        } else {
            try {
                beanMap = BeanUtils.describe(object);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error(e.getMessage(), e);
                return Collections.emptyList();
            }
        }
        return new ArrayList<>(beanMap.keySet());
    }

//    @SuppressWarnings("unchecked")
//    private static List<String> extractValues(Object object) {
//        Map<String, String> beanMap;
//        if (object instanceof Map) {
//            beanMap = (Map<String, String>) object;
//        } else {
//            try {
//                beanMap = BeanUtils.describe(object);
//            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                logger.error(e.getMessage(), e);
//                return Collections.emptyList();
//            }
//        }
//        return new ArrayList<>(beanMap.values());
//    }

    /**
     * ================================================================================
     * 　　　　　　　　　　　　　　　　　　　　　　where 条件构建 start
     * ================================================================================
     */
    public SqlBuilder where(String whereClause) {
        this.sql.append(" WHERE ").append(whereClause);
        return this;
    }

    public SqlBuilder whereMore(String wherePhaseAnd) {
        return whereMore(true, wherePhaseAnd);
    }

    public SqlBuilder whereMoreOr(String wherePhaseOr) {
        return whereMore(false, wherePhaseOr);
    }

    private SqlBuilder whereMore(boolean andNotOr, String wherePhase) {
        String splitter = andNotOr ? "AND" : "OR";
        this.sql.append(" ").append(splitter).append(" ").append(wherePhase);
        return this;
    }

    public SqlBuilder where(AbstractCondition... conditions) {
        if (ArrayUtils.isEmpty(conditions)) {
            return this;
        }
        return where(true, conditions);
    }

    public SqlBuilder whereOr(AbstractCondition... conditions) {
        return where(false, conditions);
    }

    private SqlBuilder where(boolean andNotOr, AbstractCondition... conditions) {
        String splitter = andNotOr ? "AND" : "OR";
        sql.append(" WHERE ");
        sql.append(Arrays.stream(conditions).map(AbstractCondition::getString).collect(Collectors.joining(" " + splitter + " ")));
        return this;
    }

    public SqlBuilder whereMore(AbstractCondition... conditions) {
        return whereMore(true, conditions);
    }

    public SqlBuilder whereMoreOr(AbstractCondition... conditions) {
        return whereMore(false, conditions);
    }

    private SqlBuilder whereMore(boolean andNotOr, AbstractCondition... conditions) {
        sql.append(" ");
        String splitter = andNotOr ? "AND" : "OR";
        sql.append(Arrays.stream(conditions).map(AbstractCondition::getString).collect(Collectors.joining(" " + splitter + " ")));
        return this;
    }

    /**
     * ================================================================================
     * 　　　　　　　　　　　　　　　　　　　　　　where条件构建 end
     * ================================================================================
     */

    public SqlBuilder order(String field, boolean... ascNotDesc) {
        String orderType = ascNotDesc == null ? "" : (ascNotDesc[0] ? " ASC" : " DESC");
        sql.append(" ORDER BY ")
                .append(field)
                .append(orderType);
        return this;
    }

    public SqlBuilder order(Order order) {
        return order(order.getField(), order.getDirection().getSign().equalsIgnoreCase("ASC"));
    }

    public SqlBuilder orderMore(String field, boolean... ascNotDesc) {
        sql.append(", ")
                .append(field)
                .append((ascNotDesc != null && ascNotDesc[0]) ? "" : " DESC");
        return this;
    }

    public SqlBuilder orderMore(Order order) {
        return orderMore(order.getField(), order.getDirection().getSign().equalsIgnoreCase("ASC"));
    }

    public SqlBuilder page(int pageNo, int pageSize) {
        sql.append(" LIMIT ").append((pageNo - 1) * pageSize).append(", ").append(pageSize);
        return this;
    }

    public SqlBuilder page(int pageNo) {
        return page(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * 追加任意sql语句段
     */
    public SqlBuilder append(String sqlPhase) {
        this.sql.append(sqlPhase);
        return this;
    }

    public String build() {
        return sql.toString();
    }

    /**
     * 抽象操作类
     *
     * @param <O> 操作类型，Insert/Select/Update
     * @param <T> 对象类型，可为空
     */
    public abstract static class AbstractOperation<O extends AbstractOperation<O, T>, T> {
        /**
         * 表名
         */
        protected String tableName;
        /**
         * 字段列表
         */
        protected List<String> fields;
        /**
         * 对象参数
         */
        protected T object;
        /**
         * 是否命名化，true 形如：":username"，false 形如："?"
         */
        protected boolean named;
        /**
         * 数据库字段和参数名映射
         */
        protected Map<String, String> columnFieldMap;
        /**
         * 参数个数
         */
        protected int paramNum;
        /**
         * 是否为COUNT(*)语句
         */
        protected boolean countPhase;
        /**
         * 是否字段脱敏，true 形如："`username`"，false 表示不作处理（默认）
         */
        protected boolean insensitiveField;

        /**
         * 设置字段脱敏
         */
        @SuppressWarnings("unchecked")
        public O withInsensitiveField() {
            this.insensitiveField = true;
            return (O) this;
        }

        @SuppressWarnings("unchecked")
        public O withNamed() {
            this.named = true;
            return (O) this;
        }

        /**
         * 获取经过脱敏处理的字段列表
         */
        protected List<String> insensitiveFields() {
            if (!this.insensitiveField) {
                return this.fields;
            }
            if (this.fields == null) {
                return null;
            }
            return this.fields.stream().map(f -> "`" + f + "`").collect(Collectors.toList());
        }
    }

    /**
     * 插入操作
     *
     * @param <T> 对象类型
     */
    public static class Insert<T> extends AbstractOperation<Insert<T>, T> {

        public Insert(String tableName) {
            this.tableName = tableName;
        }

        public Insert<T> withColumnFieldMap(Map<String, String> columnFieldMap) {
            this.columnFieldMap = columnFieldMap;
            return this;
        }

        public Insert<T> withFields(List<String> fields) {
            this.fields = fields;
            return this;
        }

        public Insert<T> withObject(T object) {
            this.object = object;
            return this;
        }

        public Insert<T> withParamNum(int paramNum) {
            this.paramNum = paramNum;
            return this;
        }

        public String build() {
            String sql = "INSERT INTO " + this.tableName;
            if (this.paramNum != 0 && this.named) {
                throw new IllegalArgumentException("请指定要插入的字段列表");
            }
            if (this.fields == null && this.columnFieldMap != null) {
                this.fields = new ArrayList<>(this.columnFieldMap.values());
            }
            if (this.paramNum == 0) {
                if (CollectionUtils.isEmpty(this.fields) && object != null) {
                    this.fields = extractFields(this.object);
                    if (logger.isDebugEnabled()) {
                        logger.debug("extract fields: [{}] from entity: {}", this.fields, this.object);
                    }
                }
                this.paramNum = this.fields.size();
                if (this.paramNum == 0) {
                    throw new IllegalArgumentException("非法的参数数目");
                }
            }

            List<String> insensitiveFields = insensitiveFields();
            sql += "(" + String.join(", ", insensitiveFields) + ")";
            sql += " VALUES(";
            if (this.named) {
                List<String> names;
                if (MapUtils.isNotEmpty(this.columnFieldMap)) {
                    names = fields.stream().map(f -> columnFieldMap.get(f) == null ? f : columnFieldMap.get(f)).collect(Collectors.toList());
                } else {
                    names = fields.stream().map(f -> ":" + f).collect(Collectors.toList());
                }
                sql += String.join(", ", names);
            } else {
                sql += String.join(", ", Collections.nCopies(this.paramNum, "?"));
            }
            sql += ")";

            return sql;
        }
    }

    /**
     * 查询操作
     */
    public static class Select extends AbstractOperation<Select, Void> {

        public Select(String tableName) {
            this.tableName = tableName;
        }

        public Select withFields(String... fields) {
            this.fields = Arrays.asList(fields);
            return this;
        }

        public Select withFields(List<String> fields) {
            this.fields = fields;
            return this;
        }

        public Select withCount() {
            this.countPhase = true;
            return this;
        }

        public SqlBuilder buildSqlBuilder() {
            StringBuilder sql = new StringBuilder("SELECT ");
            if (!this.countPhase) {
                if (CollectionUtils.isEmpty(this.fields)) {
                    sql.append("*");
                } else {
                    this.fields = insensitiveFields();
                    sql.append(String.join(", ", this.fields));
                }
            } else {
                sql.append("COUNT(*)");
            }
            sql.append(" FROM ").append(tableName);

            return new SqlBuilder(sql, this.named);
        }
    }

    /**
     * 更新操作
     *
     * @param <T> 操作对象类型
     */
    public static class Update<T> extends AbstractOperation<Update<T>, T> {

        public Update(String tableName) {
            this.tableName = tableName;
        }

        public Update<T> withColumnFieldMap(Map<String, String> columnFieldMap) {
            this.columnFieldMap = columnFieldMap;
            return this;
        }

        public Update<T> withFields(List<String> fields) {
            this.fields = fields;
            return this;
        }

        public Update<T> withObject(T object) {
            this.object = object;
            return this;
        }

        public SqlBuilder buildSqlBuilder() {
            StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
            if (this.fields == null) {
                this.fields = extractFields(this.object);
            }
            List<String> insensitiveFields = insensitiveFields();
            int index = 0;
            for (String field : insensitiveFields) {
                if (!this.named) {
                    sql.append(field).append(" = ?");
                } else {
                    sql.append(field).append(" = :").append((this.columnFieldMap == null || this.columnFieldMap.get(this.fields.get(index)) == null) ? this.fields.get(index) : this.columnFieldMap.get(this.fields.get(index)));
                }
                if (index != insensitiveFields.size() - 1) {
                    sql.append(", ");
                }
                index++;
            }

            return new SqlBuilder(sql, this.named, this.columnFieldMap);
        }
    }

    public static class Delete extends AbstractOperation<Delete, Void> {

        public Delete(String tableName) {
            this.tableName = tableName;
        }

        public SqlBuilder buildSqlBuilder() {
            StringBuilder sql = new StringBuilder("DELETE FROM ")
                    .append(tableName);
            return new SqlBuilder(sql, this.named);
        }

    }

}
