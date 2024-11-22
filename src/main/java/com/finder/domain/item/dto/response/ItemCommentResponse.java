package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemCommentEntity;

import java.time.LocalDateTime;
import java.util.List;
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
        // children 필드를 중복 없이 처리
        List<ItemCommentResponse> childrenResponses = comment.getChildren().stream()
                .map(ItemCommentResponse::of) // 재귀적으로 자식 댓글 처리
                .distinct()  // 중복된 댓글을 제거
                .collect(Collectors.toList());

        return new ItemCommentResponse(
                comment.getId(),
                comment.getContent(),
                ItemCommentAuthorResponse.of(comment.getAuthor()),
                childrenResponses,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}

