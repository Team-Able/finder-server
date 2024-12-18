package com.finder.domain.item.dto.request;

public record ItemCommentUpdateRequest(
        Long itemId,
        String content
) {
}
