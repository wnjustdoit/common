package com.caiya.common.db.sql;

import com.caiya.common.db.core.sql.LikeMatchEnum;
import com.caiya.common.db.core.sql.OperatorEnum;
import com.caiya.common.db.core.sql.TemplateCondition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TemplateConditionTests.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/18
 **/
public class TemplateConditionTests extends AbstractConditionTests {

    @Override
    public void testGe() {
        String result = TemplateCondition.newCondition("age", OperatorEnum.GE)
                .getString();
        assertEquals("age >= ?", result);
    }

    @Override
    public void testEquals() {
        String result = TemplateCondition.newCondition("name")
                .getString();
        assertEquals("name = ?", result);
    }

    @Override
    public void testEqualFieldLeft() {
        String result = TemplateCondition.newCondition("name")
                .setFieldLeft(false)
                .getString();
        assertEquals("? = name", result);
    }

    @Test
    public void testLike() {
        String result = TemplateCondition.newCondition("name", OperatorEnum.LIKE)
                .setLikeMatchEnum(LikeMatchEnum.RIGHT)
                .getString();
        assertEquals("name LIKE ?%", result);
    }

    @Test
    public void testBetweenAnd() {
        String result = TemplateCondition.newCondition("name", OperatorEnum.BETWEEN_AND)
                .getString();
        assertEquals("name BETWEEN ? AND ?", result);
    }

    @Override
    public void testIn() {
        String result = TemplateCondition.newCondition("id", OperatorEnum.IN)
                .setPlaceHolderNumber(3)
                .getString();
        assertEquals("id IN(?, ?, ?)", result);
    }

    @Override
    public void testIsNotNull() {
        String result = TemplateCondition.newCondition("username", OperatorEnum.IS_NOT_NULL)
                .getString();
        assertEquals("username IS NOT NULL", result);
    }


}
