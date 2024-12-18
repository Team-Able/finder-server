package com.finder.domain.item.dto.request;

public record ItemCommentCreateRequest(
        Long itemId,
        String content,
        Long parentId
) {
}
