package com.email_writer.email_writer.service;

import com.email_writer.email_writer.dto.EmailRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailLGenerationService {

    private final ChatClient chatClient;

    public EmailLGenerationService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String generateEmailReply(EmailRequest request) {

        String emailContent = request.getEmailContent() != null ? request.getEmailContent() : "";
        String userRecommendation = request.getUserRecommendation() != null ? request.getUserRecommendation() : "";
        String tone = request.getTone() != null ? request.getTone() : "professional";

        PromptTemplate promptTemplate = new PromptTemplate(
                new ClassPathResource("prompts/email-reply-generation/user-email-reply-generation.st")
        );

        String userPrompt = promptTemplate.render(
                Map.of(
                        "emailContent", emailContent,
                        "userRecommendation", userRecommendation,
                        "tone", tone
                )
        );

        PromptTemplate systemPromptTemplate = new PromptTemplate(
                new ClassPathResource("prompts/email-reply-generation/system-email-reply-generation.st")
        );

        String systemPrompt = systemPromptTemplate.render();

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }
}