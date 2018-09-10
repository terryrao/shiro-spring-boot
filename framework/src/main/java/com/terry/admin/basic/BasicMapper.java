package com.terry.admin.basic;

import java.util.List;

public interface BasicMapper<T> {

    void add(T entity);

    List<T> list(T condition);

    int update(T upd);

    int delete(T del);
}
