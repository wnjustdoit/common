package com.caiya.common.db.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * AbstractJdbcOperator.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/20
 **/
public abstract class AbstractJdbcOperator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected DataSource dataSource;


    @SuppressWarnings("unchecked")
    protected SqlParameterSource createSqlParameterSource(Object paramObj) {
        return paramObj == null ? null : (paramObj instanceof Map ? new MapSqlParameterSource((Map<String, ?>) paramObj) :
                new BeanPropertySqlParameterSource(paramObj));
    }

    protected SqlParameterSource[] createSqlParameterSources(Object... paramObj) {
        return SqlParameterSourceUtils.createBatch(paramObj);
    }

}
