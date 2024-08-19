package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.enums.ItemStatus;

public record ItemResponse(
        Long id,
        String title,
        String content,
        ItemStatus status
) {
    public static ItemResponse of(ItemEntity item) {
        return new ItemResponse(item.getId(), item.getTitle(), item.getContent(), item.getStatus());
    }
}
