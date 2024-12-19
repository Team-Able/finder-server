package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemCommentEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record ItemCommentResponse(
        Long id,
        String content,
        ItemCommentAuthorResponse author,
        List<ItemCommentResponse> children,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ItemCommentResponse of(ItemCommentEntity comment) {
        return new ItemCommentResponse(
                comment.getId(), comment.getContent(), ItemCommentAuthorResponse.of(comment.getAuthor()), Optional.ofNullable(comment.getChildren()).orElse(Collections.emptyList()).stream().map(ItemCommentResponse::of).collect(Collectors.toList()), comment.getCreatedAt(), comment.getUpdatedAt()
        );
    }
}

