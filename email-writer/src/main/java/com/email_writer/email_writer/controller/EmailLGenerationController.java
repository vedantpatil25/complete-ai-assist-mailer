package com.email_writer.email_writer.controller;

import com.email_writer.email_writer.dto.EmailRequest;
import com.email_writer.email_writer.dto.EmailResponse;
import com.email_writer.email_writer.service.EmailLGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "*")
public class EmailLGenerationController {

    private final EmailLGenerationService emailLGenerationService;

    @Autowired
    public EmailLGenerationController(EmailLGenerationService emailLGenerationService) {
        this.emailLGenerationService = emailLGenerationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<EmailResponse> generateReply(@RequestBody EmailRequest emailRequest) {

        String generatedReply = emailLGenerationService.generateEmailReply(emailRequest);

        EmailResponse response = new EmailResponse();
        response.setResponse(generatedReply);

        return ResponseEntity.ok(response);
    }
}