package com.neuedu.it.artcreation.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.neuedu.it.artcreation.entity.RespEntity;
import com.neuedu.it.artcreation.entity.dto.ArtDTO;
import com.neuedu.it.artcreation.entity.dto.ContentDTO;
import com.neuedu.it.artcreation.entity.dto.PublishDTO;
import com.neuedu.it.artcreation.entity.pojo.Creation;
import com.neuedu.it.artcreation.entity.pojo.User;
import com.neuedu.it.artcreation.entity.vo.DisplayVO;
import com.neuedu.it.artcreation.entity.vo.CreationVO;
import com.neuedu.it.artcreation.service.CreationService;
import com.neuedu.it.artcreation.tools.ImgTool;
import com.openai.models.Image;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    /*
     * 删除内容
     * */
    @PostMapping("/delete")
    public RespEntity deleteContent(@RequestBody ContentDTO contentDTO){
        QueryWrapper<Creation> wrapper=new QueryWrapper<>();
        wrapper.lambda().eq(Creation::getId,contentDTO.getId());
        creationService.remove(wrapper);
        return RespEntity.success("删除成功",null);
    }
    /*
    修改内容
    */
    @PostMapping("/modify")
    public RespEntity<CreationVO> modifyContent(@RequestBody ContentDTO contentDTO){
        UpdateWrapper<Creation> wrapper=new UpdateWrapper<>();
        wrapper.lambda().eq(Creation::getId,contentDTO.getId());
        Creation creation=creationService.getById(contentDTO.getId());
        creation.setContent(contentDTO.getContent());
        creation.setTitle(contentDTO.getTitle());
        Boolean bool=creationService.update(creation,wrapper);
        if(!bool){
            return RespEntity.error("修改失败",null);
        }
        CreationVO cv=new CreationVO();
        BeanUtils.copyProperties(creation,cv);
        return RespEntity.success("修改成功",cv);
    }
    //展示最新十条数据
    @PostMapping("/display")
    public RespEntity<List<DisplayVO>> display(){
        List<DisplayVO> displayVOs = new ArrayList<>();
        List<Creation> creations = creationService.list();
        int size = creations.size();
        if (size != 0 && size <= 10){
            for (Creation creation : creations){
                DisplayVO displayVO = new DisplayVO();
                displayVO.setTitle(creation.getTitle());
                displayVO.setImgUrl(creation.getUrl());
                displayVOs.add(displayVO);
            }
        }
        else{
            for (int i = 1;i <= size;i++){
                if (size - i < 10) {
                    DisplayVO displayVO = new DisplayVO();
                    displayVO.setTitle(creations.get(i - 1).getTitle());
                    displayVO.setImgUrl(creations.get(i - 1).getUrl());
                    displayVOs.add(displayVO);
                }
            }
        }
        return RespEntity.success("查询成功",displayVOs);
    }
    @PostMapping("/creation/show")
    public RespEntity<List<DisplayVO>> show(HttpServletRequest request){
        List<DisplayVO> displayVOS = new ArrayList<>();
        List<Creation> creations = creationService.list();
        User user = (User)request.getAttribute("curUser");
        for (Creation creation : creations){
            if (creation.getUserId().equals(user.getId())) {
                DisplayVO displayVO = new DisplayVO();
                displayVO.setTitle(creation.getTitle());
                displayVO.setImgUrl(creation.getUrl());
                displayVOS.add(displayVO);
            }
        }
        int size = displayVOS.size();
        if (size > 10) {
            for (int i = 1;i <= size;i++)
                if (size - i >= 10) {
                    displayVOS.remove(i);
                }
        }
        return RespEntity.success("查询成功",displayVOS);
    }
}
