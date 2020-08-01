package com.caiya.common.db.core.sql;

/**
 * 连接符枚举类
 * <br/>
 * 如果为函数的话，sql拼接时后加"(" 和 ")"
 * See https://dev.mysql.com/doc/refman/8.0/en/comparison-operators.html
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/18
 */
public enum OperatorEnum {

    GT(">"),
    GE(">="),
    LT("<"),
    NOT_EQUALS("<>"),
    NOT_EQUALS_OLD("!="),
    LE("<="),
    NEQ("<=>"),
    EQUALS("="),
    BETWEEN_AND("BETWEEN", "AND"),
    COALESCE(true, "COALESCE"),
    GREATEST(true, "GREATEST"),
    IN(true, "IN"),
    INTERVAL(true, "INTERVAL"),
    IS("IS"),
    IS_NOT("IS NOT"),
    IS_NOT_NULL("IS NOT NULL"),
    IS_NULL("IS NULL"),
    ISNULL(true, "ISNULL"),
    LEAST(true, "LEAST"),
    LIKE("LIKE"),
    NOT_BETWEEN_AND("NOT BETWEEN", "AND"),
    NOT_IN(true, "NOT IN"),
    NOT_LIKE("NOT LIKE"),
    STRCMP(true, "STRCMP");

    /**
     * 是否为函数
     */
    private final boolean function;

    /**
     * 连接符或函数名或连接短语列表
     */
    private final String[] operator;

    OperatorEnum(String... operator) {
        this(false, operator);
    }

    OperatorEnum(boolean function, String... operator) {
        this.function = function;
        this.operator = operator;
    }

    public boolean isFunction() {
        return function;
    }

    /**
     * 默认获取第一个字符串
     */
    public String getOperator() {
        return operator[0];
    }

    public String[] getOperators() {
        return operator;
    }
}

