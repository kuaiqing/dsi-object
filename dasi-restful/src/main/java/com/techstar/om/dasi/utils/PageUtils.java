package com.techstar.om.dasi.utils;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtils {
    public static <T> Page<T> pageOf(List<T> values, Pageable pageable) {
        PagedListHolder<T> holder = new PagedListHolder(values);
        holder.setPage(pageable.getPageNumber());
        holder.setPageSize(pageable.getPageSize());
        return new PageImpl<T>(holder.getPageList(), pageable, values.size());
    }
}
