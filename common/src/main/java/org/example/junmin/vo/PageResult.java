package org.example.junmin.vo;

import java.util.List;

public class PageResult<T> {

    private List<T> list;

    private long total;

    public PageResult(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public long getTotal() {
        return total;
    }
}