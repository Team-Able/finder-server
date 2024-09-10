package com.finder.domain.email.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Email", description = "이메일 API")
@RestController
@RequestMapping("/email")
public class EmailController {
    @GetMapping("/check")
    public Boolean checkEmail(@RequestParam String email) {
        return true;
    }

    @PostMapping("/send")
    public void sendEmail(@RequestParam String email) {
        return;
    }
}
