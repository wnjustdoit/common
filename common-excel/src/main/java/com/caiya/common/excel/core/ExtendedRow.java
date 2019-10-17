package com.caiya.common.excel.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 标准数据行
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExtendedRow extends ExtendedSubRow {

    /**
     * 逻辑子数据行集合，它们之间不换行，坐标y轴值相同，但高度可能不同（由poi写excel特性决定）
     */
    private List<ExtendedSubRow> extendedSubRows;


}
