package com.bi.controller;

import com.bi.entity.Clazz;
import com.bi.entity.Grade;
import com.bi.page.Page;
import com.bi.service.ClazzService;
import com.bi.service.GradeService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/clazz")
@Controller
public class ClazzController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private ClazzService clazzService;
    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("gradeList",gradeService.findAll());
        model.addAttribute("gradeListJson", JSONArray.fromObject(gradeService.findAll()));
        return "clazz/clazzList";
    }
    @PostMapping("/getList")
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value = "name",required = false,defaultValue = "") String name,
            @RequestParam(value = "gradeId",required = false) Long gradeId,
            Page page
    ){
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> queryMap=new HashMap<>();
        queryMap.put("name","%"+name+"%");
        queryMap.put("offset",page.getOffset());
        queryMap.put("pagesize",page.getRows());
        if(gradeId!=null){
            queryMap.put("gradeId",gradeId);
        }
        map.put("rows",clazzService.findList(queryMap));
        map.put("total",clazzService.getTotal(queryMap));
        return map;
    }
    @PostMapping("/edit")
    @ResponseBody
    public Map<String,String> edit(Clazz clazz){

        Map<String,String> map=new HashMap<>();
        if(clazz==null){
            map.put("type","error");
            map.put("msg","数据绑定错误，请联系开发者");
            return map;
        }
        if (StringUtils.isEmpty(clazz.getName())){
            map.put("type","error");
            map.put("msg","班级名不能为空");
            return map;
        }
        if (clazz.getGradeId()==null){
            map.put("type","error");
            map.put("msg","所属年级不能为空");
            return map;
        }
        if(clazzService.edit(clazz)<=0){
            map.put("type","error");
            map.put("msg","班级修改失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","班级修改成功");
        return map;
    }
    @PostMapping("/add")
    @ResponseBody
    public Map<String,String> add(Clazz clazz){

        Map<String,String> map=new HashMap<>();
        if(clazz==null){
            map.put("type","error");
            map.put("msg","数据绑定错误，请联系开发者");
            return map;
        }
        if(clazz.getGradeId()==null){
            map.put("type","error");
            map.put("msg","请选择所属年级");
            return map;
        }
        if (StringUtils.isEmpty(clazz.getName())){
            map.put("type","error");
            map.put("msg","班级名不能为空");
            return map;
        }
        if(clazzService.add(clazz)<=0){
            map.put("type","error");
            map.put("msg","班级添加失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","班级添加成功");
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
            if(clazzService.delete(idsString)<ids.length){
                map.put("type","error");
                map.put("msg","删除失败");
                return map;
            }
        }catch (Exception e){
            map.put("type","error");
            map.put("msg","该班级下存在学生信息,请勿删除");
            return map;
        }

        map.put("type","success");
        map.put("msg","删除成功");
        return map;
    }
}
