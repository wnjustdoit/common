package com.caiya.common.db.configuration;

import com.caiya.common.db.core.JdbcOperator;
import com.caiya.common.db.core.NamedJdbcOperator;
import com.caiya.common.db.core.SimpleJdbcOperator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * DatabaseConfiguration.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/27
 **/
@Configuration
public class DatabaseConfiguration {


    @Bean
    public SimpleJdbcOperator simpleJdbcOperator(DataSource dataSource) {
        return new SimpleJdbcOperator(dataSource);
    }

    @Bean
    public NamedJdbcOperator namedJdbcOperator(DataSource dataSource) {
        return new NamedJdbcOperator(dataSource);
    }

    @Bean
    public JdbcOperator jdbcOperator(DataSource dataSource) {
        return new JdbcOperator(dataSource);
    }

}
