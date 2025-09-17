package com.neuedu.it.artcreation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.neuedu.it.artcreation.entity.RespEntity;
import com.neuedu.it.artcreation.entity.dto.ContentDTO;
import com.neuedu.it.artcreation.entity.dto.LoginDTO;
import com.neuedu.it.artcreation.entity.pojo.Creation;
import com.neuedu.it.artcreation.entity.pojo.User;
import com.neuedu.it.artcreation.entity.vo.CreationVO;
import com.neuedu.it.artcreation.service.CreationService;
import com.neuedu.it.artcreation.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CreationService creationService;
    @Value("${my.reg_score}")
    private int score;

    @PostMapping("/reg")
    public RespEntity register(@RequestBody User user) {
        user.setScore(score);
        userService.save(user);
        return RespEntity.success("注册成功", null);
    }

    @PostMapping("/login")
    public RespEntity<Map<String,Object>> login(@RequestBody LoginDTO loginDTO){
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.lambda().eq(User::getLoginName, loginDTO.getLoginName()).eq(User::getLoginPwd, loginDTO.getLoginPwd());
        User user=userService.getOne(qw);
        if(user==null)
            return RespEntity.error( "用户名或密码错误", null);
//        user.setLoginPwd(null);
//        JwtBuilder builder = Jwts.builder();
//        builder.setId(UUID.randomUUID().toString())
//                .setIssuedAt(new java.util.Date())
//                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
//                .signWith(SignatureAlgorithm.HS256,"123456");
//        builder.setClaims(
//                Map.of("id",user.getId()
//                        ,"loginName",user.getLoginName()
//                        ,"nickName",user.getNickName()
//                        ,"email",user.getEmail()
//                        ,"score",user.getScore()
//                ));
//        builder.setSubject(user.getId()+"");
//        String jwttoken=builder.compact();
        String jwttoken = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .setSubject(user.getId().toString())
                .claim("id", user.getId())
                .claim("loginName", user.getLoginName())
                .claim("nickName", user.getNickName())
                .claim("email", user.getEmail())
                .claim("score", user.getScore())
                .signWith(SignatureAlgorithm.HS256, "123456")
                .compact();
        System.out.println(jwttoken);
        return RespEntity.success("登录成功", Map.of("user",user,"token",jwttoken));
    }

//    @PostMapping("/reg")
//    public RespEntity register(String loginName, String loginPwd, String nickName, String email) {
//        User user = new User();
//        user.setLoginName(loginName);
//        user.setLoginPwd(loginPwd);
//        user.setNickName(nickName);
//        user.setEmail(email);
//        user.setScore(20);
//        userService.save(user);
//        return new RespEntity("2000", "注册成功", null);
//    }



}
