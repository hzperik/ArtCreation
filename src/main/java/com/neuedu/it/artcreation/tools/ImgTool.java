package com.neuedu.it.artcreation.tools;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImgTool {
    public String saveImageFromUrl(String imageUrl) throws IOException {
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
//                Path imagePath = Paths.get(dir, fileName);
//                Files.write(imagePath, imageBytes);

                return fileName;
            }
            return "error";
        } finally {
            // 关闭响应
            response.close();
            httpClient.close();
        }
    }
}
