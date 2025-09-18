package com.neuedu.it.artcreation.tools;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.qiniu.storage.DownloadUrl;
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
    public String imgUpload(byte[] img) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = Configuration.create(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "I8pdiUqFk2hWbtYj1Kord5zlbrKmnzsRyq5wNa_H";
        String secretKey = "xPszvmk2HLPifVlPFwMvLtJHLbe2y2lt1JD7CYP-";
        String bucket = "aiimgurl";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        String fileKey = null;
        try {
            Response response = uploadManager.put(img, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            fileKey = putRet.key;
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                System.err.println(ex.response);
                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                }
            }
        }
        return fileKey;
    }
    public String imgDownload(String fileKey) throws QiniuException {
// domain   用户 bucket 绑定的下载域名 eg: mock.qiniu.com【必须】
// useHttps 是否使用 https【必须】
// key      下载资源在七牛云存储的 key【必须】
        String domain = "http://t2rqyfusr.hn-bkt.clouddn.com";
        DownloadUrl url = new DownloadUrl(domain,false, fileKey);
        String urlString = url.buildURL();
        System.out.println(urlString);
        return urlString;
    }
}
