package com.caiya.common.db.core.sql;

import java.util.Collections;

/**
 * 模板条件
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/18
 **/
public class TemplateCondition extends AbstractCondition<TemplateCondition> {

    /**
     * 变量占位符
     */
    private static final String PLACE_HOLDER = "?";

    /**
     * 占位符的个数
     */
    private int placeHolderNumber = 1;


    public TemplateCondition(String field) {
        super(field);
    }

    public TemplateCondition(String field, OperatorEnum operatorEnum) {
        super(field, operatorEnum);
    }

    public static TemplateCondition newCondition(String field) {
        return new TemplateCondition(field);
    }

    public static TemplateCondition newCondition(String field, OperatorEnum operatorEnum) {
        return new TemplateCondition(field, operatorEnum);
    }

    public TemplateCondition setOperatorEnum(OperatorEnum operatorEnum) {
        this.operatorEnum = operatorEnum;
        return this;
    }

    public TemplateCondition setPlaceHolderNumber(int placeHolderNumber) {
        this.placeHolderNumber = placeHolderNumber;
        return this;
    }

    public TemplateCondition setFieldLeft(boolean fieldLeft) {
        this.fieldLeft = fieldLeft;
        return this;
    }

    public TemplateCondition setLikeMatchEnum(LikeMatchEnum likeMatchEnum) {
        this.likeMatchEnum = likeMatchEnum;
        return this;
    }


    @Override
    public String getString() {
        if (OPERATOR_NORMALS.contains(this.operatorEnum)) {
            if (this.fieldLeft) {
                return this.field + " " + this.operatorEnum.getOperator() + " " + PLACE_HOLDER;
            }
            return PLACE_HOLDER + " " + this.operatorEnum.getOperator() + " " + this.field;
        }
        if (OPERATOR_LIKES.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperator() + " " + this.likeMatchEnum.getResult(PLACE_HOLDER);
        }
        if (OPERATOR_TWO_SEPARATORS.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperators()[0] + " " + PLACE_HOLDER + " " + this.operatorEnum.getOperators()[1] + " " + PLACE_HOLDER;
        }
        if (OPERATOR_NO_VALUES.contains(this.operatorEnum)) {
            return this.field + " " + this.operatorEnum.getOperator();
        }
        if (OPERATOR_FUNCTIONS.contains(this.operatorEnum)) {
            String valueStr = convertValueTemplateToPhasePart();
            return this.field + " " + this.operatorEnum.getOperator() + "(" + valueStr + ")";
        }
        if (logger.isErrorEnabled()) {
            logger.error("非法的连接符！当前对象：{}", toString());
        }
        throw new IllegalArgumentException("非法的连接符！");
    }

    private String convertValueTemplateToPhasePart() {
        return String.join(", ", Collections.nCopies(this.placeHolderNumber, PLACE_HOLDER));
    }

}
