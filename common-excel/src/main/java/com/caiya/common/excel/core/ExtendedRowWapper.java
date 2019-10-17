package com.caiya.common.excel.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 逻辑数据块，含N个数据行(N>=1)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExtendedRowWapper extends ExtendedBaseRow {

    /**
     * 标准数据行集合
     */
    private List<ExtendedRow> extendedRows;


    /**
     * 只能通过{@link #setExtendedRows(List)}设置数据集
     *
     * @param extendedCells 单元格集合
     */
    @Override
    public void setExtendedCells(List<ExtendedCell> extendedCells) {
        throw new UnsupportedOperationException("不支持的操作");
    }

}
