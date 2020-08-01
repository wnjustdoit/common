package com.caiya.common.db.core;

import com.caiya.common.db.core.sql.SqlBuilder;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * SimpleJdbcOperator, using {@link JdbcTemplate}
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/17
 **/
public class SimpleJdbcOperator extends AbstractJdbcOperator {


    private final JdbcTemplate jdbcTemplate;

    public SimpleJdbcOperator(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    private SimpleJdbcInsert createSimpleJdbcInsert() {
        return new SimpleJdbcInsert(dataSource);
    }

    /**
     * 获取字段名
     *
     * @param tableName 表名
     * @return 字段名数组
     */
    public String[] getColumnNames(String tableName) {
        String sql = SqlBuilder.createSelect(tableName).buildSqlBuilder().page(1, 0).build();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        return sqlRowSet.getMetaData().getColumnNames();
    }

    private int insert(@NonNull String tableName, @Nullable String generatedKey, @Nullable List<String> fields, @NonNull SqlParameterSource paramSource) {
        SimpleJdbcInsert simpleJdbcInsert = createSimpleJdbcInsert()
                .withTableName(tableName);
        if (!StringUtils.isEmpty(generatedKey)) {
            simpleJdbcInsert.usingGeneratedKeyColumns(generatedKey);
        }
        if (!CollectionUtils.isEmpty(fields)) {
            simpleJdbcInsert.usingColumns(fields.toArray(new String[0]));
        }
        return simpleJdbcInsert.execute(paramSource);
    }

    /**
     * 单条插入
     *
     * @param tableName    表名
     * @param generatedKey 自增长字段名
     * @param fields       插入的字段列表
     * @param paramObj     插入的对象或Map
     * @return 影响行数
     */
    public int insert(@NonNull String tableName, @Nullable String generatedKey, @Nullable List<String> fields, @NonNull Object paramObj) {
        return insert(tableName, generatedKey, fields, createSqlParameterSource(paramObj));
    }

    /**
     * 单条插入
     *
     * @param tableName 表名
     * @param fields    插入的字段列表
     * @param paramObj  插入的对象或Map
     * @return 影响行数
     */
    public int insert(@NonNull String tableName, @Nullable List<String> fields, @NonNull Object paramObj) {
        return insert(tableName, null, fields, createSqlParameterSource(paramObj));
    }

    /**
     * 单条插入
     *
     * @param tableName 表名
     * @param paramObj  插入的对象或Map
     * @return 影响行数
     */
    public int insert(@NonNull String tableName, @NonNull Object paramObj) {
        return insert(tableName, null, null, createSqlParameterSource(paramObj));
    }

    /**
     * 单条插入
     *
     * @param simpleJdbcInsert simpleJdbcInsert对象
     * @param paramObj         插入的对象或Map
     * @return 影响行数
     */
    public int insert(@NonNull SimpleJdbcInsert simpleJdbcInsert, @NonNull Object paramObj) {
        return simpleJdbcInsert.execute(createSqlParameterSource(paramObj));
    }

    private Number insertAndReturnKey(@NonNull String tableName, @Nullable String generatedKey, @Nullable List<String> fields, @NonNull SqlParameterSource paramSource) {
        SimpleJdbcInsert simpleJdbcInsert = createSimpleJdbcInsert()
                .withTableName(tableName);
        if (!StringUtils.isEmpty(generatedKey)) {
            simpleJdbcInsert.usingGeneratedKeyColumns(generatedKey);
        }
        if (!CollectionUtils.isEmpty(fields)) {
            simpleJdbcInsert.usingColumns(fields.toArray(new String[0]));
        }
        return simpleJdbcInsert.executeAndReturnKey(paramSource);
    }

    /**
     * 单条插入并返回自增长字段
     *
     * @param tableName    表名
     * @param generatedKey 自增长字段名
     * @param fields       插入的字段列表
     * @param paramObj     插入的对象或Map
     * @return 影响行数
     */
    public Number insertAndReturnKey(@NonNull String tableName, @Nullable String generatedKey, @Nullable List<String> fields, @NonNull Object paramObj) {
        return insertAndReturnKey(tableName, generatedKey, fields, createSqlParameterSource(paramObj));
    }

    private int[] insertBatch(@NonNull String tableName, @Nullable String generatedKey, @Nullable List<String> fields, @NonNull SqlParameterSource... paramSource) {
        SimpleJdbcInsert simpleJdbcInsert = createSimpleJdbcInsert()
                .withTableName(tableName);
        if (!StringUtils.isEmpty(generatedKey)) {
            simpleJdbcInsert.usingGeneratedKeyColumns(generatedKey);
        }
        if (!CollectionUtils.isEmpty(fields)) {
            simpleJdbcInsert.usingColumns(fields.toArray(new String[0]));
        }
        return simpleJdbcInsert.executeBatch(paramSource);
    }

    /**
     * 批量插入
     */
    public <T> int[] insertBatch(@NonNull String tableName, @Nullable String generatedKey, @Nullable List<String> fields, @NonNull List<T> paramObjs) {
        return insertBatch(tableName, generatedKey, fields, createSqlParameterSources(paramObjs));
    }

    /**
     * 批量更新
     */
    public <T> void batchUpdate(@NonNull String sqlTemplate, @NonNull List<String> fields, @NonNull List<T> paramObjs, int batchSize) {
        SqlParameterSource[] paramSources = createSqlParameterSources(paramObjs);
        jdbcTemplate.batchUpdate(sqlTemplate, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                int index = 0;
                for (String field : fields) {
                    ps.setObject(index, SqlParameterSourceUtils.getTypedValue(paramSources[index], field));
                    index++;
                }
            }

            @Override
            public int getBatchSize() {
                return batchSize;
            }
        });
    }

    /**
     * 更新
     */
    public int update(@NonNull String sqlTemplate, @Nullable Object[] args) {
        return jdbcTemplate.update(sqlTemplate, args);
    }

    /**
     * 插入
     */
    public int insert(@NonNull String sqlTemplate, @Nullable Object[] args) {
        return update(sqlTemplate, args);
    }


    /**
     * 删除
     */
    public int delete(@NonNull String sqlTemplate, @Nullable Object[] args) {
        return update(sqlTemplate, args);
    }

}
