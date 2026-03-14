package com.email_writer.email_writer.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String emailContent;
    private String userRecommendation;
    private String tone;
}
