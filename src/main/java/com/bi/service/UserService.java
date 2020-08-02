package com.bi.service;

import com.bi.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    public User findByUserName(String username);
    public int add(User user);
    public abstract List<User> findList(Map<String,Object> queryMap);
    public abstract int getTotal(Map<String,Object> queryMap);
    public int edit(User user);
    public int delete(String ids);
}
