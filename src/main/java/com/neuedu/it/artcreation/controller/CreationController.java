package com.neuedu.it.artcreation.controller;

import com.neuedu.it.artcreation.entity.RespEntity;
import com.neuedu.it.artcreation.entity.dto.PublishDTO;
import com.neuedu.it.artcreation.entity.pojo.Creation;
import com.neuedu.it.artcreation.entity.pojo.User;
import com.neuedu.it.artcreation.service.CreationService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public String saveImageFromUrl(String imageUrl, String dir) throws IOException {
        // 创建HttpClient实例
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
                // 生成文件名
                String fileName = imageUrl.split("\\?")[0].substring(imageUrl.lastIndexOf("/") + 1);
                // 保存图片
                Path imagePath = Paths.get(dir, fileName);
                Files.write(imagePath, imageBytes);

                return fileName;
            }
            return null;
        } finally {
            // 关闭响应
            response.close();
            httpClient.close();
        }
    }
    @PostMapping("/creation/publish")
    @Transactional(rollbackFor = Exception.class)
    public RespEntity pulish(HttpServletRequest request,PublishDTO dto) throws IOException {
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
        return new RespEntity("2000", "发布成功", creation);
    }
}
