package com.caiya.common.db.core.sql;

/**
 * like模糊匹配规则
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/18
 **/
public enum LikeMatchEnum {
    /**
     * 左侧匹配
     */
    LEFT("%", ""),
    /**
     * 右侧匹配
     */
    RIGHT("", "%"),
    /**
     * 两侧匹配
     */
    BOTH("%", "%");

    private final String left;

    private final String right;

    LikeMatchEnum(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getResult(String value) {
        return this.left + value + this.right;
    }

    public String getWrapperResult(String value) {
        return "'" + this.left + value + this.right + "'";
    }

}
