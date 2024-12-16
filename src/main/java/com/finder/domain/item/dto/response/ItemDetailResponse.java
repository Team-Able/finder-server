package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.entity.ItemLocation;
import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.domain.user.domain.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record ItemDetailResponse(
        Long id,
        String title,
        UserEntity author,
        String content,
        String imageUrl,
        ItemStatus status,
        ItemLocation location,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ItemDetailResponse of(ItemEntity item) {
        return new ItemDetailResponse(item.getId(), item.getTitle(), item.getAuthor(), item.getContent(), item.getImageUrl(), item.getStatus(),item.getLocation(), item.getCreatedAt(), item.getUpdatedAt());
    }
}
