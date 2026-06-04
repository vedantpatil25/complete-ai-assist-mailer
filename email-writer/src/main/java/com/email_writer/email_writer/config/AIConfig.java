package com.email_writer.email_writer.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Value("${ai.maxTokens}")
    private int maximumToken;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(ChatOptions.builder()
                        .maxTokens(maximumToken)
                        .build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
