package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemEntity;

import java.util.List;

public record ItemDetailCommentResponse(
        List<ItemCommentResponse> comments
) {
    public static ItemDetailCommentResponse of(ItemEntity item) {
        return new ItemDetailCommentResponse(item.getComments().stream().filter(comment -> comment.getParent() == null).map(ItemCommentResponse::of).toList());
    }
}
