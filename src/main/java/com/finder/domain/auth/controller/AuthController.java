package com.finder.domain.auth.controller;

import com.finder.domain.auth.dto.request.LoginRequest;
import com.finder.domain.auth.dto.request.ReissueRequest;
import com.finder.domain.auth.dto.request.SignUpRequest;
import com.finder.domain.auth.service.AuthService;
import com.finder.global.security.jwt.dto.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public void signup(@RequestBody SignUpRequest request) {
        authService.signup(request);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public Jwt login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    public Jwt reissue(@RequestBody ReissueRequest request) {
        return authService.reissue(request);
    }
}
