package com.terryrao.admin.basic;

import java.util.List;

public interface BasicMapper<T> {

    void add(T entity);

    List<T> list(T condition);

    int update(T upd);

    int deleteByPrimaryKey(String  primaryKey);

    T selectByPrimaryKey(String key);
}
