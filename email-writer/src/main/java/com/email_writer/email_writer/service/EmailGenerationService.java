package com.email_writer.email_writer.service;

import com.email_writer.email_writer.dto.EmailRequest;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;

import org.springframework.core.io.ClassPathResource;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailGenerationService {

    private final ChatClient chatClient;

    public EmailGenerationService(
            ChatClient chatClient
    ) {
        this.chatClient = chatClient;
    }

    private String executePrompt(
            String folder,
            Map<String, Object> variables
    ) {

        PromptTemplate userPromptTemplate =
                new PromptTemplate(
                        new ClassPathResource(
                                "prompts/" + folder + "/user.st"
                        )
                );

        String userPrompt =
                userPromptTemplate.render(variables);

        PromptTemplate systemPromptTemplate =
                new PromptTemplate(
                        new ClassPathResource(
                                "prompts/" + folder + "/system.st"
                        )
                );

        String systemPrompt =
                systemPromptTemplate.render();

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }

    public String generateReply(
            EmailRequest request
    ) {

        String tone =
                request.getTone() != null
                        ? request.getTone()
                        : "professional";

        String recommendation =
                request.getUserRecommendation() != null
                        ? request.getUserRecommendation()
                        : "";

        return executePrompt(
                "email-reply-generation",
                Map.of(
                        "emailContent",
                        request.getEmailContent(),

                        "userRecommendation",
                        recommendation,

                        "tone",
                        tone
                )
        );
    }

    public String summarizeEmail(
            EmailRequest request
    ) {

        return executePrompt(
                "email-summary",
                Map.of(
                        "emailContent",
                        request.getEmailContent()
                )
        );
    }

    public String rewriteEmail(
            EmailRequest request
    ) {

        String tone =
                request.getTone() != null
                        ? request.getTone()
                        : "professional";

        return executePrompt(
                "email-rewrite",
                Map.of(
                        "emailContent",
                        request.getEmailContent(),

                        "tone",
                        tone
                )
        );
    }

    public String fixGrammar(
            EmailRequest request
    ) {

        return executePrompt(
                "email-grammar",
                Map.of(
                        "emailContent",
                        request.getEmailContent()
                )
        );
    }
}