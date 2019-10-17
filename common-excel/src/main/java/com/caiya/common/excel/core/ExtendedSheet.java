package com.caiya.common.excel.core;

import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据表格
 */
@Data
public class ExtendedSheet {

    /**
     * 表格名称
     */
    private String name;

    /**
     * 单元格集合
     */
    private List<ExtendedCell> extendedCells = new ArrayList<>();

    /**
     * 所属工作簿
     */
    private ExtendedWorkbook extendedWorkbook;

    /**
     * 原生表格，一对一
     */
    private Sheet sheet;

    /**
     * 行数据
     */
    private Map<Integer, Map<Integer, ExtendedCell>> rowMap = new HashMap<>();

    /**
     * 列数据
     */
    private Map<Integer, Map<Integer, ExtendedCell>> columnMap = new HashMap<>();

    /**
     * 跨行跨列
     */
    private List<CellRangeAddress> cellRangeAddresses = new ArrayList<>();

    /**
     * 表格数据集，通用字符串类型，无样式
     */
    private List<List<String>> rowDatas;

    /**
     * sheet内全局标题样式
     */
    private CellStyle titleCellStyle;

    /**
     * sheet内全局内容样式
     */
    private CellStyle contentCellStyle;

    /**
     * 表格数据集，可定义字段类型和单元格样式
     */
    private List<ExtendedBaseRow> richRowDatas;


    public ExtendedSheet(ExtendedWorkbook extendedWorkbook) {
        this.extendedWorkbook = extendedWorkbook;
        this.sheet = this.getExtendedWorkbook()
                .getWorkbook()
                .createSheet();
        // 初始化默认表格样式
        this.initCellStyle();
    }

    public ExtendedSheet(ExtendedWorkbook extendedWorkbook, String name) {
        this.name = name;
        this.extendedWorkbook = extendedWorkbook;
        this.sheet = this.getExtendedWorkbook()
                .getWorkbook()
                .createSheet(this.getName());
        // 初始化默认表格样式
        this.initCellStyle();
    }

    /**
     * 读取excel使用的构造函数
     *
     * @param extendedWorkbook extendedWorkbook
     * @param sheet            sheet
     */
    public ExtendedSheet(ExtendedWorkbook extendedWorkbook, Sheet sheet) {
        this.name = sheet.getSheetName();
        this.extendedWorkbook = extendedWorkbook;
        this.sheet = sheet;
    }

    private void initCellStyle() {
        // 设置默认的标题样式
        this.titleCellStyle = this.getExtendedWorkbook().createCellStyle();
        this.titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        this.titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        this.titleCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        this.titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置默认的内容样式
        this.contentCellStyle = this.getExtendedWorkbook().createCellStyle();
        this.contentCellStyle.setWrapText(true);
    }

    public void addExtendedCell(ExtendedCell extendedCell) {
        this.getExtendedCells().add(extendedCell);
        for (int i = 0; i < extendedCell.getWidth(); i++) {
            for (int j = 0; j < extendedCell.getHeight(); j++) {
                int x = extendedCell.getX() + i;
                int y = extendedCell.getY() + j;
                if (!rowMap.containsKey(y)) {
                    rowMap.put(y, new HashMap<>());
                }
                rowMap.get(y).put(x, extendedCell);
                if (!columnMap.containsKey(x)) {
                    columnMap.put(x, new HashMap<>());
                }
                columnMap.get(x).put(y, extendedCell);
            }
        }
    }

}
