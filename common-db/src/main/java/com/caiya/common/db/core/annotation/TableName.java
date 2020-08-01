package com.caiya.common.db.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表名注解
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/23
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableName {

    /**
     * 数据库表名
     */
    String name() default "";

}
