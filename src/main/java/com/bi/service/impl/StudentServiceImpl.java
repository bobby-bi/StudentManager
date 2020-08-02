package com.bi.service.impl;

import com.bi.dao.StudentMapper;
import com.bi.entity.Student;
import com.bi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentMapper studentMapper;
    @Override
    public int add(Student student) {
        return studentMapper.add(student);
    }

    @Override
    public List<Student> findList(Map<String, Object> queryMap) {
        return studentMapper.findList(queryMap);
    }

    @Override
    public List<Student> findAll() {
        return studentMapper.findAll();
    }

    @Override
    public int getTotal(Map<String, Object> queryMap) {
        return studentMapper.getTotal(queryMap);
    }

    @Override
    public int edit(Student student) {
        return studentMapper.edit(student);
    }

    @Override
    public int delete(String ids) {
        return studentMapper.delete(ids);
    }

    @Override
    public Student findByUserName(String username) {
        return studentMapper.findByUserName(username);
    }
}
