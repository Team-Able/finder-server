package com.finder.domain.item.controller;

import com.finder.domain.item.dto.request.ItemCommentCreateRequest;
import com.finder.domain.item.dto.request.ItemCommentUpdateRequest;
import com.finder.domain.item.dto.response.ItemCommentResponse;
import com.finder.domain.item.service.ItemCommentService;
import com.finder.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ItemComment", description = "분실물 댓글 관련 API")
@RestController
@RequestMapping("/items/{itemId}/comments")
@RequiredArgsConstructor
public class ItemCommentController {
    private final ItemCommentService itemCommentService;

    @Operation(summary = "댓글 작성")
    @PostMapping
    public ResponseEntity<BaseResponse<ItemCommentResponse>> createComment(@PathVariable Long itemId, @Valid @RequestBody ItemCommentCreateRequest request) {
        return BaseResponse.of(itemCommentService.createItemComment(itemId, request), 200, "댓글 작성 성공");
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseEntity<BaseResponse<ItemCommentResponse>> updateComment(@PathVariable Long itemId, @PathVariable Long commentId, @Valid @RequestBody ItemCommentUpdateRequest request) {
        return BaseResponse.of(itemCommentService.updateItemComment(itemId,commentId,request), 200, "댓글 수정 성공");
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(@PathVariable Long itemId, @PathVariable Long commentId) {
        itemCommentService.deleteItemComment(itemId,commentId);

        return BaseResponse.of(null, 200, "댓글 삭제 성공");
    }
}
