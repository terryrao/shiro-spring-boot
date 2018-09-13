package com.terryrao.admin.basic;

import com.github.pagehelper.Page;

public interface BasicPageService<T> {

    Page<T> listByPage(T condition, Page<T> page);

}
