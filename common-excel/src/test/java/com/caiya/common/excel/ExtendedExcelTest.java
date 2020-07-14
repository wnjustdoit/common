package com.caiya.common.excel;

import com.google.common.collect.Lists;
import com.caiya.common.excel.core.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class ExtendedExcelTest {

    private static final Logger logger = LoggerFactory.getLogger(ExtendedExcelTest.class);

    @Test
    void testWritePlainExcel(@TempDir java.nio.file.Path tempDir) throws IOException {
        // 设置工作簿
        ExtendedWorkbook extendedWorkbook = new ExtendedWorkbook("测试导出excel");

        // 设置表格
        ExtendedSheet extendedSheet = new ExtendedSheet(extendedWorkbook);
        // 设置跨行跨列
        List<CellRangeAddress> cellRangeAddresses = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            cellRangeAddresses.add(new CellRangeAddress(0, 1, i, i));
        }
        cellRangeAddresses.add(new CellRangeAddress(3, 5, 0, 0));
        cellRangeAddresses.add(new CellRangeAddress(2, 3, 6, 9));
        cellRangeAddresses.add(new CellRangeAddress(7, 9, 3, 4));
        extendedSheet.setCellRangeAddresses(cellRangeAddresses);
        // 设置列宽和行高
        extendedSheet.getSheet().setColumnWidth(4, 6000);
        extendedSheet.getSheet().setDefaultRowHeight((short) 400);
        // 设置表格数据，与跨行跨列预先对应
        List<List<String>> rowDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                if ((i == 1 && (j == 18 || j == 19))
                        || ((i == 3 || i == 4) && j == 19)) {
                    continue;
                }
                if (i == 3 && j == 4) {
                    row.add(null);
                    continue;
                }
                row.add(i + "-" + j);
            }
            rowDatas.add(row);
        }
        extendedSheet.setRowDatas(rowDatas);

        // 添加表格到工作簿
        extendedWorkbook.addExtendedSheet(extendedSheet);

        // 自定义输出流
        OutputStream outputStream = new FileOutputStream(tempDir.resolve(extendedWorkbook.getFileName() + extendedWorkbook.getSuffix().getValue()).toFile());
        // 写入输出流
        try {
            ExtendedExcelWriter.writerExcel(extendedWorkbook, outputStream);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testWriteRichExcel(@TempDir java.nio.file.Path tempDir) throws IOException {
        // 设置工作簿
        ExtendedWorkbook extendedWorkbook = new ExtendedWorkbook("测试导出excel"/*, ExtendedWorkbook.Suffix.XLSX*/);

        // 设置表格
        ExtendedSheet extendedSheet = new ExtendedSheet(extendedWorkbook, "某某统计报表");
        // 设置跨行跨列
        List<CellRangeAddress> cellRangeAddresses = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            cellRangeAddresses.add(new CellRangeAddress(0, 1, i, i));
        }
        cellRangeAddresses.add(new CellRangeAddress(3, 5, 0, 0));
        cellRangeAddresses.add(new CellRangeAddress(2, 3, 6, 9));
        cellRangeAddresses.add(new CellRangeAddress(7, 9, 3, 4));
        extendedSheet.setCellRangeAddresses(cellRangeAddresses);
        // 设置列宽和行高
        extendedSheet.getSheet().setColumnWidth(4, 6000);
        extendedSheet.getSheet().setDefaultRowHeight((short) 400);
        // 设置表格数据，与跨行跨列预先对应
        List<ExtendedBaseRow> richRowDatas = new ArrayList<>();
        ExtendedSubRow first2CellRow = new ExtendedSubRow();
        CellStyle cellStyle = extendedWorkbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = extendedWorkbook.createFont();
        font.setFontName("黑体");
        // 设置字体大小
        font.setFontHeightInPoints((short) 16);
        // 粗体显示
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        first2CellRow.setExtendedCells(Lists.newArrayList(new ExtendedCell(new CellValue("第一格")), new ExtendedCell(new CellValue("第二格"), cellStyle)));
        // 先设置前两行（特例：跨行了）
        richRowDatas.add(first2CellRow);

        CellStyle cellStyle2 = extendedWorkbook.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle2.setWrapText(true);
        // 这里只演示一个有规则的行列数据块
        List<ExtendedRow> extendedRows = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            ExtendedBaseRow extendedBaseRow;
            if (i == 0) {
                extendedBaseRow = new ExtendedSubRow();
                // 设置不换行，修改此处直接影响结果展示，演示demo便于理解执行机制
//                extendedBaseRow.setNewLine(false);
            } else {
                extendedBaseRow = new ExtendedRow();
            }
            if (i == 5) {
                // 当前行空白单元格的背景会着色，如果需要填充数据单元格，那么设置数据单元格的样式即可
                extendedBaseRow.setCellStyle(cellStyle2);
            }
            List<ExtendedCell> extendedCells = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                ExtendedCell extendedCell = new ExtendedCell(new CellValue(i + "-" + j));
                if (j == 7) {
                    extendedCell.setCellStyle(cellStyle2);
                }
                extendedCells.add(extendedCell);
            }
            extendedBaseRow.setExtendedCells(extendedCells);
            if (extendedBaseRow.getClass().equals(ExtendedSubRow.class)) {
                richRowDatas.add(extendedBaseRow);
            } else if (extendedBaseRow.getClass().equals(ExtendedRow.class)) {
                extendedRows.add((ExtendedRow) extendedBaseRow);
            } else if (extendedBaseRow.getClass().equals(ExtendedRowWapper.class)) {
                // ignore
            } else {
                // ignore
            }
        }
        // 批量设置有规则的数据块
        if (!CollectionUtils.isEmpty(extendedRows)) {
            ExtendedRowWapper extendedRowWapper = new ExtendedRowWapper();
            extendedRowWapper.setExtendedRows(extendedRows);
            richRowDatas.add(extendedRowWapper);
        }
        extendedSheet.setRichRowDatas(richRowDatas);

        // 添加表格到工作簿
        extendedWorkbook.addExtendedSheet(extendedSheet);

        // 自定义输出流
        OutputStream outputStream = new FileOutputStream(tempDir.resolve(extendedWorkbook.getFileName() + extendedWorkbook.getSuffix().getValue()).toFile());
        // 写入输出流
        try {
            ExtendedExcelWriter.writerExcel(extendedWorkbook, outputStream);
            logger.info("excel文件写入临时目录：{}", tempDir.toString());
            testReadExcel(tempDir);
        } catch (Exception e) {
            fail();
        }
    }

//    @Test
//    @Disabled
    void testReadExcel(@TempDir java.nio.file.Path tempDir) throws IOException {
        ExtendedWorkbook extendedWorkbook = ExtendedExcelReader.readExcel(new FileInputStream(tempDir.resolve("测试导出excel.xls").toFile()));
        try {
            logger.info(extendedWorkbook.getExtendedSheets().get(0).getRowDatas().toString());
        } catch (Exception e) {
            fail();
        }
        // 你可能经常转换成自定义的bean对象，即 List<List<String>> 先转换为 List<Map<String, Object>> 再转换为 List<Bean>。
        // 推荐用fastjson处理泛型的方法：List<VO> list = JSON.parseObject("...", new TypeReference<List<VO>>() {});
        // 或者单个对象解析更简单：VO vo = JSON.parseObject("...", VO.class);
        // 同时可能用到的方法：String text = JSON.toJSONString(object);
    }

}
