package com.finder.domain.comment.dto.request;


import java.util.UUID;

public record CommentRequest(
        UUID author,
        String content
) {
}
