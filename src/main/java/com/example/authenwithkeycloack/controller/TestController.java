package com.example.authenwithkeycloack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/member/hello")
    public ResponseEntity<String> helloMember() {
        return ResponseEntity.ok("Hello dear member");
    }

    @GetMapping("/moderator/hello")
    public ResponseEntity<String> helloModerator() {
        return ResponseEntity.ok("Hello Moderator");
    }


    @GetMapping("/admin/hello")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("Nice day, admin");
    }

    @GetMapping("/service/user")
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok("All user information");
    }
}
