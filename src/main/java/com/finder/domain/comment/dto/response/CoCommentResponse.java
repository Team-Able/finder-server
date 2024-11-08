package com.finder.domain.comment.dto.response;

import com.finder.domain.comment.domain.entity.CoCommentEntity;
import com.finder.domain.comment.domain.entity.CommentEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record CoCommentResponse(
        Long id,
        UUID author,
        String content,
        Long parent,
        LocalDateTime createdAt,
        LocalDateTime updatedAt


) {
    public static CoCommentResponse of(CoCommentEntity comment) {
        return new CoCommentResponse(comment.getId(), comment.getAuthor(), comment.getContent(), comment.getParent().getId(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
