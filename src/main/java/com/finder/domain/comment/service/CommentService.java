package com.finder.domain.comment.service;

import com.finder.domain.comment.dto.request.CoCommentRequest;
import com.finder.domain.comment.dto.request.CommentRequest;
import com.finder.domain.comment.dto.request.PatchRequest;
import com.finder.domain.comment.dto.response.CoCommentResponse;
import com.finder.domain.comment.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(Long itemId,CommentRequest commentRequest);
    CoCommentResponse createCoComment(CoCommentRequest commentRequest);
    CommentResponse updateComment(PatchRequest request);
    CoCommentResponse updateCoComment(PatchRequest request);
    void deleteComment(Long commentId);
    void deleteCoComment(Long commentId);
}
