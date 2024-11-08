package com.finder.domain.comment.dto.request;

import com.finder.domain.comment.domain.entity.CommentEntity;

import java.util.UUID;

public record CoCommentRequest(
        UUID author,
        String content,
        Long parent
) {
}
