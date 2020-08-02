package com.bi.controller;

import com.bi.entity.Student;
import com.bi.entity.User;
import com.bi.service.StudentService;
import com.bi.service.UserService;
import com.bi.util.CpachaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    @RequestMapping("/index")
    public String index(){
        return "system/index";
    }
    //登录页面
    @GetMapping("/login")
    public String login(Model model){
        return "system/login";
    }
    //注销登录
    @GetMapping("/loginOut")
    public String loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "redirect:login";
    }
    //登录表达提交
    @PostMapping("/login")
    @ResponseBody
    public Map<String,String> login(
            @RequestParam(value = "username",required = false) String username,
            @RequestParam(value = "password",required = false) String password,
            @RequestParam(value = "vcode",required = false) String vcode,
            @RequestParam(value = "type",required = true) int type,
            HttpServletRequest request
    ){
        Map<String,String> map=new HashMap<>();
        if(StringUtils.isEmpty(username)){
            map.put("type","error");
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("type","error");
            map.put("msg","密码不能为空");
            return map;
        }
        if(StringUtils.isEmpty(vcode)){
            map.put("type","error");
            map.put("msg","验证码不能为空");
            return map;
        }
        String loginCpacha = (String)request.getSession().getAttribute("loginCpacha");
        if(StringUtils.isEmpty(loginCpacha)){
            map.put("type","error");
            map.put("msg","长时间未操作，验证码失效，请刷新后再试！");
            return map;
        }
        if(!vcode.toUpperCase().equals(loginCpacha.toUpperCase())){
            map.put("type","error");
            map.put("msg","验证码错误");
            return map;
        }
        request.getSession().removeAttribute("loginCpacha");
        //管理员
        if(type==1){
            User user = userService.findByUserName(username);
            if(user==null){
                map.put("type","error");
                map.put("msg","不存在该用户");
                return map;
            }
            if(!password.equals(user.getPassword())){
                map.put("type","error");
                map.put("msg","密码错误");
                return map;
            }
            request.getSession().setAttribute("user",user);
        }
        //学生
        if(type==2){
            Student student = studentService.findByUserName(username);
            if(student==null){
                map.put("type","error");
                map.put("msg","不存在该用户");
                return map;
            }
            if(!password.equals(student.getPassword())){
                map.put("type","error");
                map.put("msg","密码错误");
                return map;
            }
            request.getSession().setAttribute("user",student);
        }
        request.getSession().setAttribute("userType",type);
        map.put("type","success");
        map.put("msg","登录成功!");
        return map;
    }
    //显示验证码
    @GetMapping("/get_cpacha")
    public void getCpacha(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "vl",required = false,defaultValue = "4") Integer vl,
                          @RequestParam(value = "w",required = false,defaultValue = "98") Integer w,
                          @RequestParam(value = "h",required = false,defaultValue = "33") Integer h
    ) throws IOException {

        CpachaUtil cpachaUtil=new CpachaUtil(vl,w,h);
        String s = cpachaUtil.generatorVCode();
        request.getSession().setAttribute("loginCpacha",s);
        BufferedImage image = cpachaUtil.generatorRotateVCodeImage(s, true);
        ImageIO.write(image,"gif",response.getOutputStream());
    }
}
