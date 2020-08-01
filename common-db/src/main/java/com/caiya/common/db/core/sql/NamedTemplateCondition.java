package com.caiya.common.db.core.sql;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 命名模板条件
 * <br/>
 * 为了简化，可变参数的长度为0或1
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/18
 **/
public class NamedTemplateCondition extends AbstractCondition<NamedTemplateCondition> {

    /**
     * 字段参数名，如："username"，拼接后为：":username"
     */
    private String name;


    public NamedTemplateCondition(String field, String... name) {
        super(field);
        if (ArrayUtils.isNotEmpty(name)) {
            this.name = name[0];
        }
    }

    public NamedTemplateCondition(String field, OperatorEnum operatorEnum, String... name) {
        super(field, operatorEnum);
        if (ArrayUtils.isNotEmpty(name)) {
            this.name = name[0];
        }
    }

    public static NamedTemplateCondition newCondition(String field, String... name) {
        return new NamedTemplateCondition(field, name);
    }

    public static NamedTemplateCondition newCondition(String field, OperatorEnum operatorEnum, String... name) {
        return new NamedTemplateCondition(field, operatorEnum, name);
    }

    @Override
    public String getString() {
        String valuePlaceHolder = ":";
        String secondValuePlaceHolder = ":";
        if (this.name != null) {
            if (this.name.contains("#")) {
                // when two operators
                valuePlaceHolder += this.name.split("#")[0];
                secondValuePlaceHolder += this.name.split("#")[1];
            } else {
                // when specified
                valuePlaceHolder += this.name;
                secondValuePlaceHolder = valuePlaceHolder;
            }
        } else {
            // default
            valuePlaceHolder += this.field;
            secondValuePlaceHolder = valuePlaceHolder;
        }

        if (OPERATOR_NORMALS.contains(this.operatorEnum)) {
            if (this.fieldLeft) {
                return this.field + " " + this.operatorEnum.getOperator() + " " + valuePlaceHolder;
            }
            return valuePlaceHolder + " " + this.operatorEnum.getOperator() + " " + this.field;
        }
        if (OPERATOR_LIKES.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperator() + " " + this.likeMatchEnum.getResult(valuePlaceHolder);
        }
        if (OPERATOR_TWO_SEPARATORS.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperators()[0] + " " + valuePlaceHolder + " " + this.operatorEnum.getOperators()[1] + " " + secondValuePlaceHolder;
        }
        if (OPERATOR_NO_VALUES.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperator();
        }
        if (OPERATOR_FUNCTIONS.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperator() + "(" + valuePlaceHolder + ")";
        }
        if (logger.isErrorEnabled()) {
            logger.error("非法的连接符！当前对象：{}", toString());
        }
        throw new IllegalArgumentException("非法的连接符！");
    }

}
