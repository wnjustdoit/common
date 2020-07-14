package com.caiya.common.excel;

import com.caiya.common.excel.core.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * 写excel文件工具类.
 */
public final class ExtendedExcelWriter {

    private static void writeSheet(ExtendedSheet extendedSheet) {
        initGrid(extendedSheet);
        if (extendedSheet.getRowDatas() != null) {
            writeRows(extendedSheet);
        } else if (extendedSheet.getRichRowDatas() != null) {
            writeRichRows(extendedSheet);
        }
    }

    private static void writeRows(ExtendedSheet extendedSheet) {
        // 获取迭代每行数据的迭代器
        Iterator<List<String>> rowIterator = extendedSheet.getRowDatas().iterator();
        int i = 0;
        while (rowIterator.hasNext()) {
            Row row = extendedSheet.getSheet().createRow(i);
            List<String> rowData = rowIterator.next();
            Iterator<String> colIterator = rowData.iterator();
            int j = 0;
            while (colIterator.hasNext()) {
                // 检查单元格是否非坐标点主单元格
                if ((extendedSheet.getColumnMap().containsKey(j)
                        && extendedSheet.getColumnMap().get(j).containsKey(i)
                        && extendedSheet.getColumnMap().get(j).get(i).getY() != i)
                        || (
                        extendedSheet.getRowMap().containsKey(i)
                                && extendedSheet.getRowMap().get(i).containsKey(j)
                                && extendedSheet.getRowMap().get(i).get(j).getX() != j)) {
                    j++;
                    continue;
                }
                Cell cell = row.createCell(j);
                cell.setCellStyle(extendedSheet.getContentCellStyle());
                cell.setCellValue(colIterator.next());
                j++;
            }
            i++;
        }
    }

    private static void writeRichRows(ExtendedSheet extendedSheet) {
        // 获取迭代每块数据的迭代器
        Iterator<ExtendedBaseRow> rowIterator = extendedSheet.getRichRowDatas().iterator();
        // 当写入数据块对象ExtendedRowWrapper时，用以保存剩余待换行写入的数据集
        Iterator<ExtendedRow> remainRowIterator = null;
        // 行号
        int i = 0;
        // 上次写入的行号，用于不换行的继续写入
        Row lastRow = null;
        // 上次循环的列号，用于不换行的继续写入
        int lastJ = 0;
        do {
            ExtendedBaseRow rowData;
            if (remainRowIterator != null && remainRowIterator.hasNext()) {
                rowData = remainRowIterator.next();
            } else {
                rowData = rowIterator.next();
            }

            int j = 0;
            if (lastJ > 0 ) {
                if (rowData.isNewLine()) {
                    i++;
                } else {
                    j = lastJ;
                }
            }

            Row row;
            if (rowData.isNewLine()) {
                lastRow = row = extendedSheet.getSheet().createRow(i);
            } else {
                row = lastRow;
            }

            if (rowData instanceof ExtendedRow) {
                ExtendedRow extendedRow = (ExtendedRow) rowData;
                lastJ = foreachExtendedRow(extendedSheet, extendedRow, i, row, j);
                setRowStyle(row, extendedRow.getCellStyle());
            } else if (rowData instanceof ExtendedRowWapper) {
                ExtendedRowWapper extendedRowWapper = (ExtendedRowWapper) rowData;
                remainRowIterator = extendedRowWapper.getExtendedRows().iterator();
                ExtendedRow extendedRow = remainRowIterator.next();
                lastJ = foreachExtendedRow(extendedSheet, extendedRow, i, row, j);
                setRowStyle(row, extendedRow.getCellStyle());
            } else if (rowData instanceof ExtendedSubRow) {// less
                ExtendedSubRow extendedSubRow = (ExtendedSubRow) rowData;
                lastJ = foreachCol(extendedSheet, extendedSubRow.getExtendedCells().iterator(), i, row, j);
                setRowStyle(row, extendedSubRow.getCellStyle());
            }

        } while ((remainRowIterator != null && remainRowIterator.hasNext()) || rowIterator.hasNext());

    }

    private static void setRowStyle(Row row, CellStyle cellStyle) {
        if (cellStyle != null && row != null) {
            row.setRowStyle(cellStyle);
        }
    }

    private static void setCellStyle(Cell cell, CellStyle cellStyle) {
        if (cellStyle != null && cell != null) {
            cell.setCellStyle(cellStyle);
        }
    }

    private static int foreachExtendedRow(ExtendedSheet extendedSheet, ExtendedRow extendedRow, int i, Row row, int j) {
        if (extendedRow.getExtendedSubRows() == null) {
            List<ExtendedCell> extendedCells = extendedRow.getExtendedCells();
            return foreachCol(extendedSheet, extendedCells.iterator(), i, row, j);
        } else {
            int lastJ = j;
            for (ExtendedSubRow extendedSubRow : extendedRow.getExtendedSubRows()) {
                lastJ = foreachCol(extendedSheet, extendedSubRow.getExtendedCells().iterator(), i, row, j);
            }
            return lastJ;
        }
    }

    private static int foreachCol(ExtendedSheet extendedSheet, Iterator<ExtendedCell> colIterator, int i, Row row, int j) {
        while (colIterator.hasNext()) {
            // 检查单元格是否非坐标点主单元格
            if ((extendedSheet.getColumnMap().containsKey(j)
                    && extendedSheet.getColumnMap().get(j).containsKey(i)
                    && extendedSheet.getColumnMap().get(j).get(i).getY() != i)
                    || (
                    extendedSheet.getRowMap().containsKey(i)
                            && extendedSheet.getRowMap().get(i).containsKey(j)
                            && extendedSheet.getRowMap().get(i).get(j).getX() != j)) {
                j++;
                continue;
            }
            ExtendedCell extendedCell = colIterator.next();
            Cell cell = row.createCell(j);
            cell.setCellValue(extendedCell.getCellValue().getStringValue());
            setCellStyle(cell, extendedCell.getCellStyle());
            j++;
        }
        return j;
    }

    // 先将跨行列的单元格初始化，没指定的用时初始化
    private static void initGrid(ExtendedSheet extendedSheet) {
        for (CellRangeAddress cellAddress : extendedSheet.getCellRangeAddresses()) {
            // 设置跨行跨列
            extendedSheet.getSheet().addMergedRegion(cellAddress);
            // 设置自定义单元格的坐标和范围
            ExtendedCell extendedCell = new ExtendedCell(cellAddress.getFirstColumn(),
                    cellAddress.getFirstRow(),
                    cellAddress.getLastColumn() - cellAddress.getFirstColumn() + 1,
                    cellAddress.getLastRow() - cellAddress.getFirstRow() + 1);
            extendedSheet.addExtendedCell(extendedCell);
        }
    }

    public static void writerExcel(ExtendedWorkbook extendedWorkbook, OutputStream outputStream) throws IOException {
        for (ExtendedSheet extendedSheet : extendedWorkbook.getExtendedSheets()) {
            writeSheet(extendedSheet);
        }
        extendedWorkbook.getWorkbook().write(outputStream);
    }


    private ExtendedExcelWriter() {
    }
}
