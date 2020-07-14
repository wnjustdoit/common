package com.caiya.common.excel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * ExcelWriterTest.
 * <br/>
 * 推荐使用ExtendedXX新类。
 *
 * @author wangnan
 * @since 1.0.0, 2020/5/13
 **/
@Deprecated
public class ExcelWriterTest {

    private static final Logger logger = LoggerFactory.getLogger(ExcelWriterTest.class);

    @Test
    void writeExcel(@TempDir java.nio.file.Path tempDir) {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> row1 = new HashMap<>();
        row1.put("id", 10001);
        row1.put("name", "汤姆");
        Map<String, Object> row2 = new HashMap<>();
        row1.put("id", 10002);
        row1.put("name", "杰克");
        data.add(row1);
        data.add(row2);
        try {
            ExcelWriter.writeExcel(new String[]{"编号#id", "名称#name"}, null, data, null, tempDir.resolve("测试导出excel.xls").toFile());
            logger.info("excel文件写入临时目录：{}", tempDir.toString());
        } catch (Exception e) {
            fail();
        }
    }

}
