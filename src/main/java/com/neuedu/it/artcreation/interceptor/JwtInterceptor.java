package com.neuedu.it.artcreation.interceptor;

import com.alibaba.fastjson.JSON;
import com.neuedu.it.artcreation.entity.RespEntity;
import com.neuedu.it.artcreation.entity.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }
        String jwtToken = request.getHeader("Authorization");
        if(jwtToken==null||jwtToken.equals("")){
            String json = JSON.toJSONString(new RespEntity("4001","未登录",null));
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return false;
        }
        try{
//            jwtToken=jwtToken.substring(7);
            Claims claims = Jwts.parser().setSigningKey("123456").parseClaimsJws(jwtToken).getBody();
            User u = new User();
            u.setId((int)claims.get("id"));
            u.setNickName((String)claims.get("nickName"));
            u.setLoginName((String)claims.get("loginName"));
            u.setEmail((String)claims.get("email"));
            u.setScore((int)claims.get("score"));
            request.setAttribute("curUser",u);//请求作用域，所有controller都能取到,一进一出减少内存的占用
            return true;
        }catch (Exception e){
            String json = JSON.toJSONString(new RespEntity("4001","未登录",null));
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return false;
        }
    }
}
