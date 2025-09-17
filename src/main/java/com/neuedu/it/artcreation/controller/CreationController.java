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
//    @PostMapping("/creation/publish")
//    @Transactional(rollbackFor = Exception.class)
//    public RespEntity<Creation> pulish(HttpServletRequest request,PublishDTO dto) throws IOException {
//        Creation creation = new Creation();
//        BeanUtils.copyProperties(dto, creation);
//        MultipartFile pic = dto.getPic();
//        System.out.println("进入方法");
//        User user = (User) request.getAttribute("curUser");
//        creation.setUserId(user.getId());
//        creation.setClick(0);
//        creation.setCreateTime(new Date());
//        if(!pic.isEmpty()){
//            byte[] imageBytes = pic.getBytes();
//            creation.setImgBytes(imageBytes);
//            creation.setImg(pic.getOriginalFilename());
//        }
//        creationService.save(creation);
//        return RespEntity.success( "发布成功", creation);
//    }

    @PostMapping("/creation/publish")
    @Transactional(rollbackFor = Exception.class)
    public RespEntity<Creation> art(HttpServletRequest request,@RequestBody ArtDTO dto) throws IOException {
//        ImgTool imgTool = new ImgTool();
//        String imgResult = imgTool.saveImageFromUrl(dto.getImageUrl());
        Creation creation = new Creation();
        String imageUrl = dto.getImageUrl();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建HTTP GET请求
        HttpGet httpGet = new HttpGet(imageUrl);
        // 执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            // 检查响应状态
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("响应状态码: " + statusCode);

            if (statusCode >= 200 && statusCode < 300) {
                // 获取响应体
                HttpEntity entity = response.getEntity();
                byte[] imageBytes = EntityUtils.toByteArray(entity);
                creation.setImgBytes(imageBytes);
                // 生成文件名
                String fileName = imageUrl.split("\\?")[0].substring(imageUrl.lastIndexOf("/") + 1);
                String jsonString = "[\"" + fileName + "\"]";
                creation.setImg(jsonString);
            }
        User user = (User)request.getAttribute("curUser");
        creation.setClick(0);
        creation.setUrl(dto.getImageUrl());
        creation.setContent(dto.getContent());
        creation.setCreateTime(new Date());
        creation.setKeyword(dto.getKeyword());
        creation.setTitle(dto.getTitle());
        creation.setUserId(user.getId());
        creation.setUserNickName(user.getNickName());
        creationService.save(creation);
        return RespEntity.success("发布成功",creation);
        }
         finally {
            response.close();
            httpClient.close();
        }
    }
//    @PostMapping("/creation/display")
//    public RespEntity<Creation> display(@RequestBody Creation creation){
}
