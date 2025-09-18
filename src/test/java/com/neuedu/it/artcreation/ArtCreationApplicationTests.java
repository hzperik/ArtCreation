package com.neuedu.it.artcreation;

import com.google.gson.Gson;
import com.neuedu.it.artcreation.entity.pojo.Creation;
import com.neuedu.it.artcreation.mapper.CreationMapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.List;

@SpringBootTest
class ArtCreationApplicationTests {
    @Autowired
    CreationMapper creationMapper;


    @Test
    void contextLoads() {
        List<Creation> creations=creationMapper.execute("SELECT * FROM creations WHERE s_userid = 1");
        System.out.println(creations);
    }
    @Test
    public void imgUpload() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = Configuration.create(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "I8pdiUqFk2hWbtYj1Kord5zlbrKmnzsRyq5wNa_H";
        String secretKey = "xPszvmk2HLPifVlPFwMvLtJHLbe2y2lt1JD7CYP-";
        String bucket = "aiimgurl";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\Erik\\Desktop\\test.png";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
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
        System.out.println("上传成功");
    }
    @Test
    public void imgUpload2() {
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
        try {
            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
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
        } catch (UnsupportedEncodingException ex) {
            //ignore
        }
        System.out.println("上传成功");
    }

    @Test
    public void imgDownload() throws QiniuException {
// domain   用户 bucket 绑定的下载域名 eg: mock.qiniu.com【必须】
// useHttps 是否使用 https【必须】
// key      下载资源在七牛云存储的 key【必须】
            String domain = "http://t2rqyfusr.hn-bkt.clouddn.com";
            DownloadUrl url = new DownloadUrl(domain,false, "FsP9HlLKKs4mewZBRNLfZXqYv8pB");
            String urlString = url.buildURL();
            System.out.println(urlString);
    }
}
