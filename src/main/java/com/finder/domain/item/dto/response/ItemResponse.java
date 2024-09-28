package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.enums.ItemStatus;

import java.time.LocalDateTime;

public record ItemResponse(
        Long id,
        String title,
        String content,
        ItemStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ItemResponse of(ItemEntity item) {
        return new ItemResponse(item.getId(), item.getTitle(), item.getContent(), item.getStatus(), item.getCreatedAt(), item.getUpdatedAt());
    }
}
