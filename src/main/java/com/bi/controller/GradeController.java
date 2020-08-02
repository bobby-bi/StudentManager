package com.bi.controller;

import com.bi.entity.Grade;
import com.bi.entity.User;
import com.bi.page.Page;
import com.bi.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/grade")
@Controller
public class GradeController {
    @Autowired
    private GradeService gradeService;
    @GetMapping("/list")
    public String list(Model model){
        return "grade/gradeList";
    }
    @PostMapping("/getList")
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value = "name",required = false,defaultValue = "") String name,
            Page page
    ){
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> queryMap=new HashMap<>();
        queryMap.put("name","%"+name+"%");
        queryMap.put("offset",page.getOffset());
        queryMap.put("pagesize",page.getRows());
        map.put("rows",gradeService.findList(queryMap));
        map.put("total",gradeService.getTotal(queryMap));
        return map;
    }
    @PostMapping("/edit")
    @ResponseBody
    public Map<String,String> edit(Grade grade){

        Map<String,String> map=new HashMap<>();
        if(grade==null){
            map.put("type","error");
            map.put("msg","数据绑定错误，请联系开发者");
            return map;
        }
        if (StringUtils.isEmpty(grade.getName())){
            map.put("type","error");
            map.put("msg","年级名不能为空");
            return map;
        }
        if(gradeService.edit(grade)<=0){
            map.put("type","error");
            map.put("msg","年级修改失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","年级修改成功");
        return map;
    }
    @PostMapping("/add")
    @ResponseBody
    public Map<String,String> add(Grade grade){

        Map<String,String> map=new HashMap<>();
        if(grade==null){
            map.put("type","error");
            map.put("msg","数据绑定错误，请联系开发者");
            return map;
        }
        if (StringUtils.isEmpty(grade.getName())){
            map.put("type","error");
            map.put("msg","年级名不能为空");
            return map;
        }
        if(gradeService.add(grade)<=0){
            map.put("type","error");
            map.put("msg","年级添加失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","年级添加成功");
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
        try {
            if(gradeService.delete(idsString)<ids.length){
                map.put("type","error");
                map.put("msg","删除失败");
                return map;
            }
        }catch (Exception e){
            map.put("type","error");
            map.put("msg","该年级下存在班级信息,请勿删除");
            return map;
        }

        map.put("type","success");
        map.put("msg","删除成功");
        return map;
    }
}
