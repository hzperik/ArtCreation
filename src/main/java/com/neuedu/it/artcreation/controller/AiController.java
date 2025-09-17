package com.neuedu.it.artcreation.controller;
import com.neuedu.it.artcreation.entity.RespEntity;
import com.neuedu.it.artcreation.entity.dto.QuestionDTO;
import com.neuedu.it.artcreation.entity.pojo.Creation;
import com.neuedu.it.artcreation.entity.vo.CreationVO;
import com.neuedu.it.artcreation.mapper.CreationMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class AiController {
    @Autowired
    @Qualifier("chatClientTxt")
    private ChatClient chatClientTxt;
    @PostMapping("/ai/txt")
    public RespEntity<String> aiTxt(String ask) {
        String answer = chatClientTxt.prompt()
                .user(ask)
                .call()
                .content();
        System.out.println(answer);
        return RespEntity.success("成功",answer);
    }
    @PostMapping(value = "/ai/img")
    public RespEntity<String> image(String ask, @RequestParam(value = "num",defaultValue = "1") int num) {
        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-DashScope-Async", "enable");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + System.getenv("ALIYUNAPI_KEY"));
        String requestBody = """
                {
                    "model": "wan2.2-t2i-flash",
                    "input": {
                        "prompt": "@p"
                        },
                    "parameters": {
                        "size": "1024*1024",
                        "n": @n
                }""";
        requestBody = requestBody.replace("@p", ask).replace("@n", num + "");
        // 创建请求实体
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        // 发送 POST 请求并返回结果
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://dashscope.aliyuncs.com/api/v1/services/aigc/text2image/image-synthesis",
                requestEntity,
                String.class
        );
        return RespEntity.success("成功",response.getBody());
    }
    @RequestMapping(value = "/ai/imgstatus")
    public RespEntity<String> isFinish(String taskId) {
        System.out.println("进入isFinish");
        // 创建RestTemplate实例
        RestTemplate restTemplate = new RestTemplate();
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + System.getenv("ALIYUNAPI_KEY"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // 发送GET请求获取结果
        String url = "https://dashscope.aliyuncs.com/api/v1/tasks/" + taskId;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        return RespEntity.success("成功",response.getBody());
    }


    @Autowired
    @Qualifier("userChatSQLClient")
    private ChatClient userChatSQLClient;

    @Autowired
    private CreationMapper creationMapper;

    /*
     * ai查询sql
     * */
    @PostMapping("/creation/sql")
    public RespEntity<List<Creation>> createSQL(@RequestBody QuestionDTO questionDTO){
        String ask=questionDTO.getQuestion();
        String answer = userChatSQLClient.prompt()
                .user(ask)
                .call()
                .content();
        if(answer.startsWith("UPDATE")
                || answer.startsWith("DELETE")
                || answer.startsWith("INSERT")){
            return RespEntity.error("权限不足，无法执行",null);
        }
        List<Creation> creations = creationMapper.execute(answer);
        return RespEntity.success("成功",creations);
    }

    /*
     * ai查询活动
     * */
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    @Qualifier("activityChatClient")
    private ChatClient activityChatClient;
    @PostMapping("/activity/ask")
    public RespEntity<String> searchActivity(@RequestBody QuestionDTO questionDTO){
        String ask=questionDTO.getQuestion();
        String answer = activityChatClient.prompt()
                .user(ask)
                .call()
                .content();
        return RespEntity.success("成功", answer);


    }

    /*
     * 上传活动文件
     * */
    @PostMapping("/activity/upload")
    public RespEntity<String> upload(MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return RespEntity.error("上传失败", "{\"msg\":\"文件为空\"}");
        }

        String projectRoot = System.getProperty("user.dir");
        String vectorModelDir = projectRoot + File.separator + "vectorModel";
        File dir = new File(vectorModelDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = file.getOriginalFilename() + ".json";
        File ff = new File(dir, fileName);

        if(ff.exists()){
            ((SimpleVectorStore)vectorStore).load(ff);
            return RespEntity.error("上传失败", "{\"msg\":\"文件已存在\"}");
        }
        String path= dir.getAbsolutePath() + File.separator + file.getOriginalFilename();
        String pdfFilePath = "file:\\" + path;
        file.transferTo(new File(path));
        PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdfFilePath,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1)
                        .build());

        List<Document> documents = pdfDocumentReader.read();
        vectorStore.add(documents);
        ((SimpleVectorStore)vectorStore).save(ff);
        return RespEntity.success("成功", "{\"msg\":\"上传成功\"}");
    }

    @Autowired
    @Qualifier("adminChatSQLClient")
    private ChatClient adminChatSQLClient;
    /*
     * ai修改数据库
     * */
    @PostMapping("/admin/sql")
    public RespEntity modifySQL(@RequestBody QuestionDTO questionDTO){
        String ask=questionDTO.getQuestion();
        String answer = adminChatSQLClient.prompt()
                .user(ask)
                .call()
                .content();
        creationMapper.execute1(answer);
        return RespEntity.success("成功",null);
    }
}
