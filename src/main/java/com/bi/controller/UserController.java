package com.bi.controller;

import com.bi.entity.User;
import com.bi.page.Page;
import com.bi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/list")
    public String list(Model model){
        return "user/userList";
    }
    @PostMapping("/getList")
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value = "username",required = false,defaultValue = "") String username,
            Page page
    ){
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> queryMap=new HashMap<>();
        queryMap.put("username","%"+username+"%");
        queryMap.put("offset",page.getOffset());
        queryMap.put("pagesize",page.getRows());
        map.put("rows",userService.findList(queryMap));
        map.put("total",userService.getTotal(queryMap));
        return map;
    }
    @PostMapping("/delete")
    @ResponseBody
    public Map<String,String> delete(
            @RequestParam(value = "ids[]",required = true) Long[] ids
    ) {
        Map<String, String> map = new HashMap<>();
        if(ids==null){
            map.put("type","error");
            map.put("msg","请选择要删除的数据");
            return map;
        }
        String idsString="";
        for (Long id:ids){
            idsString+=id+",";
        }
        idsString=idsString.substring(0,idsString.length()-1);
        if(userService.delete(idsString)<=0){
            map.put("type","error");
            map.put("msg","删除失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","删除成功");
        return map;
    }
    @PostMapping("/edit")
    @ResponseBody
    public Map<String,String> edit(User user){

        Map<String,String> map=new HashMap<>();
        if(user==null){
            map.put("type","error");
            map.put("msg","数据绑定错误，请联系开发者");
            return map;
        }
        if (StringUtils.isEmpty(user.getUsername())){
            map.put("type","error");
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(user.getPassword())){
            map.put("type","error");
            map.put("msg","密码不能为空");
            return map;
        }
        User byUserName = userService.findByUserName(user.getUsername());
        if(byUserName!=null){
            if(byUserName.getId()!=user.getId()){
                map.put("type","error");
                map.put("msg","用户名已存在");
                return map;
            }
        }
        if(userService.edit(user)<=0){
            map.put("type","error");
            map.put("msg","修改失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","修改成功");
        return map;
    }
    @PostMapping("/add")
    @ResponseBody
    public Map<String,String> add(User user){

        Map<String,String> map=new HashMap<>();
        if(user==null){
            map.put("type","error");
            map.put("msg","数据绑定错误，请联系开发者");
            return map;
        }
        if (StringUtils.isEmpty(user.getUsername())){
            map.put("type","error");
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(user.getPassword())){
            map.put("type","error");
            map.put("msg","密码不能为空");
            return map;
        }
        User byUserName = userService.findByUserName(user.getUsername());
        if(byUserName!=null){
            map.put("type","error");
            map.put("msg","用户名已存在");
            return map;
        }
        if(userService.add(user)<=0){
            map.put("type","error");
            map.put("msg","添加失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","添加成功");
        return map;
    }
}
