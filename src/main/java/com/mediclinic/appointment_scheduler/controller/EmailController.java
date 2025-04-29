package com.mediclinic.appointment_scheduler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.appointment_scheduler.service.EmailService;
import com.mediclinic.appointment_scheduler.util.annotation.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("Gửi email")
    public ResponseEntity<?> sendEmail() {
        emailService.sendEmail();
        return ResponseEntity.ok("Đặt lịch thành công, vui lòng kiểm tra hộp thư.");
    }

}
