package com.caiya.common.db.core.sql;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 条件对象
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/18
 **/
public class Condition<T> extends AbstractCondition<Condition<T>> {

    /**
     * 真实值
     */
    private T value;

    /**
     * 第二个真实值，如：(NOT) BETWEEN [value] AND [secondValue]
     */
    private T secondValue;


    public Condition(String field) {
        super(field);
    }

    public Condition(String field, OperatorEnum operatorEnum) {
        super(field, operatorEnum);
    }

    public Condition(String field, OperatorEnum operatorEnum, T value) {
        super(field, operatorEnum);
        setValue(value);
    }

    public static <T> Condition<T> newCondition(String field) {
        return new Condition<>(field);
    }

    public static <T> Condition<T> newCondition(String field, OperatorEnum operatorEnum) {
        return new Condition<>(field, operatorEnum);
    }

    public static <T> Condition<T> newCondition(String field, OperatorEnum operatorEnum, T value) {
        return new Condition<>(field, operatorEnum, value);
    }

    public static <T> Condition<T> newCondition(String field, T value) {
        return new Condition<>(field, OperatorEnum.EQUALS, value);
    }

    public Condition<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public Condition<T> setSecondValue(T secondValue) {
        this.secondValue = secondValue;
        return this;
    }

    @Override
    public String getString() {
        if (this.value == null && !OPERATOR_NO_VALUES.contains(this.operatorEnum)) {
            if (logger.isWarnEnabled()) {
                logger.warn("field value is null when field is: {}", this.field);
            }
        }

        if (OPERATOR_NORMALS.contains(this.operatorEnum)) {
            if (this.fieldLeft) {
                return this.field + " " + this.operatorEnum.getOperator() + " " + convertValueToPhasePart(this.value);
            }
            return convertValueToPhasePart(this.value) + " " + this.operatorEnum.getOperator() + " " + this.field;
        }
        if (OPERATOR_LIKES.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperator() + " " + this.likeMatchEnum.getWrapperResult(String.valueOf(this.value));
        }
        if (OPERATOR_TWO_SEPARATORS.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperators()[0] + " " + convertValueToPhasePart(this.value) + " " + this.operatorEnum.getOperators()[1] + " " + convertValueToPhasePart(this.secondValue);
        }
        if (OPERATOR_NO_VALUES.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperator();
        }
        if (OPERATOR_FUNCTIONS.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperator() + "(" + convertValueToPhasePart(this.value) + ")";
        }
        if (logger.isErrorEnabled()) {
            logger.error("非法的连接符！当前对象：{}", toString());
        }
        throw new IllegalArgumentException("非法的连接符！");
    }

    private String convertValueToPhasePart(T value) {
        if (value == null) {
            return null;
        }
        if (Number.class.isAssignableFrom(value.getClass())) {
            return String.valueOf(value);
        }
        if (CharSequence.class.isAssignableFrom(value.getClass())) {
            return wrapperStringValue(value);
        }
        if (value.getClass().isArray()) {
            return Arrays.stream((Object[]) value).map(v -> {
                if (Number.class.isAssignableFrom(v.getClass())) {
                    return String.valueOf(v);
                }
                if (CharSequence.class.isAssignableFrom(v.getClass())) {
                    return wrapperStringValue(v);
                }
                throw new IllegalArgumentException("非法的类型");
            }).collect(Collectors.joining(", "));
        }
        if (value instanceof List) {
            return ((List<?>) value).stream().map(v -> {
                if (Number.class.isAssignableFrom(v.getClass())) {
                    return String.valueOf(v);
                }
                if (CharSequence.class.isAssignableFrom(v.getClass())) {
                    return wrapperStringValue(v);
                }
                throw new IllegalArgumentException("非法的类型");
            }).collect(Collectors.joining(", "));
        }
        throw new IllegalArgumentException("非法的类型");
    }

    private String wrapperStringValue(Object value) {
        return "'" + value + "'";
    }

}
