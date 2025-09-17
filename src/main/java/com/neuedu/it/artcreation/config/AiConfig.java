package com.neuedu.it.artcreation.config;

import com.neuedu.it.artcreation.tool.SqlTool;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
public class AiConfig {
    @Autowired
    private List<McpSyncClient> mcpSyncClients;
    @Autowired
    private OpenAiEmbeddingModel embeddingModel;
    @Bean
    public VectorStore vectorStore() {
        return SimpleVectorStore.builder(embeddingModel).build();
    }
    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder()
                .maxMessages(5).
                build();
    }
    @Bean
    public ChatClient chatClientTxt(OpenAiChatModel openAiChatModel, ChatMemory chatMemory){
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("你是一名小说家，根据作者意图，编写小说，字数限制在200字内")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                        ,new SimpleLoggerAdvisor()
                )
                .build();
    }
    @Bean
    public ChatClient chatTicketClient(OpenAiChatModel openAiChatModel,ChatMemory chatMemory) {
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("你是一位车票查询小助手，请根据我提供的城市查询车票信息")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                        ,new SimpleLoggerAdvisor()
                )
                .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
                .build();
    }


    @Autowired
    SqlTool sqlTool;

    @Bean
    public ChatClient userChatSQLClient(OpenAiChatModel openAiChatModel,ChatMemory chatMemory){
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("请根据我提供的问题生成对应的sql语句，" +
                        "要查询的表名称为creations，注意，表名是creations，不是creation，建表语句如下：" +
                        "CREATE TABLE `creations` (\n" +
                        "  `s_id` int NOT NULL AUTO_INCREMENT,\n" +
                        "  `s_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                        "  `s_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                        "  `s_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                        "  `s_click` int DEFAULT NULL,\n" +
                        "  `s_userid` int DEFAULT NULL,\n" +
                        "  `s_cgy` int DEFAULT NULL,\n" +
                        "  `s_keyword` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                        "  `s_createtime` datetime DEFAULT NULL,\n" +
                        "  `s_imgfile` LONGBLOB NULL,\n" +
                        "  `s_url` varchar NULL,\n" +
                        "  PRIMARY KEY (`s_id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;" +
                        "例如用户要查询用户id为1发表的内容，则返回如下类似语句：SELECT * from creations where s_userid=1," +
                        "只返回sql语句即可，不需要返回其他的话")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                        ,new SimpleLoggerAdvisor()
                )
                .defaultTools(sqlTool)
                .build();
    }

    @Bean
    public ChatClient activityChatClient(OpenAiChatModel openAiChatModel,ChatMemory chatMemory,VectorStore vectorStore) {
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("你是一个活动小助手，请根据我提供的活动信息，回答用户问题,如果不知道，只需要回答暂时无法回答该问题")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                        ,new SimpleLoggerAdvisor()
                        , QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder()
                                        .topK(3)
                                        .similarityThreshold(0.1)
                                        .build()
                                )
                                .build()
                )
                .build();
    }

    @Bean
    public ChatClient adminChatSQLClient(OpenAiChatModel openAiChatModel,ChatMemory chatMemory){
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("请根据我提供的问题生成对应的sql语句，" +
                        "要修改的表名称为creations，注意，表名是creations，不是creation，建表语句如下：" +
                        "CREATE TABLE `creations` (\n" +
                        "  `s_id` int NOT NULL AUTO_INCREMENT,\n" +
                        "  `s_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                        "  `s_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                        "  `s_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                        "  `s_click` int DEFAULT NULL,\n" +
                        "  `s_userid` int DEFAULT NULL,\n" +
                        "  `s_cgy` int DEFAULT NULL,\n" +
                        "  `s_keyword` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                        "  `s_createtime` datetime DEFAULT NULL,\n" +
                        "  `s_imgfile` LONGBLOB NULL,\n" +
                        "  `s_url` varchar NULL,\n" +
                        "  PRIMARY KEY (`s_id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;" +
                        "例如用户要查询用户id为1发表的内容，则返回如下类似语句：SELECT * from creations where s_userid=1," +
                        "只返回sql语句即可，不需要返回其他的话")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                        ,new SimpleLoggerAdvisor()
                )
                .build();
    }


}
