package com.terry.admin.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.function.Supplier;

public class PageUtils {

    /**
     * <p>
     * {@see https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md}
     * PageHelper 安全调用
     *
     * @param page      page 参数
     * @param supplier  dao中的List参数
     * @param <T>       泛型参数
     * @param condition 条件
     */
    public static <T> Page<T> getLocalPage(Page<T> page, SupplierList<T> supplier, T condition) {
        if (page == null) {
            return new Page<>();
        } else {
            return PageHelper.startPage(page.getPageNum(), page.getPageSize())
                    .doSelectPage(() -> supplier.get(condition));
        }
    }

}
