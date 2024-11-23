package com.finder.domain.item.dto.request;

public record ItemCommentCreateRequest(
        String content,
        Long parentId
) {
}
