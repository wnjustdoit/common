package com.caiya.common.db.sql;

import org.junit.Test;


/**
 * AbstractConditionTests.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/23
 **/
public abstract class AbstractConditionTests {

    @Test
    public abstract void testEquals();

    @Test
    public abstract void testEqualFieldLeft();

    @Test
    public abstract void testGe();

    @Test
    public abstract void testLike();

    @Test
    public abstract void testBetweenAnd();

    @Test
    public abstract void testIn();

    @Test
    public abstract void testIsNotNull();

}
