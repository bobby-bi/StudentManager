package com.bi.service;

import com.bi.entity.Clazz;
import com.bi.entity.Grade;

import java.util.List;
import java.util.Map;

public interface ClazzService {
    public int add(Clazz clazz);
    public abstract List<Clazz> findList(Map<String, Object> queryMap);
    public abstract List<Clazz> findAll();
    public abstract int getTotal(Map<String, Object> queryMap);
    public int edit(Clazz clazz);
    public int delete(String ids);
}
