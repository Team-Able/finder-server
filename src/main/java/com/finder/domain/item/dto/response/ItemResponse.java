package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.entity.ItemLocation;
import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.domain.item.repository.ItemCommentRepository;

import java.time.LocalDateTime;
import java.util.List;

public record ItemResponse(
        Long id,
        String title,
        ItemAuthorResponse author,
        String content,
        String imageUrl,
        Long commentCount,
        ItemStatus status,
        ItemLocation location,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ItemResponse of(ItemEntity item) {
        return new ItemResponse(item.getId(), item.getTitle(), ItemAuthorResponse.of(item.getAuthor()) , item.getContent(), item.getImageUrl(), countAllComments(item.getComments(), item.getComments().stream().filter(comment -> comment.getParent() == null).count()), item.getStatus(), item.getLocation(), item.getCreatedAt(), item.getUpdatedAt());
    }

    private static long countAllComments(List<ItemCommentEntity> comments, Long PCommentsSum) {
        return PCommentsSum +
                comments.stream()
                .mapToLong(ItemResponse::countChildComments)
                .sum();
    }

    private static long countChildComments(ItemCommentEntity comment) {
        return comment.getChildren().stream()
                .mapToLong(child -> 1)
                .sum();
    }
}
