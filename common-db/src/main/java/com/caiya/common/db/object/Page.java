package com.caiya.common.db.object;

import java.io.Serializable;

/**
 * 通用分页模型
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/27
 **/
public class Page implements Serializable {

    private static final long serialVersionUID = 8064895973575258038L;

    private Integer pageNo;

    private Integer pageSize;

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    @SuppressWarnings("unchecked")
    public <T extends Page> T pageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return (T) this;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getPageNo(Integer defaultValue) {
        if (defaultValue == null || defaultValue < 1) {
            throw new IllegalArgumentException("defaultValue cannot be null or zero or negative value");
        }
        if (pageNo == null || pageNo < 1) {
            return defaultValue;
        }
        return pageNo;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @SuppressWarnings("unchecked")
    public <T extends Page> T pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return (T) this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPageSize(Integer defaultValue) {
        if (defaultValue == null || defaultValue < 0) {
            throw new IllegalArgumentException("defaultValue cannot be null or negative value");
        }
        if (pageSize == null || pageSize < 0) {
            return defaultValue;
        }
        return pageSize;
    }
}
