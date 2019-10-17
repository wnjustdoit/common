package com.caiya.common.excel;

import com.caiya.common.excel.core.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelTest {

    private static final Logger logger = LoggerFactory.getLogger(ExcelTest.class);

    @Test
    public void testWritePlainExcel() throws IOException {
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
        OutputStream outputStream = new FileOutputStream(new File("/Users/wangnan/workspace/tmppppp/" + extendedWorkbook.getFileName() + extendedWorkbook.getSuffix().getValue()));
        // 写入输出流
        ExtendedExcelWriter.writerExcel(extendedWorkbook, outputStream);
    }

    @Test
    public void testWriteRichExcel() throws IOException {
        // 设置工作簿
        ExtendedWorkbook extendedWorkbook = new ExtendedWorkbook("测试导出excel");

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
        font.setFontHeightInPoints((short) 16);//设置字体大小
        font.setBold(true);//粗体显示
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        first2CellRow.setExtendedCells(Lists.newArrayList(new ExtendedCell(new CellValue("第一格")), new ExtendedCell(new CellValue("第二格"), cellStyle)));
        richRowDatas.add(first2CellRow);

        CellStyle cellStyle2 = extendedWorkbook.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setWrapText(true);
        List<ExtendedRow> extendedRows = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            ExtendedBaseRow extendedBaseRow;
            if (i == 0) {
                extendedBaseRow = new ExtendedSubRow();
                extendedBaseRow.setNewLine(false);// 设置不换行
            } else {
                extendedBaseRow = new ExtendedRow();
            }
            if (i == 5) {
                extendedBaseRow.setCellStyle(cellStyle2);// 当前行空白单元格的背景会着色，如果需要填充数据单元格，那么设置数据单元格的样式即可
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
                // do nothing
            } else {
                // do nothing
            }
        }
        if (!CollectionUtils.isEmpty(extendedRows)) {
            ExtendedRowWapper extendedRowWapper = new ExtendedRowWapper();
            extendedRowWapper.setExtendedRows(extendedRows);
            richRowDatas.add(extendedRowWapper);
        }
        extendedSheet.setRichRowDatas(richRowDatas);

        // 添加表格到工作簿
        extendedWorkbook.addExtendedSheet(extendedSheet);

        // 自定义输出流
        OutputStream outputStream = new FileOutputStream(new File("/Users/wangnan/workspace/tmppppp/" + extendedWorkbook.getFileName() + extendedWorkbook.getSuffix().getValue()));
        // 写入输出流
        ExtendedExcelWriter.writerExcel(extendedWorkbook, outputStream);
    }

    @Test
    public void testReadExcel() throws IOException {
        ExtendedWorkbook extendedWorkbook = ExtendedExcelReader.readExcel(new FileInputStream(new File("/Users/wangnan/workspace/tmppppp/测试导出excel.xls")));
        logger.info(extendedWorkbook.getExtendedSheets().get(0).getRowDatas().toString());
    }

}
