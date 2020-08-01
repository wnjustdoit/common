package com.caiya.common.db.core;

import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * NamedJdbcOperator, using {@link NamedParameterJdbcTemplate}
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/20
 **/
public class NamedJdbcOperator extends AbstractJdbcOperator {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public NamedJdbcOperator(DataSource dataSource) {
        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    /**
     * 查询单个对象
     *
     * @param sqlTemplate  sql模板
     * @param paramObj     参数对象
     * @param requiredType 返回类型Class
     * @param <T>          返回类型
     * @return 单个对象
     */
    public <T> T queryForObject(@NonNull String sqlTemplate, @Nullable Object paramObj, @NonNull Class<T> requiredType) {
        return queryForObject(sqlTemplate, createSqlParameterSource(paramObj), requiredType);
    }

    /**
     * 查询单个对象，返回Map类型的对象
     *
     * @param sqlTemplate sql模板
     * @param paramObj    参数对象
     * @return 单个对象
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryForObject(@NonNull String sqlTemplate, @Nullable Object paramObj) {
        return queryForObject(sqlTemplate, paramObj, Map.class);
    }

    /**
     * 查询单个值
     *
     * @param sqlTemplate sql模板
     * @param paramObj    参数对象
     * @return 单个值
     */
    @SuppressWarnings("unchecked")
    public Object queryForSingleColumnValue(@NonNull String sqlTemplate, @Nullable Object paramObj) {
        Map<String, Object> kvResult = queryForObject(sqlTemplate, paramObj, Map.class);
        if (MapUtils.isNotEmpty(kvResult)) {
            return kvResult.values().iterator().next();
        }
        return null;
    }

    private <T> T queryForObject(@NonNull String sqlTemplate, @Nullable SqlParameterSource paramSource, @NonNull Class<T> requiredType) {
        return namedParameterJdbcTemplate.queryForObject(sqlTemplate, paramSource, createRowMapper(requiredType));
    }

    @SuppressWarnings("unchecked")
    private <T> RowMapper<T> createRowMapper(Class<T> elementType) {
        return Map.class.isAssignableFrom(elementType) ? (RowMapper<T>) new ColumnMapRowMapper() : new BeanPropertyRowMapper<>(elementType);
    }


    /**
     * 查询对象列表
     *
     * @param sqlTemplate sql模板
     * @param paramObj    参数对象
     * @param elementType 返回类型Class
     * @param <T>         返回类型
     * @return 对象列表
     */
    public <T> List<T> queryForList(@NonNull String sqlTemplate, @Nullable Object paramObj, @NonNull Class<T> elementType) {
        return queryForList(sqlTemplate, createSqlParameterSource(paramObj), elementType);
    }

    /**
     * 查询对象列表，返回Map类型的列表
     *
     * @param sqlTemplate sql模板
     * @param paramObj    参数对象
     * @return 对象列表
     */
    @SuppressWarnings("unchecked")
    public List<Map> queryForList(@NonNull String sqlTemplate, @Nullable Object paramObj) {
        return queryForList(sqlTemplate, paramObj, Map.class);
    }


    private <T> List<T> queryForList(@NonNull String sqlTemplate, @Nullable SqlParameterSource paramSource, @NonNull Class<T> elementType) {
        return namedParameterJdbcTemplate.query(sqlTemplate, paramSource, createRowMapper(elementType));
    }

    /**
     * 插入单条数据
     *
     * @param sqlTemplate sql模板
     * @param paramObj    插入的对象或Map
     * @return 影响行数
     */
    public int insert(@NonNull String sqlTemplate, Object paramObj) {
        return namedParameterJdbcTemplate.update(sqlTemplate, createSqlParameterSource(paramObj));
    }

    /**
     * 更新单条数据
     *
     * @param sqlTemplate sql模板
     * @param paramObj    插入的对象或Map
     * @return 影响行数
     */
    public int update(@NonNull String sqlTemplate, Object paramObj) {
        return insert(sqlTemplate, paramObj);
    }

    /**
     * 删除单条数据
     *
     * @param sqlTemplate sql模板
     * @param paramObj    插入的对象或Map
     * @return 影响行数
     */
    public int delete(@NonNull String sqlTemplate, Object paramObj) {
        return insert(sqlTemplate, paramObj);
    }


}
