package com.neuedu.it.artcreation.controller;

import com.alibaba.fastjson.JSON;
import com.neuedu.it.artcreation.entity.RespEntity;
import com.neuedu.it.artcreation.entity.dto.ArtDTO;
import com.neuedu.it.artcreation.entity.dto.PublishDTO;
import com.neuedu.it.artcreation.entity.pojo.Creation;
import com.neuedu.it.artcreation.entity.pojo.User;
import com.neuedu.it.artcreation.service.CreationService;
import com.neuedu.it.artcreation.tools.ImgTool;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@RestController
@CrossOrigin
public class CreationController {
    @Autowired
    private CreationService creationService;
    @PostMapping("/creation/publish")
    @Transactional(rollbackFor = Exception.class)
    public RespEntity<Creation> pulish(HttpServletRequest request,PublishDTO dto) throws IOException {
        MultipartFile pic = dto.getPic();
        System.out.println("进入方法");
        User user = (User) request.getAttribute("curUser");
        dto.setUserId(user.getId());
        if(!pic.isEmpty()){
            String dir = getClass().getClassLoader().getResource("").getPath() + UUID.randomUUID();
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String fileName = new Date().getTime() + "_" + pic.getOriginalFilename();
            pic.transferTo(new File(dir+"/"+fileName));
            dto.setImg(fileName);
        }
        dto.setClick(0);
        dto.setCreateTime(new Date());
        Creation creation = new Creation();
        BeanUtils.copyProperties(dto, creation);
        creationService.save(creation);
        return RespEntity.success( "发布成功", creation);
    }

    @PostMapping("/creation/art")
    @Transactional(rollbackFor = Exception.class)
    public RespEntity<Creation> art(HttpServletRequest request,@RequestBody ArtDTO dto) throws IOException {
        ImgTool imgTool = new ImgTool();
        String imgResult = imgTool.saveImageFromUrl(dto.getImageUrl(), dto.getDir());
        User user = (User)request.getAttribute("curUser");
        Creation creation = new Creation();
        creation.setClick(0);
        creation.setCreateTime(new Date());
        creation.setKeyword(dto.getKeyword());
        creation.setTitle(dto.getTitle());
        creation.setUserId(user.getId());
        creation.setUserNickName(user.getNickName());
        String jsonString = "[\""+imgResult+"\"]";
        creation.setContent(jsonString);
        creationService.save(creation);
        return RespEntity.success("发布成功",creation);
    }
}
