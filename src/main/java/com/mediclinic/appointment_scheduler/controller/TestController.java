package com.mediclinic.appointment_scheduler.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public ResponseEntity<String> getHomePage() {
        return ResponseEntity.ok().body("Xin chào Đỗ Văn Annn");
    }
}
