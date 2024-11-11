package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemCommentEntity;

import java.time.LocalDateTime;
import java.util.List;

public record ItemCommentResponse(
        Long id,
        String content,
        ItemCommentAuthorResponse author,
        List<ItemCommentResponse> children,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ItemCommentResponse of(ItemCommentEntity comment) {
        return new ItemCommentResponse(comment.getId(), comment.getContent(), ItemCommentAuthorResponse.of(comment.getAuthor()), comment.getChildren().stream().map(ItemCommentResponse::of).toList(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
