package com.caiya.common.excel.core;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;

/**
 * 数据单元格，一个单元格由唯一完整数据内容填充，包含N个边长为1的单元格（N>=1）
 */
@Data
public class ExtendedCell {

    /**
     * 横坐标
     */
    private int x;

    /**
     * 纵坐标
     */
    private int y;

    /**
     * 宽度，即平行横坐标的长度
     */
    private int width = 1;

    /**
     * 高度，即平行纵坐标的长度
     */
    private int height = 1;

    /**
     * 是否为基础单元格，即边长为1的单元格
     * <p>
     * 改变宽度和高度，都可能影响该值
     * </p>
     */
    private boolean unit = true;

    /**
     * 原生单元格，一对一
     *
     * @deprecated 暂无使用
     */
    @Deprecated
    private Cell cell;

    /**
     * 单元格值
     */
    private CellValue cellValue;

    /**
     * 单元格样式
     */
    private CellStyle cellStyle;

    public ExtendedCell(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * @deprecated 暂无使用
     */
    @Deprecated
    public ExtendedCell(Cell cell) {
        this.cell = cell;
    }

    public ExtendedCell(CellValue cellValue, CellStyle cellStyle) {
        this.cellValue = cellValue;
        this.cellStyle = cellStyle;
    }

    public ExtendedCell(CellValue cellValue) {
        this.cellValue = cellValue;
    }

    private void setWidth(int width) {
        this.width = width;
        if (width != 1 && this.unit) {
            setUnit(false);
        }
    }

    private void setHeight(int height) {
        this.height = height;
        if (height != 1 && this.unit) {
            setUnit(false);
        }
    }

}
