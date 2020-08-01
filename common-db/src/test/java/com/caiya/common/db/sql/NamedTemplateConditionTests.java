package com.caiya.common.db.sql;

import com.caiya.common.db.core.sql.LikeMatchEnum;
import com.caiya.common.db.core.sql.NamedTemplateCondition;
import com.caiya.common.db.core.sql.OperatorEnum;

import static org.junit.Assert.assertEquals;

/**
 * @author wangnan
 * @since 1.0.0, 2020/7/18
 **/
public class NamedTemplateConditionTests extends AbstractConditionTests {


    @Override
    public void testEquals() {
        String result = NamedTemplateCondition.newCondition("username")
                .getString();
        assertEquals("username = :username", result);
    }

    @Override
    public void testEqualFieldLeft() {
        String result = NamedTemplateCondition.newCondition("username")
                .setFieldLeft(false)
                .getString();
        assertEquals(":username = username", result);
    }

    @Override
    public void testGe() {
        String result = NamedTemplateCondition.newCondition("age", OperatorEnum.GE)
                .getString();
        assertEquals("age >= :age", result);
    }

    @Override
    public void testLike() {
        String result = NamedTemplateCondition.newCondition("username", OperatorEnum.LIKE)
                .setLikeMatchEnum(LikeMatchEnum.RIGHT)
                .getString();
        assertEquals("username LIKE :username%", result);
    }

    @Override
    public void testBetweenAnd() {
        String result = NamedTemplateCondition.newCondition("age", "ageStart#ageEnd")
                .setOperatorEnum(OperatorEnum.BETWEEN_AND)
                .getString();
        assertEquals("age BETWEEN :ageStart AND :ageEnd", result);
    }

    @Override
    public void testIn() {
        String result = NamedTemplateCondition.newCondition("id", OperatorEnum.IN, "list")
                .getString();
        assertEquals("id IN(:list)", result);
    }

    @Override
    public void testIsNotNull() {
        String result = NamedTemplateCondition.newCondition("username", OperatorEnum.IS_NOT_NULL)
                .getString();
        assertEquals("username IS NOT NULL", result);
    }


}
