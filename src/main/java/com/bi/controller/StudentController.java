package com.bi.controller;

import com.bi.entity.Clazz;
import com.bi.entity.Student;
import com.bi.entity.User;
import com.bi.page.Page;
import com.bi.service.ClazzService;
import com.bi.service.GradeService;
import com.bi.service.StudentService;
import com.bi.util.MyStringUtil;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/student")
@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private ClazzService clazzService;
    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("clazzList",clazzService.findAll());
        model.addAttribute("clazzListJson", JSONArray.fromObject(clazzService.findAll()));
        return "student/studentList";
    }
    @PostMapping("/getList")
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value = "name",required = false,defaultValue = "") String name,
            @RequestParam(value = "clazzId",required = false) Long clazzId,
            HttpServletRequest request,
            Page page
    ){
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> queryMap=new HashMap<>();
        queryMap.put("username","%"+name+"%");
        queryMap.put("offset",page.getOffset());
        queryMap.put("pagesize",page.getRows());
        Object attribute =  request.getSession().getAttribute("userType");
            if("2".equals(attribute.toString())){
                Student loginStudent=(Student) request.getSession().getAttribute("user");
                queryMap.put("username",loginStudent.getUsername());
            }
        if(clazzId!=null){
            queryMap.put("clazzId",clazzId);
        }
        map.put("rows",studentService.findList(queryMap));
        map.put("total",studentService.getTotal(queryMap));
        return map;
    }
    @PostMapping("/edit")
    @ResponseBody
    public Map<String,String> edit(Student student){

        Map<String,String> map=new HashMap<>();
        if(student==null){
            map.put("type","error");
            map.put("msg","数据绑定错误，请联系开发者");
            return map;
        }
        if(student.getClazzId()==null){
            map.put("type","error");
            map.put("msg","请选择所属班级");
            return map;
        }
        if (StringUtils.isEmpty(student.getUsername())){
            map.put("type","error");
            map.put("msg","学生姓名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(student.getPassword())){
            map.put("type","error");
            map.put("msg","密码不能为空");
            return map;
        }
        if(isExist(student.getUsername(),student.getId())){
            map.put("type","error");
            map.put("msg","该姓名已存在");
            return map;
        }
        if(studentService.edit(student)<=0){
            map.put("type","error");
            map.put("msg","学生修改失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","学生修改成功");
        return map;
    }
    @PostMapping("/add")
    @ResponseBody
    public Map<String,String> add(Student student){

        Map<String,String> map=new HashMap<>();
        if(student==null){
            map.put("type","error");
            map.put("msg","数据绑定错误，请联系开发者");
            return map;
        }
        if(student.getClazzId()==null){
            map.put("type","error");
            map.put("msg","请选择所属班级");
            return map;
        }
        if (StringUtils.isEmpty(student.getUsername())){
            map.put("type","error");
            map.put("msg","学生姓名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(student.getPassword())){
            map.put("type","error");
            map.put("msg","密码不能为空");
            return map;
        }
        if(isExist(student.getUsername(),student.getId())){
            map.put("type","error");
            map.put("msg","该姓名已存在");
            return map;
        }
        student.setSn(MyStringUtil.generateSn("S",""));
        if(studentService.add(student)<=0){
            map.put("type","error");
            map.put("msg","学生添加失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","学生添加成功");
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
            if(studentService.delete(idsString)<ids.length){
                map.put("type","error");
                map.put("msg","删除失败");
                return map;
            }
        }catch (Exception e){
            map.put("type","error");
            map.put("msg","该学生下存在其他信息,请勿删除");
            return map;
        }

        map.put("type","success");
        map.put("msg","删除成功");
        return map;
    }
    @PostMapping("/uploadPhoto")
    @ResponseBody
    public Map<String,String> uploadPhoto(MultipartFile photo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,String> map=new HashMap<>();
        response.setCharacterEncoding("UTF-8");
        if(photo==null){
            map.put("type","error");
            map.put("msg","请选择文件");
            return map;
        }
        if(photo.getSize()>10485760){
            map.put("type","error");
            map.put("msg","文件大小超过10M，请上传小于10M的图片！");
            return map;
        }
        String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".") + 1, photo.getOriginalFilename().length());
        if(!"jpg,png,gif,jpeg".contains(suffix.toLowerCase())){
            map.put("type","error");
            map.put("msg","文件格式不正确，请上传jpg,png,gif,jpeg格式的图片！");
            return map;
        }
        String savePath=request.getServletContext().getRealPath("/")+"/upload/";
        File savePathFile=new File(savePath);
        if(!savePathFile.exists()){
            savePathFile.mkdir();
        }
        String filename=new Date().getTime()+ "." + suffix;
        photo.transferTo(new File(savePath+filename));
        map.put("type","success");
        map.put("msg","文件上传成功");
        map.put("src",request.getServletContext().getContextPath()+"/upload/"+filename);
        return map;
    }

    private boolean isExist(String username,Long id){
        Student student = studentService.findByUserName(username);
        if(student!=null){
            if(id==null){
                return true;
            }
            if(student.getId().longValue()!=id.longValue()){
                return true;
            }
        }

            return  false;
    }
}
