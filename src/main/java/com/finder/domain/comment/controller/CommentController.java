package com.finder.domain.comment.controller;

import com.finder.domain.comment.dto.request.CoCommentRequest;
import com.finder.domain.comment.dto.request.CommentRequest;
import com.finder.domain.comment.dto.request.PatchRequest;
import com.finder.domain.comment.dto.response.CoCommentResponse;
import com.finder.domain.comment.dto.response.CommentResponse;
import com.finder.domain.comment.service.CommentService;
import com.finder.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/item/comments")
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/parent/{postId}")
    @Operation(summary = "댓글 작성", description = "Path(Long) : 게시물Id 입력 | Body:{ author(UUID) : 작성자id / content(String) : 내용 }")
    public ResponseEntity<BaseResponse<CommentResponse>> createComment (@PathVariable Long postId, @RequestBody CommentRequest request) {
        return BaseResponse.of(commentService.createComment(postId, request), 200, "댓글 작성 성공");
    }

    @PostMapping("/child/{postId}")
    @Operation(summary = "대댓글 작성", description = "Body:{ author : 작성자id(UUID) / content : 내용(String) / parent(Long) : 상위 댓글id }")
    public ResponseEntity<BaseResponse<CoCommentResponse>> createCoComment(@RequestBody CoCommentRequest request) {
        return BaseResponse.of(commentService.createCoComment(request), 200, "대댓글 작성 성공");
    }

    @PatchMapping("/parent/patch")
    @Operation(summary = "댓글 수정", description = "Body:{ commentId : 댓글id(Long) / content : 수정내용(String) }")
    public ResponseEntity<BaseResponse<CommentResponse>> updateComment (@RequestBody PatchRequest request) {
        return BaseResponse.of(commentService.updateComment(request), 200, "댓글 수정 성공");
    }


    @PatchMapping("/child/patch")
    @Operation(summary = "대댓글 수정", description = "Body:{ commentId : 대댓글id(Long) / content : 수정내용(String) }")
    public ResponseEntity<BaseResponse<CoCommentResponse>> updateCoComment (@RequestBody PatchRequest request) {
        return BaseResponse.of(commentService.updateCoComment(request), 200, "대댓글 수정 성공");
    }

    @DeleteMapping("/parent/delete")
    @Operation(summary = "댓글 삭제", description = "Body:{ id : 댓글id(Long) }")
    public ResponseEntity<BaseResponse<Void>> deleteComment (@RequestBody Long id) {
        commentService.deleteComment(id);
        return BaseResponse.of(null, 200,"댓글 삭제 성공");
    }

    @DeleteMapping("/child/delete")
    @Operation(summary = "대댓글 삭제", description = "Body:{ id : 댓글id(Long) }")
    public ResponseEntity<BaseResponse<Void>> deleteCoComment (@RequestBody Long id) {
        commentService.deleteCoComment(id);
        return BaseResponse.of(null, 200, "대댓글 삭제 성공");
    }




}
