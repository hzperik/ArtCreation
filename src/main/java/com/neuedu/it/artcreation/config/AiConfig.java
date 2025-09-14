package com.neuedu.it.artcreation.config;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
