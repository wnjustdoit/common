package com.caiya.common.db;

import com.caiya.common.db.core.JdbcOperator;
import com.caiya.common.db.core.sql.Direction;
import com.caiya.common.db.core.sql.Order;
import com.caiya.common.db.form.AreaForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * JdbcOperatorTests.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/30
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class JdbcOperatorTests {

    @Resource
    private JdbcOperator jdbcOperator;

    @Test
    public void tests() {
        save();
        Long id = queryForList();
        count();
        update(id);
        queryForObject(id);
        delete(id);
    }

    private void save() {
        AreaForm areaForm = AreaForm.builder()
                .areaName("测试1")
                .cityId(10000L)
                .build();
        int result = jdbcOperator.save(areaForm);
        assertEquals(1, result);
    }

    private void update(Long id) {
        AreaForm areaForm = AreaForm.builder()
                .areaName("测试2222")
                .cityId(20000L)
                .id(id)
                .build();
        int result = jdbcOperator.updateByPrimaryKey(areaForm);
        assertEquals(1, result);
    }

    private void delete(Long id) {
        int result = jdbcOperator.deleteByPrimaryKey(id, AreaForm.class);
        assertEquals(1, result);
    }

    private void queryForObject(Long id) {
        Map<String, Object> result = jdbcOperator.queryForObjectByPrimaryKey(id, AreaForm.class);
        assertNotNull(result);
    }

    private Long queryForList() {
        AreaForm areaForm = AreaForm.builder()
                .areaName("测试1")
                .cityId(10000L)
                .build()
                .pageNo(1)
                .pageSize(10);
        List<Map> result = jdbcOperator.queryForListByConditions(areaForm, Order.newOrder("id", Direction.DESC));
        assertNotNull(result);
        assertTrue(result.size() > 0);
        return Long.valueOf(result.get(0).get("id").toString());
    }

    private void count() {
        AreaForm areaForm = AreaForm.builder()
                .areaName("测试1")
                .cityId(10000L)
                .build()
                .pageNo(1)
                .pageSize(10);
        int count = jdbcOperator.count(areaForm);
        assertTrue(count > 0);
    }

}
