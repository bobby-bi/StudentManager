package com.bi.interceptor;


import com.bi.entity.User;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
//        System.out.println(requestURI);
        Object user =  request.getSession().getAttribute("user");
        if(user==null){
            System.out.println("未登录");

            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                Map<String,String> map=new HashMap<>();
                map.put("type","error");
                map.put("msg","登录状态已失效,请重新登录");
                response.getWriter().write(JSONObject.fromObject(map).toString());
                return false;
            }
            System.out.println("被拦截了");
            response.sendRedirect(request.getContextPath()+"/system/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
