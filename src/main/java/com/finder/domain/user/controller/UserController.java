package com.finder.domain.user.controller;

import com.finder.domain.user.dto.response.UserResponse;
import com.finder.domain.user.service.UserService;
import com.finder.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserResponse>> getMe() {
        return BaseResponse.of(userService.getMe(), 200, "내 정보 조회 성공");
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/secession")
    public ResponseEntity<BaseResponse<Void>> secession()  {
        userService.secession();
        return BaseResponse.of(null, 200, "회원 탈퇴 성공");
    }
}
