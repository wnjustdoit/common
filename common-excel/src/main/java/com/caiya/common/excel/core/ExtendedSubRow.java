package com.caiya.common.excel.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.SortedMap;

/**
 * 逻辑子行，在某些情况下也可单独成行，比较通用
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExtendedSubRow extends ExtendedBaseRow {

    /**
     * 单元格集合
     */
    protected List<ExtendedCell> extendedCells;

    /**
     * 可排序的单元格集合
     */
    private SortedMap<Object, ExtendedCell> sortedExtendedCells;

}
