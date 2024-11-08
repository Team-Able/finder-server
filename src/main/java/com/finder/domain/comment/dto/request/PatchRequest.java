package com.finder.domain.comment.dto.request;

public record PatchRequest(
        Long commentId,
        String content
) {
}
