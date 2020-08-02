package com.bi.dao;

import com.bi.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface StudentMapper {
    public int add(Student student);
    public abstract List<Student> findList(Map<String, Object> queryMap);
    public abstract List<Student> findAll();
    public abstract int getTotal(Map<String, Object> queryMap);
    public int edit(Student student);
    public int delete(String ids);
    public Student findByUserName(String username);
}
