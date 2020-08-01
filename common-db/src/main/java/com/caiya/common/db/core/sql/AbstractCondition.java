package com.caiya.common.db.core.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 条件语句基类
 *
 * @param <C> Condition类型
 * @author wangnan
 * @since 1.0.0, 2020/7/21
 **/
public abstract class AbstractCondition<C extends AbstractCondition<C>> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 常规操作符，例如："username = tomcat"
     */
    protected static final List<OperatorEnum> OPERATOR_NORMALS = Arrays.asList(OperatorEnum.GT, OperatorEnum.GE, OperatorEnum.LT, OperatorEnum.NOT_EQUALS, OperatorEnum.NOT_EQUALS_OLD
            , OperatorEnum.LE, OperatorEnum.NEQ, OperatorEnum.EQUALS, OperatorEnum.IS, OperatorEnum.IS_NOT);
    /**
     * like类操作符，需要结合 {#LikeMatchEnum} 使用（默认为两侧匹配），例如："title LIKE %奶粉%"
     */
    protected static final List<OperatorEnum> OPERATOR_LIKES = Arrays.asList(OperatorEnum.LIKE, OperatorEnum.NOT_LIKE);
    /**
     * 两段的操作符，例如："age BETWEEN 18 AND 60"
     */
    protected static final List<OperatorEnum> OPERATOR_TWO_SEPARATORS = Arrays.asList(OperatorEnum.BETWEEN_AND, OperatorEnum.NOT_BETWEEN_AND);
    /**
     * 不需要值的操作符，例如："description IS NULL"
     */
    protected static final List<OperatorEnum> OPERATOR_NO_VALUES = Arrays.asList(OperatorEnum.IS_NULL, OperatorEnum.IS_NOT_NULL);
    /**
     * 属于函数的操作符，例如："id IN(1, 3, 4, 8, 9)"
     */
    protected static List<OperatorEnum> OPERATOR_FUNCTIONS;

    /**
     * 字段名
     */
    protected String field;

    /**
     * 连接符
     */
    protected OperatorEnum operatorEnum = OperatorEnum.EQUALS;

    /**
     * 是否字段居左，true 如：age > [placeHolder_or_value]；false 如：[placeHolder_or_value] > age
     */
    protected boolean fieldLeft = true;

    /**
     * LIKE匹配规则，默认两侧匹配
     */
    protected LikeMatchEnum likeMatchEnum = LikeMatchEnum.BOTH;

    static {
        OPERATOR_FUNCTIONS = new ArrayList<>();
        for (OperatorEnum operatorEnum : OperatorEnum.values()) {
            if (operatorEnum.isFunction()) {
                OPERATOR_FUNCTIONS.add(operatorEnum);
            }
        }
        OPERATOR_FUNCTIONS = Collections.unmodifiableList(OPERATOR_FUNCTIONS);
    }

    public AbstractCondition(String field) {
        this.field = field;
    }

    public AbstractCondition(String field, OperatorEnum operatorEnum) {
        this.field = field;
        this.operatorEnum = operatorEnum;
    }

    public abstract String getString();

    /**
     * 对mysql关键字的字段进行脱敏
     */
    public void inSensitiveField() {
        if (this.field == null)
            throw new IllegalArgumentException("field为空，也许是调用时机不对");

        this.field = "`" + this.field + "`";
    }


    @SuppressWarnings("unchecked")
    public C setOperatorEnum(OperatorEnum operatorEnum) {
        this.operatorEnum = operatorEnum;
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    public C setLikeMatchEnum(LikeMatchEnum likeMatchEnum) {
        this.likeMatchEnum = likeMatchEnum;
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    public C setFieldLeft(boolean fieldLeft) {
        this.fieldLeft = fieldLeft;
        return (C) this;
    }

}
