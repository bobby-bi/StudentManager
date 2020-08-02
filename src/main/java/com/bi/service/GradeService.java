package com.bi.service;

import com.bi.entity.Grade;
import java.util.List;
import java.util.Map;

public interface GradeService {
    public int add(Grade grade);
    public abstract List<Grade> findList(Map<String,Object> queryMap);
    public abstract List<Grade> findAll();
    public abstract int getTotal(Map<String,Object> queryMap);
    public int edit(Grade grade);
    public int delete(String ids);
}
