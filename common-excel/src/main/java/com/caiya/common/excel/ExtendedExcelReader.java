package com.caiya.common.excel;

import com.caiya.common.excel.core.ExtendedBaseRow;
import com.caiya.common.excel.core.ExtendedSheet;
import com.caiya.common.excel.core.ExtendedWorkbook;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class ExtendedExcelReader {

    private static List<List<String>> readSheet(Sheet sheet) {
        List<List<String>> result = new ArrayList<>();
        int startRow = sheet.getFirstRowNum();
        int endRow = sheet.getLastRowNum();
        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            int startCell = row.getFirstCellNum();
            int endCell = row.getLastCellNum();
            List<String> rowValue = new ArrayList<>();
            for (int j = startCell; j <= endCell; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                rowValue.add(cell.getStringCellValue());
            }
            result.add(rowValue);
        }

        return result;
    }

    /**
     * @deprecated 暂时无此需求
     */
    @Deprecated
    @SuppressWarnings("unused")
    private static List<ExtendedBaseRow> readRichSheet(Sheet sheet) {
        return Lists.newArrayList();
    }

    public static ExtendedWorkbook readExcel(InputStream inputStream) throws IOException {
        return readExcel(inputStream, null, null);
    }

    public static ExtendedWorkbook readExcel(InputStream inputStream, String fileName, ExtendedWorkbook.Suffix suffix) throws IOException {
        ExtendedWorkbook extendedWorkbook = new ExtendedWorkbook(inputStream, fileName, suffix);
        Workbook workbook = extendedWorkbook.getWorkbook();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            ExtendedSheet extendedSheet = new ExtendedSheet(extendedWorkbook, sheet);
            extendedSheet.setCellRangeAddresses(sheet.getMergedRegions());
            List<List<String>> rowDatas = readSheet(sheet);
            extendedSheet.setRowDatas(rowDatas);
            extendedWorkbook.addExtendedSheet(extendedSheet);
        }

        return extendedWorkbook;
    }


    private ExtendedExcelReader() {
    }

}
