package com.caiya.common.db.sql;

import com.caiya.common.db.core.sql.Condition;
import com.caiya.common.db.core.sql.LikeMatchEnum;
import com.caiya.common.db.core.sql.OperatorEnum;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * ConditionTests.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/23
 **/
public class ConditionTests extends AbstractConditionTests {

    @Override
    public void testEquals() {
        String result = Condition.newCondition("username", "tomcat").getString();
        assertEquals("username = 'tomcat'", result);
    }


    @Override
    public void testEqualFieldLeft() {
        String result = Condition.newCondition("username", "tomcat")
                .setFieldLeft(false)
                .getString();
        assertEquals("'tomcat' = username", result);
    }

    @Override
    public void testGe() {
        String result = Condition.newCondition("age", OperatorEnum.GE, 18)
                .getString();
        assertEquals("age >= 18", result);
    }


    @Override
    public void testLike() {
        String result = Condition.newCondition("username", OperatorEnum.LIKE, "tomcat")
                .setLikeMatchEnum(LikeMatchEnum.LEFT)
                .getString();
        assertEquals("username LIKE '%tomcat'", result);
    }


    @Override
    public void testBetweenAnd() {
        String result = Condition.newCondition("age", OperatorEnum.BETWEEN_AND)
                .setValue(18)
                .setSecondValue(22)
                .getString();
        assertEquals("age BETWEEN 18 AND 22", result);
    }


    @Override
    public void testIn() {
        String result = Condition.newCondition("id", OperatorEnum.IN)
                .setValue(new Integer[]{1, 3, 13})
                .getString();
        assertEquals("id IN(1, 3, 13)", result);
        String result1 = Condition.newCondition("id", OperatorEnum.IN)
                .setValue(Arrays.asList(1, 3, 13))
                .getString();
        assertEquals("id IN(1, 3, 13)", result1);
    }


    @Override
    public void testIsNotNull() {
        String result = Condition.newCondition("username", OperatorEnum.IS_NOT_NULL)
                .getString();
        assertEquals("username IS NOT NULL", result);
    }

}
