package com.caiya.common.db.sql;

import com.caiya.common.db.core.sql.OperatorEnum;
import com.caiya.common.db.core.sql.SqlBuilder;
import com.caiya.common.db.core.sql.TemplateCondition;
import com.caiya.common.db.form.AreaForm;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * SqlBuilderTests.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/18
 **/
public class SqlBuilderTests {

    String tableName = "aaa_copy";
    AreaForm area = AreaForm.builder().id(900000001L).areaName("测试90001").cityId(1L).isCustom(0).build();

    @Test
    public void testInsert() {
        String sql = SqlBuilder.createInsert(tableName).withObject(area).build();
        assertEquals("INSERT INTO aaa_copy(areaName, pageNo, isCustom, pageSize, cityId, id, class, others) VALUES(?, ?, ?, ?, ?, ?, ?, ?)", sql);
    }

    @Test
    public void testSelect() {
        String sql = SqlBuilder.createSelect(tableName)
                .withFields(Arrays.asList("id", "area_name"))
                .buildSqlBuilder()
                .where(TemplateCondition.newCondition("is_custom"))
                .order("id", false)
                .page(1)
                .build();
        assertEquals("SELECT id, area_name FROM aaa_copy WHERE is_custom = ? ORDER BY id DESC LIMIT 0, 10", sql);
    }

    @Test
    public void testUpdate() {
        String sql = SqlBuilder.createUpdate(tableName)
                .withObject(area)
                .buildSqlBuilder()
                .where(TemplateCondition.newCondition("title").setOperatorEnum(OperatorEnum.LIKE))
                .build();
        assertEquals("UPDATE aaa_copy SET areaName = ?, pageNo = ?, isCustom = ?, pageSize = ?, cityId = ?, id = ?, class = ?, others = ? WHERE title LIKE %?%", sql);
    }

    @Test
    public void testDelete() {
        String sql = SqlBuilder.createDelete(tableName)
                .buildSqlBuilder()
                .where(TemplateCondition.newCondition("id"))
                .build();
        assertEquals("DELETE FROM aaa_copy WHERE id = ?", sql);
    }


}
