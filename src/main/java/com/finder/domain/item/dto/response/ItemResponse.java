package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.entity.ItemLocation;
import com.finder.domain.item.domain.enums.ItemStatus;

import java.time.LocalDateTime;

public record ItemResponse(
        Long id,
        String title,
        ItemAuthorResponse author,
        String content,
        String imageUrl,
        ItemStatus status,
        ItemLocation location,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ItemResponse of(ItemEntity item) {
        return new ItemResponse(item.getId(), item.getTitle(), ItemAuthorResponse.of(item.getAuthor()), item.getContent(), item.getImageUrl(), item.getStatus(), item.getLocation(), item.getCreatedAt(), item.getUpdatedAt());
    }
}
