package com.jark.TMS.controllers;

import com.jark.TMS.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;
    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/sendEmail")
    public String sendEmail() {
        emailService.sendEmail("Y666N@yandex.ru", "Test Subject", "Test Body");
        return "Email sent successfully!";
    }
}
