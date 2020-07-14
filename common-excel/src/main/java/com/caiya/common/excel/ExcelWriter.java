package com.caiya.common.excel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 写Excel文件，过时的类。
 * <br/>
 * 注意：
 * 默认支持.xls后缀；如果要支持.xlsx后缀，修改以下两处：
 * <code> Workbook workbook = WorkbookFactory.create(true); </code>
 * 以及 <code> response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx"); </code>（或本地文件的后缀）
 * <br/>
 *
 * @deprecated 已过时，推荐使用{@link ExtendedExcelWriter}替代。
 */
@Deprecated
public final class ExcelWriter {

    private static final Logger logger = LoggerFactory.getLogger(ExcelWriter.class);

    private static final int DEFAULT_COLUMN_WIDTH = 6000;


    public static void writeExcel(String[] titleMixs, String[] titleMixsSecond, List<Map<String, Object>> data, List<CellRangeAddress> cellList, HttpServletResponse response, String fileName) {
        response.reset();
        try {
            // 设置表格名时中文乱码,进行转码（或者 fileName = java.net.URLEncoder.encode(fileName, "UTF-8");）
            fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
        } catch (UnsupportedEncodingException e1) {
            logger.error("fileName transcoding failed, fileName: {}", fileName, e1);
        }
        response.setCharacterEncoding("UTF-8");
        // 设置ContentType：response.setContentType("application/octet-stream;charset=utf-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        try {
            OutputStream outputStream = response.getOutputStream();
            writeExcel(titleMixs, titleMixsSecond, data, cellList, outputStream);
        } catch (IOException e) {
            logger.error("write excel failed, title: {}", titleMixs, e);
        }
    }

    public static void writeExcel(String[] titleMixs, String[] titleMixsSecond, List<Map<String, Object>> data, List<CellRangeAddress> cellList, File file) {
        try {
            writeExcel(titleMixs, titleMixsSecond, data, cellList, new FileOutputStream(file));
        } catch (IOException e) {
            logger.error("write excel failed, title: {}", titleMixs, e);
        }
    }

    private static void writeExcel(String[] titleMixs, String[] titleMixsSecond, List<Map<String, Object>> data, List<CellRangeAddress> cellList, OutputStream outputStream) throws IOException {
        // 生成一个工作簿
        Workbook workbook = WorkbookFactory.create(false);
        try {
            // 截取标题和字段
            String[] titles = new String[titleMixs.length];
            String[] fields = new String[titleMixs.length];
            for (int i = 0; i < titleMixs.length; i++) {
                String[] temp = titleMixs[i].split("#");
                titles[i] = temp[0];
                fields[i] = temp[1];
            }

            // 设置标题单元格样式
            CellStyle titleCellStyle = workbook.createCellStyle();
            titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 设置普通单元格样式
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setWrapText(true);

            // 生成一个表格
            Sheet sheet = workbook.createSheet();

            // 设置跨行跨列
            // 设置：sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));

            // 添加标题行
            Row titleRow = sheet.createRow(0);
            titleRow.setRowStyle(titleCellStyle);
            for (int i = 0; i < titles.length; i++) {
                Cell titleCell = titleRow.createCell(i);
                String title = titles[i];
                titleCell.setCellValue(title);
                titleCell.setCellStyle(titleCellStyle);
                // 设置列间距
                sheet.setColumnWidth(i, DEFAULT_COLUMN_WIDTH);
            }

            // 添加普通行(内容起始行)
            int rowIndex = 1;

            // 第二行
            if (null != titleMixsSecond) {
                Row titleRow2 = sheet.createRow(1);
                titleRow2.setRowStyle(titleCellStyle);
                for (int i = 0; i < titleMixsSecond.length; i++) {
                    Cell titleCell = titleRow2.createCell(i);
                    String title2 = titleMixsSecond[i];
                    titleCell.setCellValue(title2);
                    titleCell.setCellStyle(titleCellStyle);
                    // 设置列间距
                    sheet.setColumnWidth(i, DEFAULT_COLUMN_WIDTH);
                }
                rowIndex++;
            }


            for (Map<String, Object> rowMap : data) {
                Row row = sheet.createRow(rowIndex++);
                int cellIndex = 0;
                for (String field : fields) {
                    Cell cell = row.createCell(cellIndex++);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(rowMap.get(field) == null ? "" : String.valueOf(rowMap.get(field)));
                }
            }

            // 行列合并
            if (CollectionUtils.isNotEmpty(cellList)) {
                for (CellRangeAddress cellRangeAddress : cellList) {
                    sheet.addMergedRegion(cellRangeAddress);
                }
            }
            workbook.write(outputStream);
        } catch (Exception e) {
            logger.error("write excel failed, title: {}", titleMixs, e);
        } finally {
            try {
                outputStream.flush();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(workbook);
        }
    }

}
