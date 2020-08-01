package com.caiya.common.db.core.sql;

/**
 * 排序方向
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/30
 **/
public enum Direction {

    ASC("ASC"),
    DESC("DESC");

    private final String sign;

    Direction(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
