package com.email_writer.email_writer.controller;

import com.email_writer.email_writer.dto.EmailRequest;
import com.email_writer.email_writer.dto.EmailResponse;
import com.email_writer.email_writer.service.EmailGenerationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "*")
public class EmailGenerationController {

    private final EmailGenerationService service;

    public EmailGenerationController(EmailGenerationService service) {
        this.service = service;
    }

    // Health Check API
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "UP", "message", "Email Writer Service is running"));
    }

    @PostMapping("/reply")
    public ResponseEntity<EmailResponse> generateReply(@RequestBody EmailRequest request) {

        String result = service.generateReply(request);

        return ResponseEntity.ok(new EmailResponse(result));
    }

    @PostMapping("/summarize")
    public ResponseEntity<EmailResponse> summarize(@RequestBody EmailRequest request) {

        String result = service.summarizeEmail(request);

        return ResponseEntity.ok(new EmailResponse(result));
    }

    @PostMapping("/rewrite")
    public ResponseEntity<EmailResponse> rewrite(@RequestBody EmailRequest request) {

        String result = service.rewriteEmail(request);

        return ResponseEntity.ok(new EmailResponse(result));
    }

    @PostMapping("/grammar")
    public ResponseEntity<EmailResponse> grammar(@RequestBody EmailRequest request) {

        String result = service.fixGrammar(request);

        return ResponseEntity.ok(new EmailResponse(result));
    }
}