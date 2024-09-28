package com.finder.domain.auth.controller;

import com.finder.domain.auth.dto.request.LoginRequest;
import com.finder.domain.auth.dto.request.ReissueRequest;
import com.finder.domain.auth.dto.request.SignUpRequest;
import com.finder.domain.auth.service.AuthService;
import com.finder.global.common.BaseResponse;
import com.finder.global.security.jwt.dto.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse<Void>> signup(@Valid @RequestBody SignUpRequest request) {
        authService.signup(request);

        return BaseResponse.of(null, 201, "회원가입 성공");
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse<Jwt>> login(@Valid @RequestBody LoginRequest request) {
        return BaseResponse.of(authService.login(request), 200, "로그인 성공");
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse<Jwt>> reissue(@Valid @RequestBody ReissueRequest request) {
        return BaseResponse.of(authService.reissue(request), 200, "토큰 재발급 성공");
    }
}
