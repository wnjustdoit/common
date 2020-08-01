package com.caiya.common.db.core.annotation;

import com.caiya.common.db.object.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SQL字段名注解
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/22
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface SQLField {

    /**
     * 是否为主键
     */
    boolean primary() default false;

    /**
     * 数据库字段名
     */
    String name() default "";

    /**
     * 包含的操作
     */
    OperationType[] include() default {};

    /**
     * 排除的操作
     */
    OperationType[] exclude() default {};

}
