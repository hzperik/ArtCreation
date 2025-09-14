package com.neuedu.it.artcreation.controller;
import com.neuedu.it.artcreation.entity.RespEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

}
