package com.mediclinic.appointment_scheduler.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.User;
import com.mediclinic.appointment_scheduler.util.SecurityUtil;

@Service
public class EmailService {

    private final UserService userService;
    private final MailSender mailSender;

    public EmailService(UserService userService, MailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    public void sendEmail() {
        String email = SecurityUtil.getCurrentUserLogin().orElse(null);

        if (email == null) {
            System.err.println("Không xác định được người dùng");
            return;
        }

        User user = userService.handleGetUserByUsername(email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Chào " + user.getName());
        message.setText("Chúc mừng bạn đã đặt lịch hẹn thành công");
        mailSender.send(message);
    }
}
