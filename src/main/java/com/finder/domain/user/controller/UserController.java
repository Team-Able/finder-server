package com.finder.domain.user.controller;

import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.user.dto.request.UserUpdateRequest;
import com.finder.domain.user.dto.response.RealFinalMyCommentsResponse;
import com.finder.domain.user.dto.response.UserResponse;
import com.finder.domain.user.service.UserService;
import com.finder.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "내 글 조회")
    @GetMapping("/posts")
    public ResponseEntity<BaseResponse<List<ItemResponse>>> getMyItems() {
        return BaseResponse.of(userService.getMyItems(), 200, "내 글 조회 성공");
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/secession")
    public ResponseEntity<BaseResponse<Void>> secession() {
        userService.secession();
        return BaseResponse.of(null, 200, "회원 탈퇴 성공");
    }

    @Operation(summary = "내 정보 수정")
    @PatchMapping("/me")
    public ResponseEntity<BaseResponse<Void>> updateMe(@RequestBody UserUpdateRequest request) {
        userService.updateUser(request);
        return BaseResponse.of(null, 200, "내 정보 수정 성공");
    }

    @Operation(summary = "내 댓글 조회")
    @GetMapping("/comments")
    public ResponseEntity<BaseResponse<RealFinalMyCommentsResponse>> getMyComments() {
        return BaseResponse.of(userService.getMyComments(), 200, "sadas");
    }
}
