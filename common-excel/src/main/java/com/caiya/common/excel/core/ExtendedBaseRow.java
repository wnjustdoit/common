package com.caiya.common.excel.core;

import lombok.Data;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;

/**
 * 基础行抽象
 */
@Data
public abstract class ExtendedBaseRow {

    /**
     * 行样式
     */
    protected CellStyle cellStyle;

    /**
     * 是否换新行
     */
    protected boolean newLine = true;

    /**
     * 设置单元格集合
     *
     * @param extendedCells 单元格集合
     */
    public abstract void setExtendedCells(List<ExtendedCell> extendedCells);

}
